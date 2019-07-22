package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;

import java.util.HashMap;
import java.util.Set;

public class Lives extends AnimatedObject<ConstraintLayout> {
    private int lives;
    Lives(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler, int lives) {
        super(null, view, resources, spaceGame,status, mainHandler, processHandler);
        this.lives=lives;
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
     * @param keys The keys that we need to iterate through.
     */

    @Override
    protected void handle(Actions actions, Set<Integer> keys) {
        for(Integer key:keys){
            switch(key) {
                case SpaceGame.LIFE_ADD:
                    regen(actions);
                    break;
                case SpaceGame.LIFE_GONE:
                    hurt(actions);
                    break;
                default:
                    return;
            }
        }
    }

    private void hurt(Actions actions){
        this.lives--;
        if(lives==0){
            //pass GAMEOVER to Game
        }
        getSpaceGame().hud.handle(actions);//change HUD
    }
    private void regen(Actions actions){
        if(lives<3) {
            this.lives++;
            getSpaceGame().hud.handle(actions);//change HUD
        }
    }

    public int getLives() {
        return lives;
    }




    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
