package com.nike.spaceinvaders;

import android.app.Activity;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

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
        setContentView(contentView);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ConstraintLayout invaderLayout = findViewById(R.id.invader_layout);

        HashMap<String, Object> resources=new HashMap<>();
        resources.put("WindowSize",size);

        InvaderGroup invaderGroup=new InvaderGroup(invaderLayout,resources,null,mainHandler,processHandler);
        HashMap<String, Pair<AnimatedObject, ArrayList<Float>>> actionSet=new HashMap<>();
        actionSet.put("start",new Pair<AnimatedObject, ArrayList<Float>>(null,null));
        AnimatedObject.Actions actions=new AnimatedObject.Actions(new PointF(0,0),actionSet);
        invaderGroup.handle(actions);
//        mSpaceGame = new SpaceGame(this, size.x, size.y);
//


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
