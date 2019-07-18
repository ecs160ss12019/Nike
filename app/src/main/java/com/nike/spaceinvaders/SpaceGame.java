// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class SpaceGame extends SurfaceView implements SurfaceHolder.Callback{

    //TODO:Test only
    private MainThread mthread;
    private HUD hud; //display info


    // The following three objects are for drawing and display
    private SurfaceHolder mMyHolder;
    private Canvas mCanvas;
    private Paint mPaint;

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
    private Invader[] mInvaders;
    // The number of invaders in this game
    private int numInvaders = 55;
    private Missile mMissile;
    private BaseShelter mBaseShelter;

    // How many lives remaining for laserbase
    private int mLaserLives;
    // How high the score the player has got
    private int mScore;

    // Our game thread and relevant variables
 //   private Thread mGameThread = null;
    private volatile boolean mPlaying;
    private boolean mPaused = true;


    // in debugging mode or not
    private final boolean DEBUGGING = false;


    public SpaceGame(Context context,int x, int y){
        super(context);
        mScreenX=x;
        mScreenY=y;
        getHolder().addCallback(this);
        mthread = new MainThread(getHolder(),this);

        hud = new HUD(x,y);
        setFocusable(true);
    }


    // Android's game loop
    // Continuously called by Android after mGameThread.start()
    /*@Override
    public void run(){
        while(mPlaying) {

            long frameStartTime = System.currentTimeMillis();

            if(!mPaused){
                // update all the game objects if not paused
                update();
            }

            // draw all the game objects and scores
            draw(mCanvas);

            // calculate how much time this frame takes
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            // if timeThisFrame is longer than 1 millisecond
            if(timeThisFrame >= 1){
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }*/


    // Update all the game objects
    public void update(){
        hud.update();
        // mMissile.update();
        // mBaseShelter.update();
        // update all the invaders that are still alive
        for(int i = 0; i < numInvaders; i++)
        {
            // if this invader alive
            //mInvaders[i].update();
        }
        // mLaserBase.update();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){

            // player's hand touches the screen
            case MotionEvent.ACTION_DOWN:
                // get the y-coordinate of where player touches
                // check if it is beneath the top of laserbase
                // if so, the player is not shooting and only moving
                // the laserbase. Then check whether x-coordinate to see
                // whether player touches the left or right part of screen
                // else,(the player touches above laserbase) the player is shooting

            case MotionEvent.ACTION_MOVE:


            // player has lifted his fingers from the screen
            case MotionEvent.ACTION_UP:
                // stop the movement of laserbase


        }
        return true;
        //return super.onTouchEvent(motionEvent);
    }

    // Draw all the game objects and scores
    public void draw(Canvas canvas){
        super.draw(canvas);

        //Usage of draw drawable file
        Drawable d = getResources().getDrawable(R.drawable.space_background300x300, null);
        d.setBounds(0 , 0, mScreenX, mScreenY);
        d.draw(canvas);
        hud.draw(canvas);


    }

    // Called by SpaceActivity when TODO:Modified pasue and resume
    // the player quits the game
    public void pause(){
        mPlaying = false;
        try{
            // stop the running game thread
            mthread.join();
        }catch (InterruptedException e){
            Log.e("Error:", "joining thread");
        }
    }


    // Called by SpaceActivity when
    // the player begins the game
    public void resume(){
        mPlaying = true;
        mthread = new MainThread(getHolder(),this);
        mthread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mthread=new MainThread(getHolder(),this);
        mthread.setRunning(true);
        mthread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = false;
        while(true){
            try {
                mthread.setRunning(false);
                mthread.join();
            }catch(Exception e){ e.printStackTrace(); }
            retry = false;
        }
    }
}
