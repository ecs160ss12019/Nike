package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Set;

import android.os.Handler;

/**
 * Developer Henry Yi
 */
class LaserBase extends AnimatedObject <ImageView>{
    private int velocity;

    LaserBase(  ImageView view, HashMap<Integer, Object> resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(null, view, resources, spaceGame, status, mainHandler, processHandler);
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
     *                "#MOVE_LEFT" indicates the laserBase's movement.
     *                "#MOVE_RIGHT" indicates the laserBase's movement.
     *                "#STRIKE" indicates the laserBase fires a missile.
     */

    @Override
    protected void handle(Actions actions, Set keys) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
