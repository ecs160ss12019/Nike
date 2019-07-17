package com.nike.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Developer Xuanchen Zhou
 */

/*
 * LaserBase is the only thing that can be directly controlled by the player in the game.
 * There is only going to be one LaserBase instance in the game.
 * The Player can move the LaserBase to the left, to the right, or shoot a missile from the LaserBase.
 * The LaserBase has hp and can be damaged by the projectiles shoot by the invaders.
 */


class LaserBase {
    RectF mRect;//For collision detection

    // the top/left corner of the object, used for bitmap
    private PointFloat point;
    private int height;
    private int width;



    enum Moving{
        LEFT, RIGHT, SHOOT, STOP
    }
    private Moving movementState;

    Missile missile;
    //private int hp;//for game state detection
    private float velocity;
    boolean isAlive;
    private Bitmap bitmap;



    public LaserBase(Context context, int screenX, int screenY) {

        isAlive = true;
        velocity = 200;
        mRect = new RectF();

        point.x = screenX / 2;
        point.y = screenY - 20;

        width = screenX / 10;
        height = screenX / 10;

        movementState = Moving.STOP;

        missile = new Missile(context, screenX, screenY);


        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public void update(long fps){
        switch(movementState){
            case LEFT:
                point.x = point.x - velocity * fps;
                break;
            case RIGHT:
                point.x = point.x + velocity * fps;
                break;
            case STOP:
                break;
            case SHOOT:
                onShoot();
                break;
        }
        mRect.left = point.x;
        mRect.right = point.x + width;
        mRect.top = point.y;
        mRect.bottom = point.y + height;

        if(missile.exist)
            missile.update(fps);
    }

    public PointFloat getPoint(){
        return point;
    }

    public Bitmap getBitmap(){ return bitmap; }

    public RectF getRect(){ return mRect; }



    public void onShoot(){
       missile.spawn(point);
    }


    public void setMovementState(Moving m){
        movementState = m;
    }

}
