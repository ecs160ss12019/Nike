package com.nike.spaceinvaders;

import android.app.Activity;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SpaceActivity extends Activity {
    private SpaceGame mSpaceGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get windows size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpaceGame = new SpaceGame(this,size.x,size.y);

        setContentView(mSpaceGame);
    }

/*
    @Override
    protected void onResume(){
        super.onResume();
        mSpaceGame.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSpaceGame.pause();
    }*/
}
