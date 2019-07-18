package com.nike.spaceinvaders;

import android.graphics.RectF;

public class ShelterBlock {
    private RectF mRectF;   //Represents block
    private int health;       //Current Health of block, starts at 4
    private boolean alive;    //Is block still alive
    private float blockWidth;
    private float blockHeight;

    ShelterBlock(int screenX, int screenY, int xCord, int yCord) {
        mRectF = new RectF();
        health = 4;
        alive = true;
        blockHeight = screenX/20;   //FIXME play with these values
        blockWidth = screenX/20;
        mRectF.top = yCord;
        mRectF.left = xCord;
        mRectF.bottom = mRectF.top + blockHeight;
        mRectF.right = mRectF.left + blockWidth;
    }

    //GETTERS
    public RectF getRect() {
        return mRectF;
    }
    public int getHealth() {
        return health;
    }
    public boolean isAlive() {
        return alive;
    }
    public float getBlockHeight() {
        return blockHeight;
    }
    public float getBlockWidth() {
        return blockWidth;
    }


    //Reduces health when block is hit
    public void reduceHealth(){
        health--;
    }

    //Destroys block so it will no longer be active in game
    public void destroyBlock(){
        alive = false;
    }

    //Resets block for a new game
    public void reset(int x, int y){
        alive = true;
        health = 4;
    }

    /* Done by Constructor now
    //Sets up the RectF of
    public void update(int x, int y){
        mRectF.left = x;
        mRectF.top = y;
        mRectF.right = x + blockWidth;
        mRectF.bottom = y + blockHeight;
    }*/


}