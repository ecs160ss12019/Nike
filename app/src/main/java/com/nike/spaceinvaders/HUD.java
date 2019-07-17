package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;

import java.util.HashMap;

public class HUD extends AnimatedObject <ConstraintLayout> {
    HUD(PointF position, Size size, ValueAnimator animator, ConstraintLayout view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
        super(position, size, animator, view, resources, spaceGame, mainHandler, processHandler);
    }

    @Override
    protected void handle(Actions actions) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
