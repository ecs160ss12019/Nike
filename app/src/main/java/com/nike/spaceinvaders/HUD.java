package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class HUD extends AnimatedObject <ConstraintLayout> implements StatusManager {
    private TextView score;
    private Lives liveLayout;
    private int lives;
    private static int LIVEMAX=3;


    @IdRes
    private final int scoreId=R.id.score;
    @IdRes
    private final int livesId=R.id.lives;


    HUD(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(null, view, resources, spaceGame,status, mainHandler, processHandler);
        //init
        this.lives=this.LIVEMAX;
        this.score=view.findViewById(scoreId);
        this.liveLayout=new Lives((ConstraintLayout) view.findViewById(livesId),resources,spaceGame,status,mainHandler,processHandler,LIVEMAX);
    }

    @Override

    protected void handle(Actions actions,Set<Integer> keys) {
        for (Integer key: keys){
            Pair<AnimatedObject, SparseArray<Float>> value=actions.get(key);
            switch (key){
                case SpaceGame.TEST: //Test only
                    score.setText("Test");
                    updateLives();
                    break;
                case SpaceGame.LIFE_ADD: //when we gain live
                    lives.handle(actions,keys);
                    updateLives();
                    break;
                case SpaceGame.LIFE_GONE: //when we lose live
                    lives.handle(actions,keys);
                    updateLives();
                    break;
                case SpaceGame.SCORES:

                    //score.setText();
                    break;
                default: return;
            }
        }
    }

    private void updateLives() {
        int livenum = lives.getLives();
        switch (livenum){
            case 1:
                live1.setVisibility(TextView.VISIBLE);
                live2.setVisibility(TextView.INVISIBLE);
                live3.setVisibility(TextView.INVISIBLE);
                break;
            case 2:
                live1.setVisibility(TextView.VISIBLE);
                live2.setVisibility(TextView.VISIBLE);
                live3.setVisibility(TextView.INVISIBLE);
                break;
            case 3:
                live1.setVisibility(TextView.VISIBLE);
                live2.setVisibility(TextView.VISIBLE);
                live3.setVisibility(TextView.VISIBLE);
                break;
                default: return;
        }
    }


    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }

    @Override
    public void updateStatus(SpaceGame.Status status) {
        Float livesTemp=(Objects.requireNonNull(status.get(SpaceGame.NUM_LIVES)).first);
        int lives=livesTemp.intValue();


    }
}
