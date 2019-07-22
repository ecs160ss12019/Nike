package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.util.SparseArray;
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

    private Bitmap hitPic;

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
        SparseArray<Float> data = Objects.requireNonNull(actions.get(SpaceGame.STRIKE)).second;
        float missileAbsX = data.get(SpaceGame.X_COORDINATE);
        float missileAbsY = data.get(SpaceGame.Y_COORDINATE);


        // change the absolute missile coordinates to coordinates relative to shelter
        float missileRelX = getRelativeX(missileAbsX);
        float missileRelY = getRelativeY(missileAbsY);

        PointF missileBoxXY = getBoxCoordinate(missileRelX, missileRelY);
        float missileBoxX = missileBoxXY.x;
        float missileBoxY = missileBoxXY.y;

        // check they are within hitbox ranges ( 0 < x < numCol && 0 < y < numRow)
        if(0 < missileBoxX && missileBoxX < numCol && 0 < missileBoxY && missileBoxY < numRow)
        {
            // hit detection
            if(hitDetection(missileBoxXY, new Size(boxSize, boxSize)))
            {
                // draw the hitting effect using bitmap
                // notify the missile handler to be gone
            }
        }
        // else return


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
