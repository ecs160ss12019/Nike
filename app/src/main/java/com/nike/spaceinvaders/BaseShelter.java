package com.nike.spaceinvaders;

/*
- Has an array of ShelterBlocks that make up a baseShelter
- Detect the collision with missiles
- Spawns at between invader and laserbase
 */


import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;


/**
 * This class is a manager class for BaseShelter.
 * It holds and manages an {@link ImageView} object that holds the image of the BaseShelter.
 */
class BaseShelter extends AnimatedObject<ImageView> {
    private boolean status=true;
    int[] oldHitBox;
    // number of cols and rows of hitBoxes
    private int numRow, numCol;
    // Each hit box is 10 by 10 in pixels
    private int boxSize = 10;

    private boolean alive = true;

    private Bitmap bitmap;

    private Canvas canvas;

    private HitDetection hitDetection;

    BaseShelter(ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame,
                SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(null, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);

        // initialize hitBox

        numRow = this.getHeight() / boxSize;
        numCol = this.getWidth() / boxSize;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        this.bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), conf); // this creates a MUTABLE bitmap

        this.canvas = new Canvas(this.bitmap);
        this.oldHitBox = new int[this.getHeight() * this.getWidth()];
        Drawable shelter = this.getDrawable();
        shelter.setBounds(shelter.copyBounds());
        shelter.setBounds(0, 0, this.getWidth(), this.getHeight());
        // shelter.setAlpha(255);
        shelter.draw(this.canvas);
        this.bitmap.getPixels(this.oldHitBox, 0, this.getWidth(), 0, 0, this.getWidth(), this.getHeight());
        removePaddingHitBox();
    }



    private void removePaddingHitBox() {
        int newHeight = bitmap.getHeight() - 30;
        int newWidth = bitmap.getWidth();
        this.setHitBox(new int[newHeight * newWidth]);
        for (int i = 0; i < newHeight * newWidth; i++) {
            this.getHitBox()[i] = oldHitBox[i];
        }

    }


    private void killSelf(){
        alive = false;
        this.setVisibility(View.INVISIBLE);
        this.status=false;
    }


    /*
    Only need to handle the strike case
     */
    @Override
    protected void handle(Actions actions, Integer key) {
        switch(key)
        {
            case SpaceGame.CONTACT:
                if(alive && hitDetection(actions.get(SpaceGame.CONTACT).first)){
                    killSelf();
                }
                break;

            case SpaceGame.STRIKE:
                if (!this.status){
                    break;
                }
                //    SparseArray<Float> data = Objects.requireNonNull(actions.get(SpaceGame.STRIKE)).second;

                Missile missile = (Missile) Objects.requireNonNull(actions.get(SpaceGame.STRIKE)).first;
                boolean missileUp = missile.getDirectionUp();

                float missileAbsX = missile.getX();
                float missileAbsY = missile.getY();

                // change the absolute missile coordinates to coordinates relative to shelter
                float missileRelX = missileAbsX - this.getAbsoluteX();
                float missileRelY = missileAbsY - this.getAbsoluteY();


                // hit detection
                PointF boxXY = new PointF(missileRelX, missileRelY);
                Point hitPoint = hitDetection(boxXY, new Size(0, this.boxSize));


                if (hitPoint != null) {
                    // draw the hitting effect using bitmap
                    drawDamage(hitPoint.x, hitPoint.y, missileUp);

                    // notify the missile to be gone
                    Actions missileGone = new Actions();
                    missileGone.put(SpaceGame.MISSILE_GONE, new
                            Pair<AnimatedObject, SparseArray<Float>>(this, null));

                    missile.handle(missileGone, SpaceGame.MISSILE_GONE);
                }
                break;


        }
    }


    private void drawDamage(float x, float y, boolean missileUp) {

        Resources resources = (Resources) this.getResources().get(SpaceGame.RESOURCES);
        assert resources != null;
        Drawable damage = resources.getDrawable(R.drawable.explode, null);

        float padding = 30;
        if(missileUp)
        {
            // LaserBase is shooting
            x = x - padding;
            y = y - padding;
        }
        else
        {
            // Invaders are shooting
            // No padding needed
        }


        damage.setBounds((int) x, (int) y, 70 + (int) x, 70 + (int) y);
        // damage.setAlpha(255);
        damage.draw(this.canvas);
        this.setBitmap(this.bitmap);
        this.bitmap.getPixels(this.oldHitBox, 0, this.getWidth(), 0, 0, this.getWidth(), this.getHeight());
        removePaddingHitBox();

        //        for (int index=0;index<10316;index++){
//            pixels[index]=0;
//        }
//        this.bitmap.setPixels(pixels,0,this.getWidth(),0,0,this.getWidth(),this.getHeight());
        return;
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }


    /*
    Return the hitting point location if it hits
    Otherwise return null
     */
    private Point hitDetection(PointF position, Size size) {

        int width = this.getWidth();
        int height = this.getHeight();
        int minX = (int) Math.floor(position.x);
        int maxX = (int) Math.ceil(position.x + size.getWidth());
        int minY = (int) Math.floor(position.y);
        int maxY = (int) Math.ceil(position.y + size.getHeight());
        float slope = (float) (maxY - minY) / (float) (maxX - minX);

        for (int x = minX; x <= maxX; x++) {
            int y = (int) ((float) (x - minX) * slope + minY);
            int realCoordinate = this.getWidth() * (y) + x;


            if (x >= 0 && y >= 0 && x < width && y < height && realCoordinate >= 0
                    && realCoordinate < this.getHitBox().length && this.getHitBox()[realCoordinate] !=
                    Color.argb(255, 0, 0, 0)) {
                return new Point(x, y);
            }
        }
        return null;
    }

    @Override
    public void updateStatus(SpaceGame.Status status) {

    }

    public void setHitDetection(HitDetection hitDetection) {
        this.hitDetection = hitDetection;
    }
    public boolean isAlive() {return alive;}
}
