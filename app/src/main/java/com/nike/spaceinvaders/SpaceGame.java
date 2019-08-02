/*
- UI and framework of this game
- Shows the graphical background of the game, which is the universe
- Contains a list of objects in the game
 */

package com.nike.spaceinvaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

//Adding two flags at l46 and l47
class SpaceGame  implements StatusManager, SensorEventListener {
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
    public static final int MOVE_STOP=  0b010000000000;
    public static final int HIT=        0b100000000000;
    public static final int GAME_PAUSE= 0b1000000000000;
    public static final int GAME_RESUME=0b10000000000000;
    public static final int GAME_STOP=  0b100000000000000;
    public static final int CONTACT=    0b1000000000000000;
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
    public static final int MOVE_DIRECTION=0b10000000;
    public static final int GRAVITY=0b100000000;

    /* Status Flags */
    public static final int NUM_INVADER=            0b0000001;
    public static final int NUM_LIVES=              0b0000010;
    public static final int SCORES=                 0b0000100;
    public static final int LEVEL=                  0b0001000;
    public static final int PERKS_OF_LASERBASE=     0b0010000;
    public static final int STATE=                  0b0100000;
    public static final int GAME_OVER=              0b1000000;

    /* Animated Object type */
    public static final int LASER_BASE=0b0000001;
    public static final int BASE_SHELTER_GROUP=0b0000010;
    public static final int INVADER_GROUP=0b0000100;
    public static final int UFO_INVADER=0b0001000;
    public static final int HUD=0b0010000;

    /* Setting Flags */
    public static final int GRAVITY_SETTING=0b0000001;
    public static final int DIFFICULITY=0b0000010;
    public static final int a=0b0000100;
    public static final int ds=0b0001000;
    private static final String TAG = "SpaceGame";

    /* State Flags */
    public static final int RUNNING_STATE=0b0000001;
    public static final int PAUSED_STATE=0b0000010;
    public static final int ENDED_STATE=0b0000100;

    final AnimatedObject laserBase;
    final AnimatedObject baseShelterGroup;
    final AnimatedObject invaderGroup;
    final AnimatedObject ufo;
    final AnimatedObject hud;

    final AnimatedObjectBox animatedObjects;

    final MissilePool missilePool;

    final Resources resources;
    private Status status;
    private Bundle setting;

    private State state;

    private int LIVEMAX=3;
    private int MAXINVADER=24;

    public SpaceGame (AnimatedObject laserBase, AnimatedObject baseShelterGroup, AnimatedObject invaderGroup, AnimatedObject missile, AnimatedObject ufo, AnimatedObject hud, Resources resources, Status status, ViewGroup layout, Handler mainHandler, Handler processThread, SoundEngine se){
        this.laserBase=laserBase;
        this.baseShelterGroup=baseShelterGroup;
        this.invaderGroup=invaderGroup;
        this.ufo = ufo;
        this.hud=hud;
        this.missilePool = new MissilePool.Builder(20).setLayout(layout)
                .setResources(resources).setMainHandler(mainHandler)
                .setProcessHandler(processThread).setStatus(status).setSpaceGame(this).setSoundEngine(se)
                .build();  // setCapacity needs to be called at the very last

        this.laserBase.setSpaceGame(this);
        this.laserBase.setHitDetection(LaserBase.HIT_DETECTION,new NormalHitDetection());
        this.baseShelterGroup.setSpaceGame(this);
        this.baseShelterGroup.setHitDetection(BaseShelter.HIT_DETECTION_NORMAL,new PreciseHitDetection());
        this.baseShelterGroup.setHitDetection(BaseShelter.HIT_DETECTION_PRECISE,new PreciseHitDetection());
        this.ufo.setHitDetection(UFO.HIT_DETECTION,new NormalHitDetection());
        this.hud.setSpaceGame(this);


        this.invaderGroup.setSpaceGame(this);
        this.ufo.setSpaceGame(this);

        this.animatedObjects=new AnimatedObjectBox();
        this.animatedObjects.put(SpaceGame.LASER_BASE,this.laserBase);
        this.animatedObjects.put(SpaceGame.BASE_SHELTER_GROUP,this.baseShelterGroup);
        this.animatedObjects.put(SpaceGame.INVADER_GROUP,this.invaderGroup);
        this.animatedObjects.put(SpaceGame.UFO_INVADER,this.ufo);
        this.animatedObjects.put(SpaceGame.HUD,this.hud);



        (this.hud).setSpaceGame(this);

        this.resources=resources;
        this.status=status;
        status.put(SpaceGame.LEVEL,new Pair<>(1f,null));
        status.put(SpaceGame.PERKS_OF_LASERBASE,new Pair<>(0f,null));
        status.put(SpaceGame.NUM_LIVES,new Pair<>((float)this.LIVEMAX,null));
        status.put(SpaceGame.NUM_INVADER,new Pair<>((float) this.MAXINVADER,null));
        for (AnimatedObject object:this.animatedObjects.values()){
            object.setStatus((Status) status.clone());
        }
        AnimatedObject.Actions actions=new AnimatedObject.Actions();
        actions.put(GAME_START,new Pair<AnimatedObject, SparseArray<Float>>(null,null));
        invaderGroup.handle(actions);
        ufo.handle(actions);


    }

