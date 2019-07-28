// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.graphics.Point;
import android.os.Handler;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Space;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

//Adding two flags at l46 and l47
class SpaceGame  implements StatusManager{
    /* Action Flags */

    public static final int GAME_START =0b00000001;
    // Missile has been released and is moving(striking)
    // If an game object, say invaders, encounter STRIKE,
    // it means it may get hit
    public static final int STRIKE=0b00000010;
    public static final int TOUCH=0b00000100;
    public static final int MISSILE_GONE=0b00001000;
    public static final int MOVE_LEFT=0b00010000;
    public static final int MOVE_RIGHT=0b00100000;
    public static final int LIFE_ADD=0b01000000;
    public static final int LIFE_GONE=0b010000000;
    public static final int RESURRECTION=0b0100000000;
    public static final int MOVE_STOP=0b010000000000;
    public static final int HIT=0b100000000000;
    public static final int GAME_PAUSE=0b1000000000000;
    public static final int GAME_RESUME=0b10000000000000;
    public static final int GAME_STOP=0b100000000000000;
    //TEST only
    public static final int TEST=0b0100001;
    // The moment at which laserBase or invader fires the missile
    public static final int FIRE=0b1000000000;
    /* Resource Flags */
    public static final int WINDOW_SIZE=0b0000001;
    public static final int RESOURCES=0b0000010;
    public static final int CONTEXT=0b0000100;
    public static final int X_COORDINATE=0b0001000;
    public static final int Y_COORDINATE=0b0010000;
    public static final int X_WIDTH=0b0100000;
    public static final int Y_HEIGHT=0b1000000;
    public static final int MOVE_DIRECTION=0b10000000;

    /* Status Flags */
    public static final int NUM_INVADER=0b0000001;
    public static final int NUM_LIVES=0b0000010;
    public static final int INTEGRITY_OF_SHELTER=0b0000100;
    public static final int SCORES=0b0001000;
    public static final int LEVEL=0b0010000;
    public static final int PERKS_OF_LASERBASE=0b0100000;
    public static final int STATE=0b1000000;

    /* Animated Object type */
    public static final int LASER_BASE=0b0000001;
    public static final int BASE_SHELTER_GROUP=0b0000010;
    public static final int INVADER_GROUP=0b0000100;
    public static final int UFO_INVADER=0b0001000;

    final AnimatedObject laserBase;
    final AnimatedObject baseShelterGroup;
    final AnimatedObject invaderGroup;
    final AnimatedObject UFO;

    final AnimatedObjectBox animatedObjects;

    final MissilePool missilePool;
    final StatusManager hud;
    final Resources resources;

    private Status status;

    private State state;


    public SpaceGame (AnimatedObject laserBase, AnimatedObject baseShelterGroup, AnimatedObject invaderGroup, AnimatedObject missile, AnimatedObject UFO, StatusManager hud, Resources resources, Status status, ViewGroup layout, Handler mainHandler, Handler processThread, SoundEngine se){
        this.laserBase=laserBase;
        this.baseShelterGroup=baseShelterGroup;
        this.invaderGroup=invaderGroup;
        this.UFO = UFO;
        this.hud=hud;
        this.missilePool = new MissilePool.Builder(20).setLayout(layout)
                .setResources(resources).setMainHandler(mainHandler)
                .setProcessHandler(processThread).setStatus(status).setSpaceGame(this).setSoundEngine(se)
                .build();  // setCapacity needs to be called at the very last
        this.laserBase.setSpaceGame(this);
        this.baseShelterGroup.setSpaceGame(this);
        this.invaderGroup.setSpaceGame(this);
        this.UFO.setSpaceGame(this);
        this.animatedObjects=new AnimatedObjectBox();

        this.animatedObjects.put(SpaceGame.LASER_BASE,this.laserBase);
        this.animatedObjects.put(SpaceGame.BASE_SHELTER_GROUP,this.baseShelterGroup);
        this.animatedObjects.put(SpaceGame.INVADER_GROUP,this.invaderGroup);
        this.animatedObjects.put(SpaceGame.UFO_INVADER,this.UFO);

        ((AnimatedObject)this.hud).setSpaceGame(this);

        this.resources=resources;
        this.status=status;

        AnimatedObject.Actions actions=new AnimatedObject.Actions();
        actions.put(GAME_START,new Pair<AnimatedObject, SparseArray<Float>>(null,null));
        invaderGroup.handle(actions);
        UFO.handle(actions);

        AnimatedObject.Actions actions2 = new AnimatedObject.Actions();
        actions2.put(LIFE_ADD,new Pair<AnimatedObject, SparseArray<Float>>(null,null));
//        hud.handle(actions2);

        status.put(SpaceGame.SCORES,new Pair<>(0f,null));
        //Below Test only
/*        status.put(SpaceGame.NUM_LIVES,new Pair<>(Float.valueOf(1),null));//TODO:Bug: LIVEMAX Doesnt work
        updateStatus(status);*/


    }

