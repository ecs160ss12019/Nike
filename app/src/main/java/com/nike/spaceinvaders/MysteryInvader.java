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


public class MysteryInvader extends Invader {
    public boolean alive = true;
    private boolean status = true;
    private boolean direction;
    private boolean isAppearing;
    private Random rand;
    private int appearcd;
    private int award;


    MysteryInvader(int index, ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(index, animator, view, resources, spaceGame, status, mainHandler, processHandler);
        appearcd = rand.nextInt(1000);
        award = 100;
        direction = rand.nextBoolean();
        isAppearing = rand.nextBoolean();
    }

    //will probably delete these functions upon testing
    private void kill(Actions actions, AnimatedObject missile) {
        alive = false;
        this.status = false;
        this.setVisibility(View.INVISIBLE);
        actions.put(SpaceGame.MISSILE_GONE, null);
        actions.put(SpaceGame.HIT, new Pair<>(this, null));
        missile.handle(actions, SpaceGame.MISSILE_GONE);
        this.getSpaceGame().invaderGroup.handle(actions, SpaceGame.HIT);
        notifySpaceGame();
    }

    //will probably delete these functions upon testing
    private void notifySpaceGame() {
        SpaceGame.Status status = getStatus();
        Pair<Float, Float> value = status.get(SpaceGame.SCORES);
        assert value != null;
        status.put(SpaceGame.SCORES, new Pair<>(value.first + this.award, null));
        getSpaceGame().updateStatus(status);
    }

    //will probably delete these functions upon testing
    private boolean hitDetection(Actions actions, AnimatedObject missile) {
        if (!status) {
            return false;
        }
        float x = missile.getX();
        float y = missile.getY();
        int missileWidth = missile.getWidth();
        float left, top, bottom, right;
        left = this.getAbsoluteX() + 50;
        top = this.getAbsoluteY();
        bottom = top + this.getHeight();
        right = left + this.getWidth() - 50;
        if ((x >= left && x <= right && y <= bottom && y >= top) || ((x + missileWidth) >= left && (x + missileWidth) <= right && y <= bottom && y >= top)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void handle(Actions actions, Integer key) {
        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);
        switch (key) {
            case SpaceGame.GAMESTART:
                //should be handled at the very beginning of the game
                //shouldn't repeat the animation
                if (this.getAnimator() == null) {
                    this.setAnimator(new ValueAnimator());
                    this.getAnimator().setIntValues(1, 100);
                    this.getAnimator().setDuration(2000);
                    this.getAnimator().setInterpolator(null);
                    this.getAnimator().addUpdateListener(animatorListenerConfigure());
                    //animator will start, but UFO won't show if it's not supposed to
                    this.getAnimator().start();
                }
                break;
            case SpaceGame.STRIKE:
                //handles death
                assert value != null;
                if (hitDetection(actions, value.first)) {
                    kill(actions, value.first);
                }

        }

    }

    //used in onAnimationUpdate to decide if the UFO is going to appear at this time spot
    public boolean appear(){
        boolean ans=false;
        if(isAppearing&&appearcd==0){
            ans=true;
        }
        return ans;
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final AnimatedObject that = this;
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(appearcd>0){
                    appearcd--;
                }
                boolean mAppear = appear();//see if it should appear now
                if(mAppear){
                    float fraction = animation.getAnimatedFraction();
                    Point size = (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                    if(direction){
                        //moving right
                        setX(size.x*fraction);
                    }else{
                        //moving left
                        setX(size.x*(1-fraction));
                    }
                }else{
                    //hide it in the far left
                    setX(-100);
                }

                Log.d("getting x of UFO"," "+getX());
            }
        };
    }


}
