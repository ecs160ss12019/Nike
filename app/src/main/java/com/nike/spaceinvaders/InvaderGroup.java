package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

/**
 * Developer Henry Yi & Xuanchen Zhou
 */
class InvaderGroup extends AnimatedObject  <ConstraintLayout> {
    private int aliveInvaders;
    private ArrayList<Invader> invaders;

    InvaderGroup(PointF position, Size size, ValueAnimator animator, ConstraintLayout view, HashMap<String, Resources> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
        super(position, size, animator, view, resources, spaceGame, mainHandler, processHandler);
        this.aliveInvaders=this.view.getChildCount();
        invaders=new ArrayList<>(this.view.getChildCount());
        for (int i=0;i<this.view.getChildCount();i++){
            ImageView invaderView= (ImageView) this.view.getChildAt(i);
            invaders.add(new Invader(new PointF(invaderView.getX(),invaderView.getY()),new Size(invaderView.getHeight(),invaderView.getWidth()),this.animator,invaderView,this.resources,this.spaceGame,this.mainHandler,this.processHandler));
        }

    }

    @Override
    protected void handle(Actions actions) {
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction=animation.getAnimatedFraction();

            }
        };
    }
}
