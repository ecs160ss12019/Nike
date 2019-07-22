package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.util.Pair;
import android.util.SparseArray;
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

    private void kill(Pair<AnimatedObject, SparseArray<Float>> strike){
        Missile missile= (Missile) strike.first;
        PointF position= new PointF(missile.getX(),missile.getY());
        Size size=new Size(missile.getHeight(),missile.getWidth());

        float groupX=strike.second.get(0);
        float groupY=strike.second.get(1);
        float realX=groupX+this.getX();
        float realY=groupY+this.getY();

        if (position.x>=realX&&position.x<=realX+this.getWidth()
                &&position.y>=realY&&position.y<=realY+this.getHeight()){
            this.status=false;
            this.setAlpha(0);
        }
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
                    kill(value);
            }
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
