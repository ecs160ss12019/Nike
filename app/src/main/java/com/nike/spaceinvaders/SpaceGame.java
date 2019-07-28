// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.graphics.Point;
import android.os.Handler;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Set;

//Adding two flags at l46 and l47
class SpaceGame  implements StatusManager{
    /* Action Flags */
    public static final int GAMESTART=0b00000001;
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

    final AnimatedObject laserBase;
    final AnimatedObject baseShelterGroup;
    final AnimatedObject invaderGroup;
    final MissilePool missilePool;
    final StatusManager hud;
    final Resources resources;

    private Status status;


    public SpaceGame (AnimatedObject laserBase, AnimatedObject baseShelterGroup, AnimatedObject invaderGroup, AnimatedObject missile, StatusManager hud, Resources resources, Status status, ViewGroup layout, Handler mainHandler, Handler processThread, SoundEngine se){
        this.laserBase=laserBase;
        this.baseShelterGroup=baseShelterGroup;
        this.invaderGroup=invaderGroup;
        this.hud=hud;
        this.missilePool = new MissilePool.Builder(20).setLayout(layout)
                .setResources(resources).setMainHandler(mainHandler)
                .setProcessHandler(processThread).setStatus(status).setSpaceGame(this).setSoundEngine(se)
                .build();  // setCapacity needs to be called at the very last
        this.laserBase.setSpaceGame(this);
        this.baseShelterGroup.setSpaceGame(this);
        this.invaderGroup.setSpaceGame(this);
        ((AnimatedObject)this.hud).setSpaceGame(this);

        this.resources=resources;
        this.status=status;

        AnimatedObject.Actions actions=new AnimatedObject.Actions();
        actions.put(GAMESTART,new Pair<AnimatedObject, SparseArray<Float>>(null,null));
        invaderGroup.handle(actions);

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
                default:
                    break;
            }
        }
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

    static class MutablePair <T,K> {
        public T first;
        public K second;
        MutablePair (T first,K second){
            this.first=first;
            this.second=second;
        }
    }

}
