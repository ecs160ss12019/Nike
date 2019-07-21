package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

class BaseShelter extends AnimatedObject<ImageView> {
    boolean[][] hitBox=new boolean[2][3];
    private int abstractionLevel=5;

    BaseShelter(ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame,
                SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(null, view, resources, spaceGame,status, mainHandler, processHandler);
    }


    /*
    Only need to handle the strike case
     */
    @Override
    protected void handle(Actions actions, Set keys) {
        ArrayList<Float> data = Objects.requireNonNull(actions.get(SpaceGame.STRIKE)).second;
        float missileX = data.get(0);
        float missileY = data.get(1);

        // hit detection

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


    /*
    Return the hitting point location if it hits
    Otherwise return null
     */
    private boolean hitDetection(PointF position,Size size){
        int minX= (int) Math.floor(position.x);
        int maxX= (int) Math.ceil(position.x+size.getWidth());
        int minY= (int) Math.floor(position.y);
        int maxY= (int) Math.ceil(position.y+size.getHeight());
        return hitBox[minX][minY] || hitBox[maxX][maxY];
    }
}
