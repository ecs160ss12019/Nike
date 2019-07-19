// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/*
    Invaders and laserBase will use missile to attack
    each other.
    Missile moves straight up/down once attacker releases
    it.
 */
class Missile extends AnimatedObject <ImageView>{
    private boolean type;

    Missile(PointF position, Size size, ValueAnimator animator, ImageView view, HashMap<Integer, Object> resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(position, size, animator, view, resources, spaceGame, status, mainHandler, processHandler);
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
     *                "#MISSILE_GONE" indicates the missile was collided with other object, and gone.
     *                "#FIRE" indicates the missile needs to move and strike objects.
     * @param keys The keys that we need to iterate through.
     */
    @Override
    protected void handle(Actions actions, Set keys) {
        BaseShelterGroup baseShelterGroup=getSpaceGame().baseShelterGroup;

        Actions newActions=new Actions();
        ArrayList<Float> coordinates=new ArrayList<>(2);
        coordinates.add(1f);
        coordinates.add(2f);
        newActions.put(SpaceGame.STRIKE,new Pair<AnimatedObject, ArrayList<Float>>(this,coordinates));
        baseShelterGroup.handle(newActions);
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