    @Override
    public void updateStatus(Status status){
        Set<Integer> keys=status.keySet();
        for (Integer key:keys){
            switch (key){
                case SpaceGame.NUM_INVADER:
                    break;
                case SpaceGame.NUM_LIVES:
                    // laserBase loses on life
                    hud.updateStatus(status);
                    ((LaserBase)laserBase).spawn();
                    // TODO: pause the game for 3 seconds
                    break;
                case SpaceGame.SCORES:
                    hud.updateStatus(status);
                    break;
                case SpaceGame.STATE:
                    if (Objects.requireNonNull(status.get(key)).first==1f){
                        this.setState(new PausedGame());
                    }
                default:

                    break;
            }
        }

    }

    private void setState(State state) {
        this.state = state;
        this.state.notifyState(this.animatedObjects);
    }

    public void onTouch(MotionEvent event){
        AnimatedObject.Actions actions=new AnimatedObject.Actions();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                Point size= (Point) this.resources.get(SpaceGame.WINDOW_SIZE);
                assert size != null;
                int motion;
                if (event.getRawX()>size.x/2&&event.getRawY()>laserBase.getY()){
                    motion=SpaceGame.MOVE_RIGHT;
                }else if (event.getRawX()<size.x/2&&event.getRawY()>laserBase.getY()){
                    motion=SpaceGame.MOVE_LEFT;
                }else {
                    motion=SpaceGame.FIRE;
                }
                actions.put(motion, null);
                this.laserBase.handle(actions);
                // debug
                if(motion == SpaceGame.FIRE)
                {

                }
                break;

            case MotionEvent.ACTION_UP:
                actions.put(SpaceGame.MOVE_STOP,null);
                this.laserBase.handle(actions);
                break;
        }
    }

    static class Status extends  HashMap<Integer, Pair<Float,Float>>{

    }

    static class Resources extends  HashMap<Integer, Object>{

    }

    static class AnimatedObjectBox extends HashMap<Integer, AnimatedObject>{

    }

    static class MutablePair <T,K> {
        public T first;
        public K second;
        MutablePair (T first,K second){
            this.first=first;
            this.second=second;
        }
    }

    interface State{
        void notifyState(AnimatedObjectBox animatedObjects);
    }

    class PausedGame implements State{

        @Override
        public void notifyState(AnimatedObjectBox animatedObjects) {
            Collection<AnimatedObject> objects=animatedObjects.values();
            AnimatedObject.Actions actions=new AnimatedObject.Actions();
            actions.put(SpaceGame.GAME_PAUSE,null);
            for (AnimatedObject object:objects){
                object.handle(actions,SpaceGame.GAME_PAUSE);
            }
        }
    }

    class RunningGame implements State{

        @Override
        public void notifyState(AnimatedObjectBox animatedObjects) {
            Collection<AnimatedObject> objects=animatedObjects.values();
            AnimatedObject.Actions actions=new AnimatedObject.Actions();
            actions.put(SpaceGame.GAME_PAUSE,null);
            for (AnimatedObject object:objects){
                object.handle(actions,SpaceGame.GAME_RESUME);
            }
        }
    }

    class EndedGame implements State{

        @Override
        public void notifyState(AnimatedObjectBox animatedObjects) {
            Collection<AnimatedObject> objects=animatedObjects.values();
            AnimatedObject.Actions actions=new AnimatedObject.Actions();
            actions.put(SpaceGame.GAME_PAUSE,null);
            for (AnimatedObject object:objects){
                object.handle(actions,SpaceGame.GAME_STOP);
            }
        }
    }

}
