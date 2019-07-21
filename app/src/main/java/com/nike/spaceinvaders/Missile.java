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
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;


/*
    Invaders and laserBase will use missile to attack
    each other.
    Missile moves straight up/down once attacker releases
    it.
 */
class Missile extends AnimatedObject <ImageView>  {
    private long time;
    private boolean recyclable;
    private boolean status=true;
    private int key;
    private MissilePool pool;

    // whether the missile is moving up or down
    private boolean up;
    // The width and height of missile
    private float width;
    private float height;
    private float speed;

    Missile(int key, boolean recyclable, MissilePool pool,ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super( new ValueAnimator(), view, resources, spaceGame, status, mainHandler, processHandler);
        this.key=key;
        this.recyclable=recyclable;
        this.pool=pool;
    
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
     * @param actions The actions to be handled by Missile:
     *                "#MISSILE_GONE" indicates the missile was collided with other object, and gone.
     *                "#FIRE" indicates the missile needs to move and strike objects.
     * @param keys The keys that we need to iterate through.
     */
    @Override
    protected void handle(Actions actions, Set keys) {
        Set<Integer> oldKeys = actions.keySet();
        for(Integer key:oldKeys){
            switch(key){
                case SpaceGame.FIRE:
                    /*
                        Missile has just been fired
                     */

                    // get the starting position of missile
                    ArrayList<Float> startPts = Objects.requireNonNull(actions.get(key)).second;
                    float startX = startPts.get(0);
                    float startY = startPts.get(1);
                    float endY = findEndYPos();

                    // load the missile
                    this.setVisibility(View.VISIBLE);
                    // set starting x position


                    // set up trajectory for missile

                    // do we really need to check null?
                    if (this.getAnimator() == null){
                        this.setAnimator(new ValueAnimator());
                    }

                    this.getAnimator().setFloatValues(startY, endY);
                    this.getAnimator().setDuration(((long)(Math.abs(endY - startY) / speed)));
                    // repeat?
                  //  this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
                    this.getAnimator().addUpdateListener(animatorListenerConfigure());
                    this.getAnimator().start();
                    break;


                case SpaceGame.STRIKE:
                    /*
                        Missile is moving and may hit an object
                     */
                    Actions newActions=new Actions();
                    ArrayList<Float> coordinates=new ArrayList<>(2);
                    coordinates.add(getX());
                    coordinates.add(getY());
                    newActions.put(SpaceGame.STRIKE,
                            new Pair<AnimatedObject, ArrayList<Float>>(this,coordinates));
                    getSpaceGame().baseShelterGroup.handle(newActions);
                    break;

            }


        }
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }


    public void initialize() {
        // set the missile to be invisible
        this.setVisibility(View.INVISIBLE);

        Point screenPt = (Point)this.getResources().get(SpaceGame.WINDOW_SIZE);
        // The width of missile will be 1 percent of the screen width
        width = screenPt.x / 100;
        // the height of missile will be 1/25 of the screen height
        height = screenPt.y / 25;

        speed = 600;
    }


    /*
        Find where the missile should end if there is no collision;
        It should end at the top of screen if it is going up,
        and end at the bottom of screen otherwise.
     */
    private float findEndYPos()
    {
        if(up)
            return 0;
        else
            return ((Point)this.getResources().get(SpaceGame.WINDOW_SIZE)).y;
    }








    public int getKey() {
        return key;
    }

    public void recycle() throws Exception {
        initialize();
        this.pool.recycle(this);
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRecyclable() {
        return recyclable;
    }

    public void setRecyclable(boolean recyclable) {
        this.recyclable = recyclable;
    }

    public void setPool(MissilePool pool) {
        this.pool = pool;
    }

    public MissilePool getPool() {
        return pool;
    }
}
