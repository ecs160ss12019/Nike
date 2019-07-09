// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;


/*
    Invaders and laserBase will use missile to attack
    each other.
    Missile moves straight up/down once attacker releases
    it.
 */
class Missile {
    private RectF mRect;

    // How fast the missile move vertically
    private float mYVelocity;

    private float mMissileWidth;
    private float mMissileHeight;

    // Missile's image
    private Bitmap mBitmapMissile;


    // Missile constructor
    Missile(Context context, float screenX, float screenY){
        // the width of missile will be 1 percent of the screen width
        mMissileWidth = screenX / 100;
        // the height of missile will be 1/25 of the screen height
        mMissileHeight = screenY / 25;

        // For now, initialize the RectF of Missile with 0, 0, 0, 0
        // The initial mRect needs to get location from invaders/laserbase
        mRect = new RectF();

        // Load the image of missile to the bitmap
        // .....
    }


    RectF getmRect(){
        return mRect;
    }


    // Update the missile position for each frame
    void update(long fps){
        // Missile from laserBase will move up
        // Missile from invaders will move down
        // with velocity defined in mYVelocity

        mRect.top += mYVelocity / fps;
        mRect.bottom = mRect.top - mMissileHeight;
    }
}