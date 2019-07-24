package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.animation.BaseInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Developer Henry Yi & Xuanchen Zhou
 */
class InvaderGroup extends AnimatedObject  <ConstraintLayout> {
    private int aliveInvaders;
    private ArrayList<Invader> invaders;
    private int duration=20000;
    private int velocity=200;
    private int horizontalTimes=20;
    private PointF initialCoordinates;

    private boolean detection=true;

    InvaderGroup(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {

        super( null, view, resources, spaceGame,status, mainHandler, processHandler);

        this.aliveInvaders=this.getChildCount();
        invaders=new ArrayList<>(this.getChildCount());

        for (int i=0;i<this.getChildCount();i++){
            ImageView invaderView= (ImageView) this.getChildAt(i);
            invaders.add(new Invader(this.getAnimator(),invaderView,resources,spaceGame,status,mainHandler,processHandler));
        }


    }

    @Override
    public void setSpaceGame(SpaceGame spaceGame) {
        super.setSpaceGame(spaceGame);
        for (Invader invader:invaders){
            invader.setSpaceGame(getSpaceGame());
        }
    }

    @Override
    protected void handle(Actions actions) {
        Set<Integer> keys=actions.keySet();
        for (Integer key: keys){
            Pair<AnimatedObject, SparseArray<Float>> value=actions.get(key);
            switch (key){
                case SpaceGame.GAMESTART:
                    if (this.getAnimator()==null){
                        this.setAnimator(new ValueAnimator());
                        this.getAnimator().setIntValues(1,100);
                        this.getAnimator().setDuration(this.duration);
//                        this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
//                        this.getAnimator().setRepeatMode(ValueAnimator.RESTART);
                        this.getAnimator().setInterpolator(null);
                        this.getAnimator().addUpdateListener(animatorListenerConfigure());
                        this.getAnimator().start();
                    }

                    break;
                case SpaceGame.STRIKE:
                    Set<Integer> x=new ArraySet<>();
                    x.add(SpaceGame.STRIKE);
                    strikeInvaders(actions,x);
            }
        }
    }

    @Override
    protected void handle(Actions actions, Set<Integer> keys) {

    }

    public void setDetection(boolean detection) {
        this.detection = detection;
    }

    public boolean isDetection() {
        return detection;
    }

    private void strikeInvaders(Actions actions, Set<Integer> keys){

        for (Invader invader:invaders){
            if (!this.detection){
                break;
            }
            invader.handle(actions,keys);
        }
        this.detection=true;
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final InvaderGroup that=this;
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (initialCoordinates==null){
                    initialCoordinates=new PointF(that.getAbsoluteX(),that.getAbsoluteY());
                }
                float fraction=animation.getAnimatedFraction();
                if (fraction==1.0){
                    killLaserBase();
                    return;
                }
                Point size= (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                assert size != null;
                int deltaY=100;
                int lengthY=size.y-(that.getHeight())-deltaY-50-that.getSpaceGame().laserBase.getHeight();
                float subFraction=1f/that.horizontalTimes;
                int status= (int) (fraction/subFraction);
                float fractionX=(fraction%subFraction)*that.horizontalTimes;
                int lengthX=size.x-(that.getWidth());
                if(status%2==0){
                    that.setXRaw(lengthX*fractionX);
                }else if (fraction!=1.0){
                    that.setXRaw(lengthX*(1-fractionX));
                }
                that.setYRaw(that.initialCoordinates.y+lengthY*fraction);
                Actions actions=new Actions();
                actions.put(SpaceGame.STRIKE,new Pair<AnimatedObject, SparseArray<Float>>(that,null));
                that.getSpaceGame().laserBase.handle(actions);
//                Log.d("aaaas", String.valueOf(fraction));


            }
        };
    }

    private void killLaserBase(){
        Pair<Float,Float> value=getStatus().get(SpaceGame.NUM_LIVES);
        assert value != null;
        getStatus().put(SpaceGame.NUM_LIVES,new Pair<>(value.first-1,null));
        this.getSpaceGame().updateStatus(getStatus());
    }

    @Override
    public void setX(float x) {
        if (this.initialCoordinates!=null){
            this.initialCoordinates.x=x;
        }
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        if (this.initialCoordinates!=null){
            this.initialCoordinates.y=y;
        }
        super.setY(y);
    }

    public void setYRaw(float y) {
        super.setY(y);
    }

    public void setXRaw(float x) {
        super.setX(x);
    }
}
