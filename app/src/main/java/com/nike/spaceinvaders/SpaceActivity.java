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

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


//        mSpaceGame = new SpaceGame(this, size.x, size.y);

        setContentView(new SpaceGame( this,size.x,size.y));
    }


    @Override
    protected void onResume(){
        super.onResume();
        mSpaceGame.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSpaceGame.pause();
    }
}
