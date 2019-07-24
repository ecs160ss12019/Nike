package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
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
        notifySpaceGame();
    }

    private void notifySpaceGame(){

    }

    public boolean diagnose(){
        return this.status;
    }



    @Override
    protected void handle(Actions actions) {
        Set<Integer> keys=actions.keySet();
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

    private boolean hitDetection(AnimatedObject missile){
        float x=missile.getX();
        float y=missile.getY();
        int width=missile.getWidth();
        int height=missile.getHeight();
        float left,top,bottom,right;
        left=this.getX();
        top=this.getY();
        bottom=this.getY()+this.getHeight();
        right=this.getX()+this.getWidth();
        if ((x>=left&&x<=right&&y<=bottom)||(x+missile.getWidth())>=left&&(x+missile.getWidth())<=right&&y<=bottom){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void handle(Actions actions, Set keys) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
