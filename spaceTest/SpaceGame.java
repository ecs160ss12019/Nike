// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class SpaceGame extends SurfaceView implements Runnable {
    // The following three objects are for drawing and display
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    Context mContext;

    // The following two give the resolution of the screen
    private int mScreenX;
    private int mScreenY;
    // Size and Margin of the font
    // for displaying scores
    private int mFontSize;
    private int mFontMargin;

    // Frames per second
    // Use this to make sure objects move as they should
    private long mFPS;
    // How many milliseconds in a second
    private final int MILLIS_IN_SECOND = 1000;

    // Objects in our game
    private LaserBase mLaserBase;
    // The number of invaders in this game
    private int numInvaders = 0;
    private Invader[] mInvaders;
    //private Missile mMissile;
    private final int numBaseShelters = 4;
    // private BaseShelter[] mBaseShelters;

    // How many lives remaining for laserbase
    private int mLaserLives;
    // How high the score the player has got
    private int mScore;

    // Our game thread and relevant variables
    private Thread mGameThread = null;
    private volatile boolean mPlaying;
    private boolean mPaused = true;


    // in debugging mode or not
    private final boolean DEBUGGING = false;


    public SpaceGame(Context context, int x, int y){
        super(context);

        mContext = context;
        mScreenX = x;
        mScreenY = y;
        // font size is 1/20 or 5% of screen width
        mFontSize = mScreenX / 20;
        // font margin is 1.5% of screen width
        mFontMargin = mScreenX / 75;


        mHolder = getHolder();
        mPaint = new Paint();

        mLaserBase = new LaserBase(context, mScreenX, mScreenY);

        mInvaders = new Invader[55]; // 55 invaders in total
        for(int row = 0; row < 5; row++){
            for(int col = 0; col < 6; col++){
                mInvaders[numInvaders] = new Invader(context, row, col, mScreenX, mScreenY);
                numInvaders++;
            }
        }

//        mBaseShelters = new BaseShelter[numBaseShelters];
//        for(int i = 0; i < numBaseShelters; i++)
//        {
//            mBaseShelters[i] = new BaseShelter(context, mScreenX, mScreenY);
//        }


    }


    // Android's game loop
    // Continuously called by Android after mGameThread.start()
    @Override
    public void run(){
        while(mPlaying) {

            long frameStartTime = System.currentTimeMillis();

            if(!mPaused){
                // update all the game objects if not paused
                update();
            }

            // draw all the game objects and scores
            draw();

            // calculate how much time this frame takes
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            // if timeThisFrame is longer than 1 millisecond
            if(timeThisFrame >= 1){
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }


    // Update all the game objects
    private void update(){
        // mBaseShelter.update();
        // update all the invaders that are still alive

        for(int i = 0; i < numInvaders; i++)
        {
            if(mInvaders[i].isAlive)
                mInvaders[i].update(mFPS);
        }
        checkInvaderBoundaries();
        checkPlayerMissile();
        mLaserBase.update(mFPS);
    }



    public void checkInvaderBoundaries(){
        boolean boundary = false;
        for(int i = 0; i < numInvaders; i++){
            if(mInvaders[i].isAlive){
                if(mInvaders[i].getPoint().x < 0 ||
                mInvaders[i].getPoint().x + mInvaders[i].getWidth() > mScreenX){
                    // out of bound
                    boundary = true;
                }
            }
        }

        // if any invader out of bound
        // move all invaders down
        if(boundary){
            for(int i = 0; i < numInvaders; i++){
                if(mInvaders[i].isAlive)
                    mInvaders[i].changeDirection();
            }
        }
    }


    // check if player's missile hit any invader
    public void checkPlayerMissile(){
        // check if missile hit the top
        if(mLaserBase.missile.getRect().top < 0){
            mLaserBase.missile.exist = false;
        }

        for(int i = 0; i < numInvaders; i++){
            if(mInvaders[i].isAlive && mLaserBase.missile.exist){
                if(RectF.intersects(mInvaders[i].getRect(), mLaserBase.missile.getRect())){
                    mInvaders[i].isAlive = false;
                    mLaserBase.missile.exist = false;
                }
            }
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){

            // player's hand touches the screen
            case MotionEvent.ACTION_DOWN:

                mPaused = false;


                // get the y-coordinate of where player touches
                // check if it is beneath the laserbase
                if(motionEvent.getY() > mLaserBase.getPoint().y){
                    // player is moving the laserbase

                    if(motionEvent.getX() < mScreenX / 2){
                        // on the left side
                        mLaserBase.setMovementState(LaserBase.Moving.LEFT);
                    }
                    else {
                        // on the right side
                        mLaserBase.setMovementState(LaserBase.Moving.RIGHT);
                    }
                }
                else{
                    // player touches above the laserbase
                    // player is shooting

                    mLaserBase.setMovementState(LaserBase.Moving.SHOOT);
                }


                break;


            // player has lifted his fingers from the screen
            case MotionEvent.ACTION_UP:
                // stop the movement of laserBase

                mLaserBase.setMovementState(LaserBase.Moving.STOP);
                break;
        }
        return true;
    }

    // Draw all the game objects and scores
    private void draw(){

        if(mHolder.getSurface().isValid()) {
            mCanvas = mHolder.lockCanvas();
            // set the background color as black
            mCanvas.drawColor(Color.argb(255, 0, 0, 0));
           // mCanvas.drawColor(Color.argb(255, 26, 128, 182));
            // set the paint color as white (objects painted white)
            mPaint.setColor(Color.argb(255, 255, 255, 255));


            // draw the laserBase
            mCanvas.drawBitmap(mLaserBase.getBitmap(), mLaserBase.getPoint().x,
            mLaserBase.getPoint().y, mPaint);


//            // draw the baseShelters
//            for(int i = 0; i < 4; i++)
//            {
//                mCanvas.drawBitmap(mBaseShelters.getBitmap(),
//                        mBaseShelters.getRect().left, mBaseShelters.getRect().right,
//                        mPaint);
//            }

            // draw the invaders if alive
            for(int i = 0; i < numInvaders; i++)
            {
                if(mInvaders[i].isAlive){
                    mCanvas.drawBitmap(mInvaders[i].getBitmap(),
                            mInvaders[i].getPoint().x, mInvaders[i].getPoint().y,
                            mPaint);
                }
            }

            // draw the player's missile
            // if the player's missile is active
            if(mLaserBase.missile.exist){
                mCanvas.drawRect(mLaserBase.missile.getRect(), mPaint);
            }

            // draw the invader's missile in blue
            // change paint's color

            // draw the HUD

            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    // Called by SpaceActivity when
    // the player quits the game
    public void pause(){
        mPlaying = false;
        try{
            // stop the running game thread
            mGameThread.join();
        }catch (InterruptedException e){
            Log.e("Error:", "joining thread");
        }
    }


    // Called by SpaceActivity when
    // the player begins the game
    public void resume(){
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }
}
