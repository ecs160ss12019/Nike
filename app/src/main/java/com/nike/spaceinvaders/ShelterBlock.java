package com.nike.spaceinvaders;

import android.graphics.RectF;

public class ShelterBlock {
    RectF mRectF;   //Represents block
    int health;       //Current Health of block, starts at 4
    boolean alive;    //Is block still alive
    float BlockWidth;
    float BlockHeight;

    ShelterBlock() {
        health = 4;
        alive = true;
    }

    RectF getRect() { //Return reference to mRectF
        return mRectF;
    }

}