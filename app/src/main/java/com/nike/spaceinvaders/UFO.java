package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import android.os.Handler;
import android.widget.Space;


public class UFO extends Invader {
    private int appear;
    private int duration;
    private Random myrand;
    private boolean decider=false;//should the UFO start flying at this frame?
    private int remainedFrames;//this is the number of frames spent in each horizontal trip, so the speed of UFO is faster when this number is smaller
    private boolean direction;

    UFO(int index, ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(index, animator, view, resources, spaceGame, status, mainHandler, processHandler, soundEngine);
        myrand = new Random();
        appear = 200;
        duration = 200;
        remainedFrames = 50;
        direction = myrand.nextBoolean();
    }

    @Override
    protected void handle(Actions actions, Integer key) {

        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);

        switch (key) {
            case SpaceGame.GAMESTART:
                if (this.getAnimator() == null) {
                    this.setAnimator(new ValueAnimator());
                    this.getAnimator().setIntValues(1, 100);
                    this.getAnimator().setDuration(this.duration);
                    this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
                    this.getAnimator().setRepeatMode(ValueAnimator.RESTART);
                    this.getAnimator().setInterpolator(null);
                    this.getAnimator().addUpdateListener(animatorListenerConfigure());
                    this.getAnimator().start();
                }

                break;
            case SpaceGame.STRIKE:
                assert value != null;
                if (hitDetection(actions, value.first)) {
                    kill(actions, value.first);
                }

        }

    }



    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final AnimatedObject that = this;
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(decider){
                    if(remainedFrames!=0){
                        Point size = (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                        if(direction){
                            setX((float)size.x*((float)remainedFrames/50));
                        }else{
                            setX((float)size.x-size.x*((float)remainedFrames/50));
                        }
                        remainedFrames--;
                    }else{
                        decider =false;
                        remainedFrames=50;
                        direction = myrand.nextBoolean();
                        that.setVisibility(View.INVISIBLE);
                    }
                }else{
                    if(myrand.nextInt(appear)==1){
                        decider=true;
                        that.setVisibility(View.VISIBLE);
                    }
                }
                Log.d("current appear rand is", ""+remainedFrames);
            }
        };
    }
}
