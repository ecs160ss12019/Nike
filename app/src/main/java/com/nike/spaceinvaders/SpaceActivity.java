package com.nike.spaceinvaders;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import jp.wasabeef.blurry.Blurry;



public class SpaceActivity extends AppCompatActivity implements SensorEventListener{
    private SpaceGame mSpaceGame;
    private Handler mainHandler;
    private Handler processHandler;
    private Thread processThread;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SpaceGame.Status setting;


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

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicPlayer.class);
        startService(music);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
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
        status.put(SpaceGame.SCORES, new Pair<>(0f, 0f));
        //bundle get from startMenu
        Bundle bundle=getIntent().getExtras();

        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (laserBase.getY()==0.0){
                    mainHandler.postDelayed(this,0);
                    return;
                }
                mSpaceGame=new SpaceGame( new LaserBase( (ImageView) laserBase,resources,mSpaceGame,status,mainHandler,processHandler,se),new BaseShelterGroup((ConstraintLayout) baseShelterGroup,resources,mSpaceGame,status,mainHandler,processHandler,se),new InvaderGroup((ConstraintLayout) invaderGroup,resources,mSpaceGame,status,mainHandler,processHandler,se),null,new UFO(0,null,(ImageView) ufo,resources,mSpaceGame,status,mainHandler,processHandler,se), new HUD((ConstraintLayout) hud,resources,mSpaceGame,status,mainHandler,processHandler,se),resources,status,mainLayout,mainHandler,processHandler, se);
                //get Setting
                mSpaceGame.setSetting(bundle);
            }
        },0);
    }

    @Override
    protected void onResume(){
        super.onResume();
//        mSpaceGame.resume();
        if (sensor!=null)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
//        mSpaceGame.pause();
        if (sensor!=null)
            sensorManager.unregisterListener(this);
        if (mServ != null) {
            mServ.pauseMusic();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSpaceGame!=null)
            mSpaceGame.onTouch(event);
        return super.onTouchEvent(event);
    }

    //invoked when pressing pause
    public void pause_press(View view){
        //blur background
        blurEffect();
        //pause the game
        if (mSpaceGame.getState() instanceof SpaceGame.PausedGame){
            //mSpaceGame.setState(new SpaceGame.RunningGame());
        }else {
            mSpaceGame.setState(new SpaceGame.PausedGame());
        }
        //start popup window
        Intent i = new Intent(SpaceActivity.this,Pop.class);
        i.putExtra("insignal","pause");
        startActivityForResult(i,0);
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
    }

    //Cite from https://github.com/wasabeef/Blurry
    private void blurEffect() {
        ViewGroup rootView = findViewById(R.id.main_layout);
        Blurry.with(getApplicationContext()).radius(25).sampling(2).onto(rootView);
    }
    //Cite from https://github.com/wasabeef/Blurry
    private void removeblurEffect(){
        ViewGroup rootView = findViewById(R.id.main_layout);
        Blurry.delete(rootView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        Log.d("Gravity", Arrays.toString(event.values));
        if (mSpaceGame!=null) {
//            Log.d("adsfaw", "awefa");
            mSpaceGame.onSensorChanged(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle=data.getExtras();
        String str = bundle.getString("signal");
        Toast.makeText(getApplicationContext(),bundle.getString("signal"),Toast.LENGTH_SHORT).show();
        removeblurEffect();
        switch (str){
            case "resume":
                if (mSpaceGame.getState() instanceof SpaceGame.PausedGame){
                    mSpaceGame.setState(new SpaceGame.RunningGame());
                }else {
                    mSpaceGame.setState(new SpaceGame.PausedGame());
                }
                break;
            case "restart":
                Toast.makeText(getApplicationContext(),"restartgame",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case "backtotitle":
                //situation when click back button
                Toast.makeText(getApplicationContext(),"backtotitle",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            default:
                break;
        }
    }


    //MusicPlayer code
    private boolean mIsBound = false;
    private MusicPlayer mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicPlayer.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this, MusicPlayer.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    //this will ban back button in bottom
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

}
