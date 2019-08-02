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
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
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
    int[] oldHitBox;
    // number of cols and rows of hitBoxes
    private int numRow, numCol;
    // Each hit box is 10 by 10 in pixels
    private int boxSize = 10;


    private Bitmap bitmap;

    private Canvas canvas;

    public static final int HIT_DETECTION_NORMAL =0b1;
    public static final int HIT_DETECTION_PRECISE =0b10;

    BaseShelter(ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame,
                SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(null, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);

        // initialize hitBox

        numRow = this.getHeight() / boxSize;
        numCol = this.getWidth() / boxSize;

        initialize();
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
        this.setAlive(false);
        this.setBackgroundResource(R.drawable.shelter_death);
        AnimationDrawable frameAnimation =  (AnimationDrawable) getBackground();
        if(frameAnimation!=null){
//            //Log.d("in baseshelter kill self","should be playing animation");
            frameAnimation.start();
        }

//        Resources resources = (Resources) this.getResources().get(SpaceGame.RESOURCES);
//        assert resources != null;
//        Drawable shelterDeath = resources.getDrawable(R.drawable.shelter_death, null);
//        this.setDrawable(shelterDeath);
//        this.getMainHandler().postDelayed(()->{
//            this.setVisibility(View.INVISIBLE);
//        },600);
    }


    @Override
    protected void initialize() {
        this.setAlive(true);
        this.setVisibility(View.VISIBLE);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        this.bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), conf); // this creates a MUTABLE bitmap

        this.canvas = new Canvas(this.bitmap);
        this.oldHitBox = new int[this.getHeight() * this.getWidth()];
        Resources resources= (Resources) this.getResources().get(SpaceGame.RESOURCES);
        Drawable shelter = resources.getDrawable(R.drawable.shelter);
        shelter.setBounds(shelter.copyBounds());
        shelter.setBounds(0, 0, this.getWidth(), this.getHeight());
        // shelter.setAlpha(255);
        shelter.draw(this.canvas);
        this.bitmap.getPixels(this.oldHitBox, 0, this.getWidth(), 0, 0, this.getWidth(), this.getHeight());
        Drawable newpic = new BitmapDrawable(this.bitmap);
        this.setBackground(newpic);
        removePaddingHitBox();
    }

    /*
        Only need to handle the strike case
         */
    @Override
    protected void handle(Actions actions, Integer key) {
        switch(key)
        {
            case SpaceGame.CONTACT:
                if(this.isAlive() && hitDetection(actions.get(SpaceGame.CONTACT).first)){
                    killSelf();
                }
                break;

            case SpaceGame.STRIKE:
                if (!this.isAlive()){
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

        float xPadding = 30;
        float yPadding = 20;
        if(missileUp)
        {
            // LaserBase is shooting
            x = x - xPadding;
            y = y - yPadding;
        }
        else
        {
            // Invaders are shooting
            // No padding needed
        }


        damage.setBounds((int) x, (int) y, 70 + (int) x, 70 + (int) y);
        // damage.setAlpha(255);
        damage.draw(this.canvas);

        Drawable newpic = new BitmapDrawable(this.bitmap);
        this.setBackground(newpic);

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


    @Override
    protected boolean hitDetection(AnimatedObject hittingObject) {
        float x = hittingObject.getAbsoluteX();
        float y = hittingObject.getAbsoluteY();

        int hittingObjectWidth = hittingObject.getWidth();
        float left, top, bottom, right;
        float padding = 20;
        left = this.getAbsoluteX() + padding;
        top = this.getAbsoluteY();
        bottom = top + this.getHeight();
        right = left + this.getWidth() - padding;
        if ((x+hittingObjectWidth >= left && x <= right &&  y+hittingObject.getHeight()+80 >= top) || ((x + hittingObjectWidth) >= left && (x + hittingObjectWidth) <= right && y <= bottom && y >= top)) {
            return true;
        } else {
            return false;
        }
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

}
