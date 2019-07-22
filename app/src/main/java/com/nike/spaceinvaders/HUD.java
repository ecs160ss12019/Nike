package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.util.Pair;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HUD extends AnimatedObject <ConstraintLayout> {
    private TextView score;
    private ConstraintLayout lives;

    @IdRes
    private final int scoreId=R.id.score;
    @IdRes
    private final int livesId=R.id.lives;


    HUD(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(null, view, resources, spaceGame,status, mainHandler, processHandler);
        this.score=view.findViewById(scoreId);
        this.lives=view.findViewById(livesId);
    }

    @Override
    protected void handle(Actions actions) {
        Set<Integer> keys=actions.keySet();
        for (Integer key: keys){
            Pair<AnimatedObject, ArrayList<Float>> value=actions.get(key);
            switch (key){
                case SpaceGame.LIFE_ADD:
                    regen();
                    break;
                case SpaceGame.LIFE_GONE:
                    hurt();
                    break;
                default: return;
            }
        }
    }

    private void regen() {

    }
    private void hurt() {
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