    public void setSetting(Bundle setting) {
        this.setting = setting;
        this.LIVEMAX-=setting.getInt(String.valueOf(SpaceGame.DIFFICULITY));
        Float lives=this.status.get(SpaceGame.NUM_LIVES).first;
        if (lives==null||lives>this.LIVEMAX){
            lives=(float) this.LIVEMAX;
        }
        this.status.put(SpaceGame.NUM_LIVES,new Pair<>(lives,null));
        updateStatus(status);
    }

    @Override
    public void updateStatus(Status status){
        Set<Integer> keys=status.keySet();
        Pair<Float,Float> newValue;
        Pair<Float,Float> oldValue;
        for (Integer key:keys){
            switch (key){
                case SpaceGame.NUM_INVADER:
                    newValue=status.get(key);
                    oldValue=this.status.get(key);

                    assert oldValue != null;
                    assert newValue != null;
//                    Log.d("Num_Invader_2_raw", String.valueOf(this.status.toString()));
                    Log.d("Num_Invader_2", String.valueOf(newValue));
                    Log.d("Num_Invader_2_old", String.valueOf(oldValue));
                    if (newValue.first<oldValue.first){
                        Log.d("Num_Invader_8", String.valueOf(newValue));
                        if(newValue.first<=0){
                            Log.d("Num_Invader_9", String.valueOf(newValue));
                            float level= Objects.requireNonNull(this.status.get(SpaceGame.LEVEL)).first;
                            this.status.put(SpaceGame.LEVEL,new Pair<>(level+1,null));
                            this.status.put(key,new Pair<>((float) this.MAXINVADER,null));
                        }else {
                            this.status.put(key,new Pair<>(newValue.first,null));
                        }

                    }
                    break;
                case SpaceGame.NUM_LIVES:
                    // laserBase loses one life
                    newValue=status.get(key);
                    oldValue=this.status.get(key);

                    assert newValue != null;
                    assert oldValue != null;
                    if (newValue.first<oldValue.first){
                        if (newValue.first<=0){
                        }else {
                            lifeGone();
                        }
                        this.status.put(key,new Pair<>(newValue.first,null));
                    }


                    break;
                case SpaceGame.SCORES:
                    newValue=status.get(key);
                    oldValue=this.status.get(key);

                    assert newValue != null;
                    assert oldValue != null;
                    if (!newValue.first.equals(oldValue.first)){
                        this.status.put(key,new Pair<>(newValue.first,null));
                    }
                    break;

                case SpaceGame.PERKS_OF_LASERBASE:
                    newValue=status.get(key);
                    oldValue=this.status.get(key);

                    assert newValue != null;
                    assert oldValue != null;
                    if (!newValue.first.equals(oldValue.first)){
                        this.status.put(key,new Pair<>(newValue.first,null));
                    }
                    break;

                default:
                    break;
            }
        }
        //Notify all the AnimatedObjects.
        for (AnimatedObject object:this.animatedObjects.values()){
            object.updateStatus(this.status);
        }
    }

