package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Set;

public class Lives extends AnimatedObject<ConstraintLayout> {

    private int lives;
    private View[] livesViews;

    Lives(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler, int lives,SoundEngine soundEngine) {
        super(null, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);
        this.lives = lives;
        this.livesViews = new ImageView[3];
        for (int index = 0; index < this.getChildCount(); index++) {
            this.livesViews[index] = this.getChildAt(index);
        }
        updateLives();
    }

    /**
     * Sets int values that will be animated between. A single
     * value implies that that value is the one being animated to. However, this is not typically
     * useful in a ValueAnimator object because there is no way for the object to determine the
     * starting value for the animation (unlike ObjectAnimator, which can derive that value
     * from the target object and property being animated). Therefore, there should typically
     * be two or more values.
     *
     * <p>If there are already multiple sets of values defined for this ValueAnimator via more
     * than one PropertyValuesHolder object, this method will set the values for the first
     * of those objects.</p>
     *
     * @param actions The actions to be handled by MissIle:
     *                "#LIFE_GONE" indicates the one life should be gone and vanished.
     *                "#LIFE_ADD" indicates the one life should added and resurrected.
     *                "#RESURRECTION" indicates the laserLase's lives restored to full mark.
     * @param key
     */

    @Override
    protected void handle(Actions actions, Integer key) {
        switch (key) {
            case SpaceGame.LIFE_ADD:
                regen(actions);
                updateLives();
                break;
            case SpaceGame.LIFE_GONE:
                hurt(actions);
                updateLives();
                break;
            default:
                return;
        }

    }

    private void hurt(Actions actions) {
        if (this.lives < 1) {
            //pass GAMEOVER to Game
            //getSpaceGame().gameover();
            return;
        }
        this.livesViews[lives - 1].setVisibility(View.INVISIBLE);
        this.lives--;
    }

    private void regen(Actions actions) {
        if (lives < 3) {
            this.lives++;
        }
    }

    public int getLives() {
        return lives;
    }

    private void updateLives() {
        int livenum = this.lives;
        livesViews[0].setVisibility(View.INVISIBLE);
        livesViews[1].setVisibility(View.INVISIBLE);
        livesViews[2].setVisibility(View.INVISIBLE);
        for (int i = 0; i < livenum; i++) {
            livesViews[i].setVisibility(View.VISIBLE);
        }
    }


    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
