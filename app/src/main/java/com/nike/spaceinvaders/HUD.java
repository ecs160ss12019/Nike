package com.nike.spaceinvaders;

/*
- Records information of status like Lifes, score, and level
- Update them when variables change
 */


import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class HUD extends AnimatedObject <ConstraintLayout> {
    private TextView score,message;
    private Lives liveLayout;
    private int lives;
    public static int LIVEMAX=3;
    public SpaceGame spaceGame;

    @IdRes
    private final int scoreId=R.id.score;
    @IdRes
    private final int livesId=R.id.lives;
    @IdRes
    private final int messageId=R.id.message;

    HUD(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(null, view, resources, spaceGame,status, mainHandler, processHandler,soundEngine);
        //init
        this.lives=this.LIVEMAX;
        this.message=view.findViewById(messageId);
        this.score=view.findViewById(scoreId);
        this.liveLayout=new Lives((ConstraintLayout) view.findViewById(livesId),resources,spaceGame,status,mainHandler,processHandler,LIVEMAX,soundEngine);
        this.spaceGame=spaceGame;
    }

    @Override
    protected void initialize() {
        score.setText("0");
        liveLayout.initialize();
        this.lives=LIVEMAX;
    }

    @Override

    protected void handle(Actions actions,Integer key) {
    }

    @Override
    public void setSpaceGame(SpaceGame spaceGame){
        this.spaceGame = spaceGame;
        liveLayout.setSpaceGame(spaceGame);
    }



    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }

    @Override
    public void updateStatus(SpaceGame.Status status) {
        Set<Integer> keys=status.keySet();
        Pair<Float,Float> newValue;
        Pair<Float,Float> oldValue;
        for (Integer key:keys){
            switch (key){
                case SpaceGame.NUM_LIVES:
                    updateLives(status);
                    break;
                case SpaceGame.SCORES:
                    updateScores(status);
                    break;
                case SpaceGame.LEVEL:
                    newValue=status.get(key);
                    oldValue=this.getStatus().get(key);
                    assert newValue != null;
                    Log.d("LevelValue", String.valueOf(oldValue));
                    Log.d("LevelValue", String.valueOf(newValue));
                    if (!newValue.equals(oldValue)) {
                        updateMessage("NEW LEVEL - " + newValue.first);
                        this.getStatus().put(key,new Pair<>(newValue.first,null));
                    }

                default:
                    break;
            }
        }

    }

    //Fancy flashing message display
    public void updateMessage(String str){

        message.setText(str);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        message.startAnimation(anim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                message.setText("");
            }
        }, 1000);   //5 seconds

    }

    private void updateScores(SpaceGame.Status status) {
        int initScore= Integer.valueOf(score.getText().toString());
        Float scoreTemp=(Objects.requireNonNull(status.get(SpaceGame.SCORES)).first);
        int score=scoreTemp.intValue();
        this.score.setText(String.valueOf(score));

        //Test only
        if(scoreTemp>initScore)
            updateMessage("+");
    }
    private void updateLives(SpaceGame.Status status) {
        Float livesTemp=(Objects.requireNonNull(status.get(SpaceGame.NUM_LIVES)).first);
        int lives=livesTemp.intValue();
        Actions actions=new Actions();
        //judge if live increase or decrease
        for(int index =0; index<Math.abs(lives-this.lives);index++){
            if (lives-this.lives>0){
                actions.put(SpaceGame.LIFE_ADD,null);
                this.liveLayout.handle(actions);
                //TESTonly
                updateMessage("Nice");
            }else if (lives-this.lives<0){
                actions.put(SpaceGame.LIFE_GONE,null);
                this.liveLayout.handle(actions);
                //TestOnly
                updateMessage("OUCH");
            }
        }
        this.lives=lives;
    }

    public int getScore(){
        return Integer.valueOf(score.getText().toString());
    }

}
