package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Set;

public class HUD extends AnimatedObject <ConstraintLayout> {
    private TextView score;
    private ConstraintLayout lives;

    @IdRes
    private final int scoreId=R.id.score;
    @IdRes
    private final int livesId=R.id.lives;


    HUD( ConstraintLayout view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
        super(new PointF(view.getX(),view.getY()), new Size(view.getHeight(),view.getWidth()),null, view, resources, spaceGame, mainHandler, processHandler);
        this.score=view.findViewById(scoreId);
        this.lives=view.findViewById(livesId);
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
