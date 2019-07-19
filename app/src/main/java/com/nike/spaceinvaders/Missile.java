// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.widget.ImageView;

import java.util.HashMap;


/*
    Invaders and laserBase will use missile to attack
    each other.
    Missile moves straight up/down once attacker releases
    it.
 */
class Missile extends AnimatedObject <ImageView>{

    Missile(  ImageView view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
        super(new PointF(view.getX(),view.getY()), new Size(view.getHeight(),view.getWidth()),new ValueAnimator(), view, resources, spaceGame, mainHandler, processHandler);
    }

    @Override
    protected void handle(Actions actions) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
