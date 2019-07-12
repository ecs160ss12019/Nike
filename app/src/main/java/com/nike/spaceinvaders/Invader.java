package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Developer Henry Yi & Xuanchen Zhou
 */
class Invader extends AnimatedObject {


    Invader(PointF position, ValueAnimator animator, ImageView imageView, HashMap<String, Resources> resources) {
        super(position, animator, imageView, resources);
    }

    @Override
    protected void handle(Actions actions) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
