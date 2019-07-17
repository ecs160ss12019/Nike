package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.widget.ImageView;

import java.util.HashMap;
import android.os.Handler;

public class Invader extends AnimatedObject <ImageView> {
    private boolean status=true;
    Invader(PointF position, Size size, ValueAnimator animator, ImageView view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
        super(position, size, animator, view, resources, spaceGame, mainHandler, processHandler);

    }

    public void kill(){
        this.status=false;
    }

    public boolean diagnose(){
        return this.status;
    }

    @Override
    protected void handle(Actions actions) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
