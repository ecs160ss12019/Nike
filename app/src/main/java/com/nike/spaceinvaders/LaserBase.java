package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Developer Henry Yi
 */
class LaserBase extends AnimatedObject{
    LaserBase(ValueAnimator animator, ImageView imageView, HashMap<String, Resources> resources) {
        super(animator, imageView, resources);
    }

    @Override
    protected void handle(Actions actions) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
