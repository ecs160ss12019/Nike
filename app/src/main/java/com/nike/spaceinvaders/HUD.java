package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HUD extends AnimatedObject <ConstraintLayout> {
    private TextView score;
    private ConstraintLayout LiveLayout;
    private ImageView live1,live2,live3;
    private Lives lives;


    @IdRes
    private final int scoreId=R.id.score;
    @IdRes
    private final int livesId=R.id.lives;
    @IdRes
    private final int live1Id=R.id.live1;
    @IdRes
    private final int live2Id=R.id.live2;
    @IdRes
    private final int live3Id=R.id.live3;


    HUD(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(null, view, resources, spaceGame,status, mainHandler, processHandler);
        //init
        lives = new Lives(view,resources,spaceGame,status,mainHandler,processHandler,3);
        this.score=view.findViewById(scoreId);
        this.LiveLayout=view.findViewById(livesId);
        this.live1=view.findViewById(live1Id);
        this.live2=view.findViewById(live2Id);
        this.live3=view.findViewById(live3Id);
    }

    @Override
    protected void handle(Actions actions,Set<Integer> keys) {
        for (Integer key: keys){
            Pair<AnimatedObject, ArrayList<Float>> value=actions.get(key);
            switch (key){

                case SpaceGame.TEST: //Test only
                    score.setText("Test");
                    break;
                case SpaceGame.LIFE_ADD: //when we get some point
                    break;
                case SpaceGame.LIFE_GONE: //when we get some point

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
