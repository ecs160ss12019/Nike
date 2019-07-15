package com.nike.spaceinvaders;

import android.app.Activity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

public class SpaceActivity extends AppCompatActivity {
    private SpaceGame mSpaceGame;
    private Handler mainHandler;
    private Handler processHandler;
    private Thread processThread;

    //Initiate runnable to be run in the process thread that initiate handler.
    private final Runnable threadInitiation= new Runnable() {
        @Override
        public void run() {
            Looper.prepare();

            processHandler=new Handler();

            Looper.loop();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initiate process thread
        this.mainHandler=new Handler();
        this.processThread=new Thread(this.threadInitiation);
        this.processThread.start();

        //Get layout file and inflate it into the screen and get the View object.

        LayoutInflater mInflater = LayoutInflater.from(this);
        View contentView  = mInflater.inflate(R.layout.space_activity,null);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

//        mSpaceGame = new SpaceGame(this, size.x, size.y);
//
        setContentView(contentView);
    }


    @Override
    protected void onResume(){
        super.onResume();
//        mSpaceGame.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
//        mSpaceGame.pause();
    }
}
