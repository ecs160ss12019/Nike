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
    private boolean alive = true;
    private int key;
    private MissilePool pool;

    // whether the missile is moving up or down
    private boolean up;

    private float speed;

    private float startX;
    private float startY;


    Missile(ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super( new ValueAnimator(), view, resources, spaceGame, status, mainHandler, processHandler);
    }


    public Missile initKey(int key)
    {
        this.key = key;
        return this;
    }

    public Missile initRecyclable(boolean recyclable)
    {
        this.recyclable = recyclable;
        return this;
    }

    public Missile initPool(MissilePool pool)
    {
        this.pool = pool;
        return this;
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
     * @param key
     */
    @Override
    protected void handle(Actions actions, Integer key) {
        switch(key){
            case SpaceGame.FIRE:
                /*
                       Missile has just been fired
                    */

                //Plays sound effect for missile
                SoundEngine.playMissile();

                // get the starting position of missile
                SparseArray<Float> startPts = Objects.requireNonNull(actions.get(key)).second;
                this.startX = startPts.get(SpaceGame.X_COORDINATE);
                this.startY = startPts.get(SpaceGame.Y_COORDINATE);
                this.up = startPts.get(SpaceGame.MOVE_DIRECTION)==1f;
                float endY = findEndYPos();

                // load the missile
                this.setVisibility(View.VISIBLE);

                // do we really need to check null?
                if (this.getAnimator() == null){
                    this.setAnimator(new ValueAnimator());
                }

                this.getAnimator().setDuration(((long)(Math.abs(endY - startY) / speed)*1000));
                this.getAnimator().start();
                break;
                case SpaceGame.STRIKE:/*
                       One Missile strikes another missile
                    */

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

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final AnimatedObject that=this;
        final Actions actions=new Actions();
        ;
        actions.put(SpaceGame.STRIKE,new Pair<>(this,null));
        return animation -> {
            float fraction=animation.getAnimatedFraction();
            Point size= (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
            assert size != null;
            int lengthY= (int) (findEndYPos()-(((Missile) that).startY));
            that.setY(((Missile) that).startY+fraction*lengthY);
            that.setX(((Missile) that).startX);
            if(up)
                that.getSpaceGame().invaderGroup.handle(actions,SpaceGame.STRIKE);
            else
                that.getSpaceGame().laserBase.handle(actions, SpaceGame.STRIKE);
            that.getSpaceGame().baseShelterGroup.handle(actions, SpaceGame.STRIKE);
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
        float width = screenPt.x / 50;
        // the height of missile will be 1/25 of the screen height
        float height = screenPt.y / 25;
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

    public boolean isAlive() {
        return alive;
    }

    public void setAliveStatus(boolean status) {
        this.alive = status;
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
