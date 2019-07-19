package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;

import java.util.HashMap;

public class Lives extends AnimatedObject<ConstraintLayout> {
    private int lives;
    Lives( ConstraintLayout view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler,int lives) {
        super(new PointF(view.getX(),view.getY()), new Size(view.getHeight(),view.getWidth()), null, view, resources, spaceGame, mainHandler, processHandler);
        this.lives=lives;
    }

    @Override
    protected void handle(Actions actions) {

    }

    private void hurt(){
        this.lives--;
    }

    public int getLives() {
        return lives;
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
