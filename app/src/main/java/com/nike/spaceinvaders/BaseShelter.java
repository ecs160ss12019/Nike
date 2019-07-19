/* Weili Yin, Roberto Lozano */

package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Set;

class BaseShelter extends AnimatedObject<ImageView> {


    BaseShelter(PointF position, Size size, ValueAnimator animator, ImageView view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
        super(position, size, animator, view, resources, spaceGame, mainHandler, processHandler);
    }

    @Override
    protected void handle(Actions actions) {

    }

    @Override
    protected void handle(Actions actions, Set keys) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
