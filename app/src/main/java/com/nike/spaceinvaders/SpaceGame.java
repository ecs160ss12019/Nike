// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class SpaceGame  {
    /* Action Flags */
    public static final int GAMESTART=0b0000001;
    // Missile has been released and is moving(striking)
    public static final int STRIKE=0b0000010;
    public static final int TOUCH=0b0000100;
    public static final int MISSILE_GONE=0b0001000;
    public static final int MOVE_LEFT=0b0010000;
    public static final int MOVE_RIGHT=0b0100000;
    public static final int LIFE_ADD=0b1000000;
    public static final int LIFE_GONE=0b10000000;
    public static final int RESURRECTION=0b100000000;
    public static final int MOVE_STOP=0b10000000000;
    //TEST only
    public static final int TEST=0b0100001;
    // The moment at which laserBase or invader fires the missile
    public static final int FIRE=0b1000000000;
    /* Resource Flags */
    public static final int WINDOW_SIZE=0b0000001;
    public static final int RESOURCES=0b0000010;
    public static final int CONTEXT=0b0000100;
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
    final AnimatedObject missile=null;
    final AnimatedObject hud;
    final Resources resources;

    private Status status;

    public SpaceGame (AnimatedObject laserBase, AnimatedObject baseShelterGroup, AnimatedObject invaderGroup, AnimatedObject missile,AnimatedObject hud, Resources resources,Status status, Handler mainHandler, Handler processThread){
        this.laserBase=laserBase;
        this.baseShelterGroup=baseShelterGroup;
        this.invaderGroup=invaderGroup;
        this.hud=hud;

        this.laserBase.setSpaceGame(this);
        this.baseShelterGroup.setSpaceGame(this);
        this.invaderGroup.setSpaceGame(this);
        this.hud.setSpaceGame(this);

        this.resources=resources;
        this.status=status;

        AnimatedObject.Actions actions=new AnimatedObject.Actions();
        actions.put(GAMESTART,new Pair<AnimatedObject, ArrayList<Float>>(null,null));
        invaderGroup.handle(actions);

        AnimatedObject.Actions actions2 = new AnimatedObject.Actions();
        actions2.put(LIFE_ADD,new Pair<AnimatedObject, ArrayList<Float>>(null,null));
        hud.handle(actions2);
    }

    public void updateStatus(Status status){
        Set<Integer> keys=status.keySet();
        for (Integer key:keys){
            switch (key){
                case SpaceGame.NUM_INVADER:
                    break;
                case SpaceGame.NUM_LIVES:
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
            break;
            case MotionEvent.ACTION_UP:
                actions.put(SpaceGame.MOVE_STOP,null);
                this.laserBase.handle(actions);
        }
    }

    static class Status extends  HashMap<Integer, Pair<Float,Float>>{

    }

    static class Resources extends  HashMap<Integer, Object>{

    }

}
