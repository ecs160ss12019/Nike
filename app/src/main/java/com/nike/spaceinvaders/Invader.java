package com.nike.spaceinvaders;

/*
- Generates and Manages the movement and size of invaders
- Detect the collision with missiles
- Spawns at the top of interface
 */
import android.animation.ValueAnimator;
import android.content.Context;
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
    public boolean alive = true;
    private Missile missile;
    private int[][] hitbox;
    private int abstractionLevel = 10;
    private int index;
    private int row;
    private Random rand;
    private MissileForm missileForm;

    Invader(int row, int index, ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(animator, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);
        this.row = row;
        this.index = index;
        initMissileForm();
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


    @Override
    protected void handle(Actions actions, Integer key) {

        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);

        switch (key) {
            case SpaceGame.STRIKE:
                assert value != null;
                if (this.alive && hitDetection(value.first)) {
                    this.getSoundEngine().playInvaderDeath(); //Sound effect for invader being destroyed
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
        final Invader that = this;
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
            ((Missile) missile).setMissileForm(this.missileForm);
            missile.handle(actions, SpaceGame.FIRE);
        }
    }


    private void initMissileForm()
    {
        switch(row)
        {
            case 0:
                this.missileForm = new InvaderAMissileForm(
                        (Context) this.getResources().get(SpaceGame.CONTEXT));
                break;

            case 1:
                this.missileForm = new InvaderBMissileForm(
                        (Context) this.getResources().get(SpaceGame.CONTEXT));
                break;
            case 2:
                this.missileForm = new InvaderCMissileForm(
                        (Context) this.getResources().get(SpaceGame.CONTEXT));
                break;
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

    @Override
    public float evaluate(float value) {
        return 0;
    }
}
