package com.nike.spaceinvaders;

import android.app.Activity;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpaceGame = new SpaceGame(this);

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
