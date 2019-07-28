package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
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


public class Invader extends AnimatedObject<ImageView> {
    private boolean alive = true;
    private Missile missile;
    private int[][] hitbox;
    private int abstractionLevel = 10;
    private int index;
    private Random rand;

    Invader(int index, ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(animator, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);
        this.index = index;
        rand = new Random();
    }

    protected void kill(Actions actions, AnimatedObject missile) {
        alive = false;
        this.setVisibility(View.INVISIBLE);
        actions.put(SpaceGame.MISSILE_GONE, null);
        actions.put(SpaceGame.HIT, new Pair<>(this, null));
        missile.handle(actions, SpaceGame.MISSILE_GONE);
        this.getSpaceGame().invaderGroup.handle(actions, SpaceGame.HIT);
        notifySpaceGame();
    }

    protected void notifySpaceGame() {
        SpaceGame.Status status = getStatus();
        Pair<Float, Float> value = status.get(SpaceGame.SCORES);
        assert value != null;
        status.put(SpaceGame.SCORES, new Pair<>(value.first + 10, null));
        getSpaceGame().updateStatus(status);
    }



    protected boolean hitDetection(Actions actions, AnimatedObject missile) {
        if (!alive) {
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
            this.getSoundEngine().playInvaderKilled(); //Sound effect for invader being destroyed
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void handle(Actions actions, Integer key) {

        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);

        switch (key) {
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
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
            }
        };
    }

    /*
    Use random number generator to decide whether an invader
    will shoot in this frame
     */
    public boolean toShoot() {
        int randNum = rand.nextInt(2000);

        if (randNum == 1) // chance is 1/2000
        {
            return true;
        }
        return false;
    }


    protected void shootMissile() {
        Actions actions = new Actions();

        AnimatedObject missile = getSpaceGame().missilePool.getMissile();
        SparseArray<Float> values = new SparseArray<>();
        values.put(SpaceGame.X_COORDINATE, (this.getWidth() - 25) / 2 + this.getAbsoluteX());
        values.put(SpaceGame.Y_COORDINATE, (this.getAbsoluteY()));
        // missile moving down
        values.put(SpaceGame.MOVE_DIRECTION, 0f);
        actions.put(SpaceGame.FIRE, new Pair<>(this, values));

        if (missile != null) {
            missile.handle(actions, SpaceGame.FIRE);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }



    public boolean isAlive()
    {
        return alive;
    }
}
