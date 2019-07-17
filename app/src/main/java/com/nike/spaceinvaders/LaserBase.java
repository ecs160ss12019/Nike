package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import android.os.Handler;

/**
 * Developer Henry Yi
 */
class LaserBase extends AnimatedObject <ImageView>{


    LaserBase(ValueAnimator animator, ImageView view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
        super(new PointF(view.getX(),view.getY()), new Size(view.getHeight(),view.getWidth()), animator, view, resources, spaceGame, mainHandler, processHandler);
    }

    @Override
    protected void handle(Actions actions) {
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
