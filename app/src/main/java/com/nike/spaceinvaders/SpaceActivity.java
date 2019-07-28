package com.nike.spaceinvaders;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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


    }

    @Override
    protected void onStart() {
        super.onStart();

        final SpaceGame.Resources resources=new SpaceGame.Resources();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        resources.put(SpaceGame.WINDOW_SIZE,size);

        resources.put(SpaceGame.RESOURCES,getResources());

        resources.put(SpaceGame.CONTEXT, this);

        SoundEngine se = SoundEngine.getInstance(this);

        final View laserBase= findViewById(R.id.laserBase);
        final View baseShelterGroup= findViewById(R.id.shelters);
        final View invaderGroup= findViewById(R.id.invader_layout);
        final View ufo = findViewById(R.id.UFO);
        final View hud=findViewById(R.id.HUD);
        final ViewGroup mainLayout=findViewById(R.id.main_layout);
        final SpaceGame.Status status=new SpaceGame.Status();
        status.put(SpaceGame.NUM_LIVES,new Pair<>(3f,0f));
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (laserBase.getY()==0.0){
                    mainHandler.postDelayed(this,0);
                    return;
                }
                mSpaceGame=new SpaceGame( new LaserBase( (ImageView) laserBase,resources,mSpaceGame,status,mainHandler,processHandler,se),new BaseShelterGroup((ConstraintLayout) baseShelterGroup,resources,mSpaceGame,status,mainHandler,processHandler,se),new InvaderGroup((ConstraintLayout) invaderGroup,resources,mSpaceGame,status,mainHandler,processHandler,se),null,new UFO(0,null,(ImageView) ufo,resources,mSpaceGame,status,mainHandler,processHandler,se), new HUD((ConstraintLayout) hud,resources,mSpaceGame,status,mainHandler,processHandler,se),resources,status,mainLayout,mainHandler,processHandler, se);

            }
        },0);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mSpaceGame.onTouch(event);
        return super.onTouchEvent(event);
    }

    //invoked when pressing pause
    public void pause_press(View view){
        //Toast.makeText(getApplicationContext(),"TEST",Toast.LENGTH_SHORT).show();
//        mSpaceGame.pause();
        startActivity(new Intent(SpaceActivity.this,Pop.class));
    }
}
