// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import java.util.HashMap;
import java.util.Set;

class SpaceGame  {
    //Action Flags
    public static final int GAMESTART=0b0000001;
    public static final int STRIKE=0b0000010;
    public static final int TOUCH=0b0000100;
    public static final int MISSILE_GONE=0b0001000;
    public static final int MOVE_LEFT=0b0010000;
    public static final int MOVE_RIGHT=0b0100000;
    public static final int LIFE_ADD=0b1000000;
    public static final int LIFE_GONE=0b10000000;
    public static final int RESURRECTION=0b100000000;
    public static final int FIRE=0b1000000000;
    //Resource Flags
    public static final int WINDOW_SIZE=0b0000001;
    public static final int RESOURCES=0b0000010;
    //Status Flags
    public static final int NUM_INVADER=0b0000001;
    public static final int NUM_LIVES=0b0000010;
    public static final int INTEGRITY_OF_SHELTER=0b0000100;
    public static final int SCORES=0b0001000;
    public static final int LEVEL=0b0010000;
    public static final int PERKS_OF_LASERBASE=0b0100000;

    final LaserBase laserBase;
    final BaseShelterGroup baseShelterGroup;
    final InvaderGroup invaderGroup;
    final   Missile missile=null;
    final HUD hud;
    final HashMap<Integer, Object> resources;

    private Status status;

    public SpaceGame (ImageView laserBaseView, ConstraintLayout baseShelterGroupView, ConstraintLayout invaderGroupView, ImageView missileView,ConstraintLayout hudView, HashMap<Integer, Object> resources, Handler mainHandler, Handler processThread){
        this.laserBase=new LaserBase(laserBaseView,resources,this,status,mainHandler,processThread);
        this.baseShelterGroup=new BaseShelterGroup(baseShelterGroupView,resources,this,status,mainHandler,processThread);
        this.invaderGroup=new InvaderGroup(invaderGroupView,resources,this,status,mainHandler,processThread);
//        this.missile=new Missile(missileView,resources,this,mainHandler,processThread);
        this.hud=new HUD(hudView,resources,this,status,mainHandler,processThread);
        this.resources=resources;
        AnimatedObject.Actions actions=new AnimatedObject.Actions();
        actions.put(GAMESTART,new Pair<AnimatedObject, ArrayList<Float>>(null,null));
        invaderGroup.handle(actions);

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

    static class Status extends  HashMap<Integer, Pair<Float,Float>>{

    }

}
