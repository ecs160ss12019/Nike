package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.logging.Handler;

/**
 * Developer Henry Yi
 */
class LaserBase extends AnimatedObject <ImageView>{


    LaserBase(PointF position, Size size, ValueAnimator animator, ImageView view, HashMap<String, Resources> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
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
