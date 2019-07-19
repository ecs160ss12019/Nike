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
    public static final int GAMESTART=0b000001;
    public static final int STRIKE=0b000010;
    public static final int TOUCH=0b000100;

    private LaserBase laserBase;
    private BaseShelterGroup baseShelterGroup;
    private InvaderGroup invaderGroup;
    private  Missile missile;
    private HUD hud;
    private HashMap<String, Object> resources;

    public SpaceGame (ImageView laserBaseView, ConstraintLayout baseShelterGroupView, ConstraintLayout invaderGroupView, ImageView missileView,ConstraintLayout hudView, HashMap<String, Object> resources, Handler mainHandler, Handler processThread){
        this.laserBase=new LaserBase(null,laserBaseView,resources,this,mainHandler,processThread);
        this.baseShelterGroup=new BaseShelterGroup(null,baseShelterGroupView,resources,this,mainHandler,processThread);
        this.invaderGroup=new InvaderGroup(invaderGroupView,resources,this,mainHandler,processThread);
//        this.missile=new Missile(missileView,resources,this,mainHandler,processThread);
        this.hud=new HUD(hudView,resources,this,mainHandler,processThread);
        HashMap<String, Pair<AnimatedObject, ArrayList<Float>>> actionSet=new HashMap<>();
        actionSet.put("start",new Pair<AnimatedObject, ArrayList<Float>>(null,null));
        AnimatedObject.Actions actions=new AnimatedObject.Actions(new PointF(0,0),actionSet);
        invaderGroup.handle(actions);
    }

    public void updateStatus(Status status){
        Set<String> keys=status.keySet();
        for (String key:keys){
            switch (key){
                case "touch":
                    break;
                case "":
            }
        }
    }

    static class Status extends  HashMap<String, Pair<Float,Float>>{

    }

}
