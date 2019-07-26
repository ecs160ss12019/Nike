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

    protected void handle(Actions actions,Integer key) {
    }




    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }

    @Override
    public void updateStatus(SpaceGame.Status status) {
        Set<Integer> keys=status.keySet();
        for (Integer key:keys){
            switch (key){
                case SpaceGame.NUM_LIVES:
                    updateLives(status);
                    break;
                case SpaceGame.SCORES:
                    updateScores(status);
                    break;
                default:
                    break;
            }
        }

    }

    private void updateScores(SpaceGame.Status status) {
        Float scoreTemp=(Objects.requireNonNull(status.get(SpaceGame.SCORES)).first);
        int score=scoreTemp.intValue();
        this.score.setText(String.valueOf(score));
    }
    private void updateLives(SpaceGame.Status status) {
        Float livesTemp=(Objects.requireNonNull(status.get(SpaceGame.NUM_LIVES)).first);
        int lives=livesTemp.intValue();
        Actions actions=new Actions();

        for(int index =0; index<Math.abs(lives-this.lives);index++){
            if (lives-this.lives>0){
                actions.put(SpaceGame.LIFE_ADD,null);
                this.liveLayout.handle(actions);
            }else if (lives-this.lives<0){
                actions.put(SpaceGame.LIFE_GONE,null);
                this.liveLayout.handle(actions);
            }
        }
        this.lives=lives;
    }
}
