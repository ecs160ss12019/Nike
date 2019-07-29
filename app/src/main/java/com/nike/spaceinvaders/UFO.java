package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;
import java.util.concurrent.TimeoutException;

import android.os.Handler;


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
        appear = 20;//really frequent
        duration = 2000;
        remainedFrames = 50;//kinda slow
        direction = myrand.nextBoolean();
    }

    @Override
    protected void handle(Actions actions, Integer key) {

        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);

        switch (key) {
            case SpaceGame.GAME_START:
                if (this.getAnimator() == null) {
                    this.setAnimator(new ValueAnimator());
                    this.getAnimator().setIntValues(1, 100);
                    this.getAnimator().setDuration(this.duration);
//                    this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
//                    this.getAnimator().setRepeatMode(ValueAnimator.RESTART);
                    this.getAnimator().setInterpolator(null);
                    this.getAnimator().addUpdateListener(animatorListenerConfigure());
                    this.getAnimator().start();
                    this.setX(-150);
                }

                break;
            case SpaceGame.STRIKE:
                assert value != null;
                if (hitDetection(actions, value.first)) {
                    kill(actions, value.first);
                }
                break;
            case SpaceGame.GAME_PAUSE:
                if (this.getAnimator()!=null&&this.getAnimator().isStarted()){
                    this.getAnimator().pause();
                }
                break;
            case SpaceGame.GAME_RESUME:
                if (this.getAnimator()!=null&&this.getAnimator().isStarted()){
                    this.getAnimator().resume();
                }
                break;
        }

    }



    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {

        final AnimatedObject that = this;
        return new ValueAnimator.AnimatorUpdateListener() {
            private int times;
            private int expectation;
            private float subFraction;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (expectation==0){
                    expectation=myrand.nextInt(appear);
                    Log.d("END-1", String.valueOf(expectation));

                }
                float fraction = animation.getAnimatedFraction();
                if (fraction==1f){


                    that.getMainHandler().post(() -> {
                        animation.setIntValues(1, 100);
                        animation.setDuration(duration);
                        animation.start();
                        Log.d("END-2", String.valueOf(times));
                        times++;
                    });

                }
                if (times==expectation){
                    direction = myrand.nextBoolean();
                    Point size = (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                    assert size != null;
                    int lengthX = size.x;
                    Log.d("fraction", String.valueOf(fraction));
                    if (direction){
                        that.setX(lengthX*fraction);
                    }else {
                        that.setX(lengthX*(1-fraction));
                    }
                    times=0;
                    expectation=0;
                }
            }
        };
    }
}
