package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.util.Pair;
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

    InvaderGroup(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {

        super( new ValueAnimator(), view, resources, spaceGame,status, mainHandler, processHandler);

        this.aliveInvaders=this.getChildCount();
        invaders=new ArrayList<>(this.getChildCount());

        for (int i=0;i<this.getChildCount();i++){
            ImageView invaderView= (ImageView) this.getChildAt(i);
            invaders.add(new Invader(this.getAnimator(),invaderView,this.getResources(),this.getSpaceGame(),this.getStatus(),this.getMainHandler(),this.getProcessHandler()));
        }


    }

    @Override
    protected void handle(Actions actions) {
        Set<Integer> keys=actions.keySet();
        for (Integer key: keys){
            Pair<AnimatedObject, ArrayList<Float>> value=actions.get(key);
            switch (key){
                case SpaceGame.GAMESTART:
                    if (this.getAnimator()==null){
                        this.setAnimator(new ValueAnimator());
                    }

                    this.getAnimator().setIntValues(1,100);
                    this.getAnimator().setDuration(this.duration);
                    this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
                    this.getAnimator().setRepeatMode(ValueAnimator.REVERSE);
                    this.getAnimator().setInterpolator(null);
                    this.getAnimator().addUpdateListener(animatorListenerConfigure());
                    if (initialCoordinates==null){
                        initialCoordinates=new PointF(this.getX(),this.getY());
                    }
                    this.getAnimator().start();
//                    this.animator.cancel();

                    break;
                case SpaceGame.STRIKE:
                    assert value != null;
            }
        }
    }

    @Override
    protected void handle(Actions actions, Set keys) {

    }

    private void strikeInvaders(Pair<AnimatedObject, ArrayList<Float>> value, PointF position){

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final InvaderGroup that=this;
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float fraction=animation.getAnimatedFraction();
                Point size= (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);

//                float processedFraction= (float) (fraction-0.5)*2;
                assert size != null;
                int deltaY=100;
                int lengthY=size.y-(that.getHeight())-deltaY-100;
                float subFraction=1f/that.horizontalTimes;
                int status= (int) (fraction/subFraction);
                float fractionX=(fraction%subFraction)*that.horizontalTimes;
                int lengthX=size.x-(that.getWidth());
                if(status%2==0){
                    that.setXRaw(that.initialCoordinates.x+lengthX*fractionX);
                }else if (fraction!=1.0){
                    that.setXRaw(that.initialCoordinates.x+lengthX*(1-fractionX));
                }
                that.setYRaw(that.initialCoordinates.y+lengthY*fraction);
                Actions actions=new Actions();
                actions.put(SpaceGame.STRIKE,new Pair<AnimatedObject, ArrayList<Float>>(that,null));
                that.getSpaceGame().laserBase.handle(actions);

            }
        };
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
