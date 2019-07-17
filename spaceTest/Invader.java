package com.nike.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;



class Invader {
    private RectF mRect; // used for collision detection

    // the top/left corner of the object, used for bitmap
    private PointFloat point;

    private int height;
    private int width;

    private enum Moving {
        LEFT, RIGHT
    }

    private Moving moving = Moving.RIGHT;
    private float velocity;

    boolean isAlive = false;

    private Bitmap bitmap;



    public Invader(Context context, int row, int col, int screenX, int screenY){
        isAlive = true;
        velocity = 0.1f;

        mRect = new RectF();

        width = screenX / 15;
        height = screenY / 15;

        point = new PointFloat();

        // distance between consecutive invaders
        int interval = screenX / 40;
        point.x = col * (width + interval * 2);
        point.y = row * (width + interval);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.myinvader);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

    }


    public void update(long fps){
        switch(moving){
            case LEFT:
                point.x = point.x - velocity * fps;
                break;
            case RIGHT:
                point.x = point.x + velocity * fps;
                break;
        }

        mRect.left = point.x;
        mRect.right = mRect.left + width;
        mRect.top = point.y;
        mRect.bottom = mRect.top + height;
    }


    public void changeDirection(){
        switch(moving){
            case LEFT:
                moving = Moving.RIGHT;
                break;
            case RIGHT:
                moving = Moving.LEFT;
                break;
        }

        point.y = point.y + height * 2;
        //update()
        //velocity = velocity * (float)1.2;
    }



    public Bitmap getBitmap(){
        return bitmap;
    }



    public PointFloat getPoint(){
        return point;
    }


    public int getWidth(){
        return width;
    }


    public RectF getRect(){
        return mRect;
    }

}