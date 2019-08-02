package com.nike.spaceinvaders;

/*
- Generates and Manages the movement and size of invaders
- Detect the collision with missiles
- Spawns at the top of interface
 */


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

import android.os.Handler;


public class Invader extends AnimatedObject<ImageView> {
    private Missile missile;
    private int abstractionLevel = 10;
    private int index;
    private int row;
    private Random rand;
    private MissileForm missileForm;

    public static final int HIT_DETECTION=0b1;

    Invader(int row, int index, ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(animator, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);
        this.row = row;
        this.index = index;
        initMissileForm();
        rand = new Random();
        if(row==0){
            view.setBackgroundResource(R.drawable.orangeinvader);
        }else if(row==1){
            view.setBackgroundResource(R.drawable.yellowinvader);
        }else if(row==2){
            view.setBackgroundResource(R.drawable.blueinvader);
        }else if (row==3){
            view.setBackgroundResource(R.drawable.orangeinvader);
        }
        AnimationDrawable frameAnimation =  (AnimationDrawable) view.getBackground();
        if(frameAnimation!=null){
            frameAnimation.start();
        }
    }

    protected void kill(Actions actions, AnimatedObject missile) {
        this.setAlive(false);
        this.setVisibility(View.INVISIBLE);
        actions.put(SpaceGame.MISSILE_GONE, null);
        actions.put(SpaceGame.HIT, new Pair<>(this, null));
        missile.handle(actions, SpaceGame.MISSILE_GONE);
        this.getSpaceGame().invaderGroup.handle(actions, SpaceGame.HIT);
        this.getSpaceGame().updateStatus(updateStatusSelf());

    }

    protected SpaceGame.Status updateStatusSelf() {
        SpaceGame.Status status = getStatus();
        Pair<Float, Float> score = status.get(SpaceGame.SCORES);
        Pair<Float, Float> numOfInvaders = status.get(SpaceGame.NUM_INVADER);
        assert score != null;
        float newScore = score.first + 10;
        assert numOfInvaders != null;
        float newNumOfInvader=numOfInvaders.first-1;
        // Overwrite the old status value
        status.put(SpaceGame.SCORES, new Pair<>(newScore, null));
        status.put(SpaceGame.NUM_INVADER, new Pair<>(newNumOfInvader, null));
//        Log.d("Num_Invader_raw", String.valueOf(status.toString()));
        Log.d("Score", String.valueOf(newScore));
        Log.d("Num_Invader", String.valueOf(newNumOfInvader));
        return status;
    }


    @Override
    protected void initialize() {
        this.getMainHandler().postDelayed(()->{
            this.setAlive(true);
        },500);

        this.setVisibility(View.VISIBLE);
    }

    @Override
    protected void handle(Actions actions, Integer key) {

        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);

        switch (key) {
            case SpaceGame.STRIKE:
                assert value != null;
                if (this.isAlive() && hitDetection(value.first)) {
                    playDeathSound(); //Sound effect for invader being destroyed
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
        AI.Evaluator evaluator= AI.getAI(SpaceGame.INVADER_GROUP,this.getStatus());
        float rate=evaluator.evaluate(InvaderGroup.RATE_OF_MISSILE);
        Log.d("RateAI", String.valueOf(rate));
        int randNum = rand.nextInt((int) rate);

        if (randNum == 0) // chance is 1/2000
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
                this.missileForm = new OrangeInvaderMissileForm(
                        (Context) this.getResources().get(SpaceGame.CONTEXT));
                break;

            case 1:
                this.missileForm = new YellowInvaderMissileForm(
                        (Context) this.getResources().get(SpaceGame.CONTEXT));
                break;
            case 2:
                this.missileForm = new BlueInvaderMissileForm(
                        (Context) this.getResources().get(SpaceGame.CONTEXT));
                break;
            case 3:
                this.missileForm = new OrangeInvaderMissileForm(
                        (Context) this.getResources().get(SpaceGame.CONTEXT));
                break;
        }
    }


    /*
    Play a type of sound when an invader dies
     */
    private void playDeathSound()
    {
        switch(row)
        {
            case 0:
                this.getSoundEngine().playYellowInvaderDeath();
                break;

            case 1:
                this.getSoundEngine().playBlueInvaderDeath();
                break;

            case 2:
                this.getSoundEngine().playOrangeInvaderDeath();
                break;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void updateStatus(SpaceGame.Status status) {

    }
}