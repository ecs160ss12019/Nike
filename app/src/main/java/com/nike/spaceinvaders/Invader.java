package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.os.Handler;


public class Invader extends AnimatedObject <ImageView> {
    private boolean status=true;
    private Missile missile;
    private int[][] hitbox;
    private int abstractionLevel=10;

    Invader(ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(animator, view, resources, spaceGame, status,mainHandler, processHandler);

    }

    private void kill(AnimatedObject missile){
        this.status=false;
        this.setVisibility(View.INVISIBLE);
        Actions actions=new Actions();
        actions.put(SpaceGame.MISSILE_GONE,null);
        missile.handle(actions);
        if (getSpaceGame().invaderGroup==null){
            Log.d("aasfdfa","asfawe");
            return;
        }
        ((InvaderGroup)(getSpaceGame().invaderGroup)).setDetection(false);
        notifySpaceGame();
    }

    private void notifySpaceGame(){

    }

    public boolean diagnose(){
        return this.status;
    }



    private boolean hitDetection(AnimatedObject missile){
        if (!status){
            return false;
        }
        float x=missile.getX();
        float y=missile.getY();
        int missileWidth=missile.getWidth();
        float left,top,bottom,right;
        left=this.getAbsoluteX();
        top=this.getAbsoluteY();
        bottom=top+this.getHeight();
        right=left+this.getWidth();
        if ((x>=left&&x<=right&&y<=bottom)||(x+missileWidth)>=left&&(x+missileWidth)<=right&&y<=bottom){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void handle(Actions actions, Set<Integer> keys) {
        for (Integer key: keys){
            Pair<AnimatedObject, SparseArray<Float>> value=actions.get(key);

            switch (key){
                case SpaceGame.STRIKE:
                    assert value != null;
                    if (hitDetection(value.first)){
                        kill(value.first);
                    }
            }
        }
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