    private void lifeGone() {
        //pause the game
            setState(new SpaceGame.PausedGame());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setState(new SpaceGame.RunningGame());
            }
        }, 1000);   //1 seconds


    }

    private void setState(State state) {
        this.state = state;
        this.state.notifyState(this.animatedObjects,this.missilePool);
    }

    public void setState(int flag) {
        switch (flag){
            case RUNNING_STATE:
                this.state=new RunningGame();
                break;
            case PAUSED_STATE:
                this.state=new PausedGame();
                break;
            case ENDED_STATE:
                this.state=new EndedGame();
                break;
        }

        this.state.notifyState(this.animatedObjects,this.missilePool);
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
//                    if (this.getState() instanceof PausedGame){
//                        this.setState(new RunningGame());
//                    }else {
//                        this.setState(new PausedGame());
//                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                actions.put(SpaceGame.MOVE_STOP,null);
                this.laserBase.handle(actions);
                break;
        }
    }

    public State getState() {
        return state;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        boolean gravitySetting=this.setting.getBoolean(String.valueOf(SpaceGame.GRAVITY));
        Bundle setting=this.setting;
        if (!setting.getBoolean(String.valueOf(SpaceGame.GRAVITY_SETTING))){
            return;
        }
        float gravity=event.values[1];
        float gravityX=event.values[0];
        float fraction=gravityX>7?(1):(gravityX<-7?-1:gravityX/7);
        int motion;
        AnimatedObject.Actions actions=new AnimatedObject.Actions();
//        Log.d("Gravity Fraction", String.valueOf(fraction));
        if (fraction>1f/7){
            motion=SpaceGame.MOVE_LEFT;
        }else if (fraction<-1f/7) {
            motion=SpaceGame.MOVE_RIGHT;
        }else {
            motion=SpaceGame.MOVE_STOP;
        }
        SparseArray<Float> value=new SparseArray<>();
        value.put(SpaceGame.GRAVITY,fraction);
        actions.put(motion, new Pair<>(null,value));
        this.laserBase.handle(actions);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void gameover() {
        Context context = (Context)this.resources.get(CONTEXT);
        Toast.makeText(context,"Entered gameover",Toast.LENGTH_LONG).show();
        //pause the game
        //start popup window
        Intent i = new Intent(context,Pop.class);
        i.putExtra("insignal","gameover");
        i.putExtra("score",this.status.get(SpaceGame.SCORES).first);
        ((Activity) context).startActivityForResult(i,0);
    }

    static class Status extends  HashMap<Integer, Pair<Float,Float>>{

    }

    static class Resources extends  HashMap<Integer, Object>{

    }

    private static class AnimatedObjectBox extends HashMap<Integer, AnimatedObject>{

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
        void notifyState(AnimatedObjectBox animatedObjects,MissilePool pool);
    }

    static class PausedGame implements State{

        @Override
        public void notifyState(AnimatedObjectBox animatedObjects,MissilePool pool) {
            Collection<AnimatedObject> objects=animatedObjects.values();
            AnimatedObject.Actions actions=new AnimatedObject.Actions();
            actions.put(SpaceGame.GAME_PAUSE,null);
            for (AnimatedObject object:objects){
                object.handle(actions,SpaceGame.GAME_PAUSE);
            }
            pool.pauseMissiles();
        }
    }

    static class RunningGame implements State{

        @Override
        public void notifyState(AnimatedObjectBox animatedObjects,MissilePool pool) {
            Collection<AnimatedObject> objects=animatedObjects.values();
            AnimatedObject.Actions actions=new AnimatedObject.Actions();
            actions.put(SpaceGame.GAME_PAUSE,null);
            for (AnimatedObject object:objects){
                object.handle(actions,SpaceGame.GAME_RESUME);
            }
            pool.resumeMissile();
        }
    }

    class EndedGame implements State{

        @Override
        public void notifyState(AnimatedObjectBox animatedObjects,MissilePool pool) {
            Collection<AnimatedObject> objects=animatedObjects.values();
            AnimatedObject.Actions actions=new AnimatedObject.Actions();
            actions.put(SpaceGame.GAME_PAUSE,null);
            for (AnimatedObject object:objects){
                object.handle(actions,SpaceGame.GAME_PAUSE);
            }
            pool.pauseMissiles();
            gameover();
            laserBase.setVisibility(View.INVISIBLE);
        }
    }

}
