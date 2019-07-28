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
    private int mInvaderKilled_ID = -1;
    private  int mSound3_ID = -1;
    private  int mSound4_ID = -1;

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
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
        try {
            AssetManager assetManager = c.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("shoot.wav");
            mShoot_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderkilled.wav");
            mInvaderKilled_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("sound3.ogg");
            mSound3_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("sound4.ogg");
            mSound4_ID = mSP.load(descriptor, 0);

        } catch (IOException e) {
            // Error
        }

    }

    public void playMissile(){
        mSP.play(mShoot_ID,1, 1, 0, 0, 1);
    }

    public void playInvaderKilled(){
        mSP.play(mInvaderKilled_ID,1, 1, 0, 0, 1);
    }

    public void playSound3(){
        mSP.play(mSound3_ID,1, 1, 0, 0, 1);
    }

    public void playSound4() {
        mSP.play(mSound4_ID, 1, 1, 0, 0, 1);
    }

}