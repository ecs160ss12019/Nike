// Author: Zhiyuan Guo

package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
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

    private float startX;
    private float startY;


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
                    SparseArray<Float> startPts = Objects.requireNonNull(actions.get(key)).second;
                    this.startX = startPts.get(SpaceGame.X_COORDINATE);
                    this.startY = startPts.get(SpaceGame.Y_COORDINATE);
                    this.up=startPts.get(SpaceGame.MOVE_DIRECTION)==1f;
                    float endY = findEndYPos();
                    Log.d("debugging5", String.valueOf(endY));
                    Log.d("debugging5", String.valueOf(startY));
                    // load the missile
                    this.setVisibility(View.VISIBLE);
                    // set starting x position


                    // set up trajectory for missile

                    // do we really need to check null?
                    if (this.getAnimator() == null){
                        this.setAnimator(new ValueAnimator());

                        this.getAnimator().setFloatValues(startY, endY);
                        Log.d("debugging4", String.valueOf(Math.abs(endY - startY) / speed));
                        this.getAnimator().setDuration(((long)(Math.abs(endY - startY) / speed)*1000));
                        // repeat?
                        //  this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
                        this.getAnimator().addUpdateListener(animatorListenerConfigure());
                        this.getAnimator().start();
                    }


                    break;


                case SpaceGame.STRIKE:
                    /*
                        Missile is moving and may hit an object
                     */
                    Actions newActions=new Actions();
                    SparseArray<Float> coordinates=new SparseArray<>(2);
                    // Add the missile's absolute coordinates
                    coordinates.put(SpaceGame.X_COORDINATE,getAbsoluteX());
                    coordinates.put(SpaceGame.Y_COORDINATE,getAbsoluteY());
                    newActions.put(SpaceGame.STRIKE,
                            new Pair<AnimatedObject, SparseArray<Float>>(this,coordinates));
                    getSpaceGame().baseShelterGroup.handle(newActions);
                    getSpaceGame().laserBase.handle(newActions);
                    getSpaceGame().invaderGroup.handle(newActions);
                    // handle missile collides with each other
                    break;


                case SpaceGame.MISSILE_GONE:
                    /*
                        Missile hit an object and should disappear;
                        Called by whom missile collides with
                     */

                    try {
                        recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }


        }
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final AnimatedObject that=this;
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d("debugging2", String.valueOf(that.getX()));
                Log.d("debugging3", String.valueOf(that.getY()));
                float fraction=animation.getAnimatedFraction();
                Point size= (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                assert size != null;
                int lengthY= (int) (findEndYPos()-(((Missile) that).startY));
                Log.d("debugging", String.valueOf(lengthY));
                that.setY(((Missile) that).startY+fraction*lengthY);
                that.setX(((Missile) that).startX);
                if(fraction==1.0){
                    try {
                        ((Missile) that).recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }


    public void initialize() {
        // set the missile to be invisible
        this.setVisibility(View.INVISIBLE);
        this.setDrawable(((Resources)(getResources().get(SpaceGame.RESOURCES))).getDrawable(R.drawable.);
        Point screenPt = (Point)this.getResources().get(SpaceGame.WINDOW_SIZE);
        // The width of missile will be 1 percent of the screen width
        width = screenPt.x / 100;
        // the height of missile will be 1/25 of the screen height
        height = screenPt.y / 25;

        speed = 60;
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


    public void setUp(boolean up) {
        this.up = up;
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
