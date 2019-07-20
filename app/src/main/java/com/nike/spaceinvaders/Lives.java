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
    protected void handle(Actions actions, Set keys) {
/*        switch(actions.get()) {
            case getSpaceGame().LIFE_ADD:
                // code block
                break;
            case getSpaceGame().LIFE_GONE:
                // code block
                break;
            default:
                // code block
        }*/

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
