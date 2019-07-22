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
    protected void handle(Actions actions,Set<Integer> keys) {
        for (Integer key: keys){
            Pair<AnimatedObject, ArrayList<Float>> value=actions.get(key);
            switch (key){
                case SpaceGame.TEST: //when we get some point
                    score.setText("1000");
                    break;
                default: return;
            }
        }
    }


    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
