package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Set;

class BaseShelter extends AnimatedObject<ImageView> {
    boolean[][] hitBox=new boolean[2][3];
    private int abstractionLevel=5;

    BaseShelter(PointF position, Size size, ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(animator, view, resources, spaceGame,status, mainHandler, processHandler);
    }

    @Override
    protected void handle(Actions actions, Set keys) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }

    private PointF getCoordinate(float x,float y){
        float newX=x/this.abstractionLevel;
        float newY=y/this.abstractionLevel;
        return new PointF(newX,newY);
    }
}
