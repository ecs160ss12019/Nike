// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class SpaceGame extends SurfaceView implements Runnable {
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
    private Invader mInvader;
    private Missile mMissile;
    private BaseShelter mBaseShelter;

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

    }


    @Override
    public void run(){

    }




    // Called by SpaceActivity when
    // the player quits the game
    public void pause(){

    }

    // Called by SpaceActivity when
    // the player begins the game
    public void resume(){

    }
}
