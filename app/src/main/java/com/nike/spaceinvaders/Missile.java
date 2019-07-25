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
import android.util.ArraySet;
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
    protected void handle(Actions actions, Set<Integer> keys) {
        for(Integer key:keys){
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
                    // load the missile
//                    this.setAlpha(1);
                    this.setVisibility(View.VISIBLE);
                    // set starting x position


                    // set up trajectory for missile

                    // do we really need to check null?
                    if (this.getAnimator() == null){
                        this.setAnimator(new ValueAnimator());
                    }

                    this.getAnimator().setDuration(((long)(Math.abs(endY - startY) / speed)*1000));
                    this.getAnimator().start();
                    break;

                case SpaceGame.INVADERS_FIRE:
                    Log.d("in Missile's handle -> Invaders_fire","a missile is handling invader's shoot request");
                    // get the starting position of missile
                    SparseArray<Float> startPts1 = Objects.requireNonNull(actions.get(key)).second;
                    this.startX = startPts1.get(SpaceGame.X_COORDINATE);
                    this.startY = startPts1.get(SpaceGame.Y_COORDINATE);
                    this.up=startPts1.get(SpaceGame.MOVE_DIRECTION)!=1f;
                    float endY1 = findEndYPos();
                    // load the missile
//                    this.setAlpha(1);
                    this.setVisibility(View.VISIBLE);
                    // set starting x position

                    // do we really need to check null?
                    if (this.getAnimator() == null){
                        this.setAnimator(new ValueAnimator());
                    }

                    this.getAnimator().setDuration(((long)(Math.abs(endY1 - startY) / speed)*1000));
                    this.getAnimator().start();
                    break;

                case SpaceGame.STRIKE:
                    /*
                        Missile is moving and may hit an object
                     */
                    Actions newActions=new Actions();
                    Set<Integer> newKeys=new ArraySet<>();
                    newKeys.add(SpaceGame.STRIKE);
                    SparseArray<Float> coordinates=new SparseArray<>(2);
                    // Add the missile's absolute coordinates
                    coordinates.put(SpaceGame.X_COORDINATE,getAbsoluteX());
                    coordinates.put(SpaceGame.Y_COORDINATE,getAbsoluteY());
                    newActions.put(SpaceGame.STRIKE,
                            new Pair<AnimatedObject, SparseArray<Float>>(this,coordinates));
                    getSpaceGame().baseShelterGroup.handle(newActions,newKeys);
                    getSpaceGame().laserBase.handle(newActions,newKeys);
                    getSpaceGame().invaderGroup.handle(newActions,newKeys);
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
        final Actions actions=new Actions();
        final Set<Integer> newKeys=new ArraySet<>();
        newKeys.add(SpaceGame.STRIKE);
        actions.put(SpaceGame.STRIKE,new Pair<>(this,null));
        return animation -> {
            float fraction=animation.getAnimatedFraction();
            Point size= (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
            assert size != null;
            int lengthY= (int) (findEndYPos()-(((Missile) that).startY));
            that.setY(((Missile) that).startY+fraction*lengthY);
            that.setX(((Missile) that).startX);
            that.getSpaceGame().invaderGroup.handle(actions,newKeys);
            that.getSpaceGame().baseShelterGroup.handle(actions,newKeys);
            if(fraction==1.0){
                try {
                    ((Missile) that).recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public void initialize() {
        this.setVisibility(View.INVISIBLE);
        this.setX(-100);
        this.setY(-100);
//        this.setAlpha(0);
        if (this.getAnimator() == null){
            this.setAnimator(new ValueAnimator());
//            this.getAnimator().setInterpolator(null);
            this.getAnimator().addUpdateListener(animatorListenerConfigure());
        }else {
            this.getAnimator().cancel();
        }
        this.getAnimator().setFloatValues(startY, findEndYPos());
        speed = 600;
        // set the missile to be invisible

//        this.setX(0);
//        this.setY(0);
        this.setDrawable(((Resources)(getResources().get(SpaceGame.RESOURCES))).getDrawable(R.drawable.missile,null));
        Point screenPt = (Point)this.getResources().get(SpaceGame.WINDOW_SIZE);
        // The width of missile will be 1 percent of the screen width
        width = screenPt.x / 50;
        // the height of missile will be 1/25 of the screen height
        height = screenPt.y / 25;
        this.setSize((int)height,(int)width);

    }


    /*
        Find where the missile should end if there is no collision;
        It should end at the top of screen if it is going up,
        and end at the bottom of screen otherwise.
     */
    private float findEndYPos()
    {
        if(up)
            return -150;
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
