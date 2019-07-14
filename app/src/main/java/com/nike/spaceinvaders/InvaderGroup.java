package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.logging.Handler;

/**
 * Developer Henry Yi & Xuanchen Zhou
 */
class InvaderGroup extends AnimatedObject {
    private ConstraintLayout layout;

    InvaderGroup(PointF position, Size size, ValueAnimator animator, ImageView imageView, HashMap<String, Resources> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler, ConstraintLayout layout) {
        super(position, size, animator, imageView, resources, spaceGame, mainHandler, processHandler);
        this.layout=layout;

    }

    @Override
    protected void handle(Actions actions) {
        CoordinatorLayout
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {

    }
}
