package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Developer Henry Yi
 */
class LaserBase extends AnimatedObject{


    LaserBase(PointF position, Size size, ValueAnimator animator, ImageView imageView, HashMap<String, Resources> resources) {
        super(position, size, animator, imageView, resources);
    }

    @Override
    protected void handle(Actions actions) {
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
