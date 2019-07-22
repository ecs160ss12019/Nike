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
    boolean[][] hitBox;
    // number of cols and rows of hitBoxes
    private int numRow, numCol;
    // Each hit box is 10 by 10 in pixels
    private int boxSize = 10;

    BaseShelter(ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame,
                SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(null, view, resources, spaceGame,status, mainHandler, processHandler);

        // initialize hitBox
        // Each hit box is 10 by 10 in pixels
        numRow = this.getHeight() / boxSize;
        numCol = this.getWidth() / boxSize;
        hitBox = new boolean[numRow][numCol];

    }


    /*
    Only need to handle the strike case
     */
    @Override
    protected void handle(Actions actions, Set keys) {
        ArrayList<Float> data = Objects.requireNonNull(actions.get(SpaceGame.STRIKE)).second;
        float missileX = data.get(0);
        float missileY = data.get(1);

        // change the absolute missile coordinates to coordinates relative to shelter
        // hit detection

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }

    /*
    Get the coordinate in terms of box
    For example, if a box has relative coordinates of (18, 22), then
    it is in the second column, third row, and thus should have box
    coordinates of (1.8, 2.2) if the box size is 10
     */
    private PointF getBoxCoordinate(float x,float y){
        float newX = x / this.boxSize;
        float newY = y / this.boxSize;
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
