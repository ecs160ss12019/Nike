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

        super(new PointF(view.getX(),view.getY()), new Size(view.getHeight(),view.getWidth()), new ValueAnimator(), view, resources, spaceGame, mainHandler, processHandler);

        this.aliveInvaders=this.getView().getChildCount();
        invaders=new ArrayList<>(this.getView().getChildCount());

        for (int i=0;i<this.getView().getChildCount();i++){
            ImageView invaderView= (ImageView) this.getView().getChildAt(i);
            invaders.add(new Invader(new PointF(invaderView.getX(),invaderView.getY()),new Size(invaderView.getHeight(),invaderView.getWidth()),this.getAnimator(),invaderView,this.getResources(),this.getSpaceGame(),this.getMainHandler(),this.getProcessHandler()));
        }


    }

    @Override
    protected void handle(Actions actions) {
        HashMap<String, Pair<AnimatedObject, ArrayList<Float>>> actionSet=actions.getActionSet();
        Set<String> keys=actionSet.keySet();
        for (String key: keys){
            Pair<AnimatedObject, ArrayList<Float>> value=actionSet.get(key);
            actionSet.entrySet();
            switch (key){
                case "start":
                    if (this.getAnimator()==null){
                        this.setAnimator(new ValueAnimator());
                    }

                    this.getAnimator().setIntValues(1,100);
                    this.getAnimator().setDuration(this.duration);
                    this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
                    this.getAnimator().setRepeatMode(ValueAnimator.REVERSE);
                    this.getAnimator().addUpdateListener(animatorListenerConfigure());
                    this.getAnimator().start();
//                    this.animator.cancel();
                    break;
                case "strike":
                    assert value != null;
                    strikeInvaders(value,actions.getPosition());
            }
        }
    }

    private void strikeInvaders(Pair<AnimatedObject, ArrayList<Float>> value, PointF position){
        HashMap<String, Pair<AnimatedObject, ArrayList<Float>>> actionSet=new HashMap<>();
        actionSet.put("strike",value);
        Actions actions=new Actions(position,actionSet);
        for (Invader invader:this.invaders){
            invader.handle(actions);
        }
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final InvaderGroup that=this;
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction=animation.getAnimatedFraction();
                Point size= (Point) that.getResources().get("WindowSize");
//                float processedFraction= (float) (fraction-0.5)*2;
                assert size != null;
                int deltaX=-100;
                int length=size.x-(that.getView().getWidth()-100)-deltaX;

                that.getView().setX(deltaX+length*fraction);

            }
        };
    }
}
