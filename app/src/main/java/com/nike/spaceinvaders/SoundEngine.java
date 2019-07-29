package com.nike.spaceinvaders;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

class SoundEngine {
    // for playing sound effects
    private SoundPool mSP;
    private int mShoot_ID = -1;
    private int mShoot2_ID = -1;
    private int mShoot3_ID = -1;
    private int mShoot4_ID = -1;
    private int mInvaderDeath_ID = -1;
    private int mInvaderDeath2_ID = -1;
    private int mInvaderDeath3_ID = -1;
    private  int mlaserBaseDeath_ID = -1;
    private  int mMenuSelect_ID = -1;
    private  int mBackgroundMusic_ID = -1;


    private static SoundEngine ourInstance;

    public static SoundEngine getInstance(Context context) {
        if (ourInstance==null){
            ourInstance = new SoundEngine(context);
        }
        return ourInstance;
    }

    private SoundEngine(Context c) {
        // Initialize the SoundPool
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSP = new SoundPool.Builder()
                .setMaxStreams(11)
                .setAudioAttributes(audioAttributes)
                .build();
        try {
            AssetManager assetManager = c.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("shoot1.wav");
            mShoot_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("shoot2.wav");
            mShoot2_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("shoot3.wav");
            mShoot3_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("shoot4.wav");
            mShoot4_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderDeath.wav");
            mInvaderDeath_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderDeath2.wav");
            mInvaderDeath2_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderDeath3.wav");
            mInvaderDeath3_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("laserBaseDeath.wav");
            mlaserBaseDeath_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("menuSelect.wav");
            mMenuSelect_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("Venus.wav");
            mBackgroundMusic_ID = mSP.load(descriptor, 0);

            playBackGroundMusic();

        } catch (IOException e) {
            // Error
        }

    }

    public void playLaserBaseMissile(){
        mSP.play(mShoot_ID,1, 1, 0, 0, 1);
    }

    public void playInvaderMissile1(){
        mSP.play(mShoot2_ID,1, 1, 0, 0, 1);
    }

    public void playInvaderMissile2(){
        mSP.play(mShoot3_ID,1, 1, 0, 0, 1);
    }

    public void playInvaderMissile3(){
        mSP.play(mShoot4_ID,1, 1, 0, 0, 1);
    }

    public void playInvaderDeath(){
        mSP.play(mInvaderDeath_ID,1, 1, 0, 0, 1);
    }

    public void playInvaderDeath2(){
        mSP.play(mInvaderDeath2_ID,1, 1, 0, 0, 1);
    }

    public void playInvaderDeath3(){
        mSP.play(mInvaderDeath3_ID,1, 1, 0, 0, 1);
    }

    public void playlaserBaseDeath(){
        mSP.play(mlaserBaseDeath_ID,1, 1, 0, 0, 1);
    }

    public void playMenuSelect() {
        mSP.play(mMenuSelect_ID, 1, 1, 0, 0, 1);
    }

    public void playBackGroundMusic() {
        mSP.play(mBackgroundMusic_ID, 1, 1, 0, 0, 1);
    }



}