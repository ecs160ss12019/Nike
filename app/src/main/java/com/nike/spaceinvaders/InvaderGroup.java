package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.util.Pair;
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
    private int duration=3000;

    InvaderGroup(ConstraintLayout view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {

        super(null, null, null, view, resources, spaceGame, mainHandler, processHandler);

        this.position=new PointF(this.view.getX(),this.view.getY());
        this.size=new Size(this.view.getHeight(),this.view.getWidth());
        this.aliveInvaders=this.view.getChildCount();
        invaders=new ArrayList<>(this.view.getChildCount());

        for (int i=0;i<this.view.getChildCount();i++){
            ImageView invaderView= (ImageView) this.view.getChildAt(i);
            invaders.add(new Invader(new PointF(invaderView.getX(),invaderView.getY()),new Size(invaderView.getHeight(),invaderView.getWidth()),this.animator,invaderView,this.resources,this.spaceGame,this.mainHandler,this.processHandler));
        }


    }

    @Override
    protected void handle(Actions actions) {
        HashMap<String, Pair<AnimatedObject, ArrayList<Float>>> actionSet=actions.getActionSet();
        Set<String> keys=actionSet.keySet();
        Log.d("infod","oaoaooa");
        for (String key: keys){
            Pair<AnimatedObject, ArrayList<Float>> value=actionSet.get(key);
            switch (key){
                case "start":
                    if (this.animator==null){
                        this.animator=new ValueAnimator();
                    }

                    this.animator.setIntValues(1,100);
                    this.animator.setDuration(this.duration);
                    this.animator.setRepeatCount(ValueAnimator.INFINITE);
                    this.animator.setRepeatMode(ValueAnimator.REVERSE);
                    this.animator.addUpdateListener(animatorListenerConfigure());
                    this.animator.start();
//                    this.animator.cancel();
                    break;
            }
        }
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final InvaderGroup that=this;
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction=animation.getAnimatedFraction();
                Point size= (Point) that.resources.get("WindowSize");
//                float processedFraction= (float) (fraction-0.5)*2;
                assert size != null;
                int deltaX=-100;
                int length=size.x-(view.getWidth()-100)-deltaX;

                that.view.setX(deltaX+length*fraction);

            }
        };
    }
}
