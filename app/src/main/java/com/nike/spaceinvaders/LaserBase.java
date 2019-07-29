package com.nike.spaceinvaders;

/*
- Able to move right or left when user touches the left/right part of the screen
- Has 3 lives
- Able to shoot missle when user touch the middle screen on the screen
 */
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.os.Handler;

import org.ejml.data.Matrix;
import org.ejml.equation.Equation;
import org.ejml.simple.SimpleMatrix;


class LaserBase extends AnimatedObject<ImageView> {
    private int velocity=200;
    private float delta = 10f;
    private boolean direction;
    private boolean running=true;
//    private Missile lastMissile;

    LaserBase(ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(null, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);

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
    protected void handle(Actions actions, Integer key) {
        if (this.getAnimator() == null) {
            this.setAnimator(new ValueAnimator());
            this.getAnimator().setIntValues(1, 100);
            this.getAnimator().setRepeatMode(ValueAnimator.RESTART);
            this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
            this.getAnimator().setInterpolator(null);
            this.getAnimator().addUpdateListener(animatorListenerConfigure());
        }
        this.getAnimator().setDuration((1/this.velocity)*40000);
        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);

        switch (key) {
            case SpaceGame.MOVE_LEFT:
                if (!this.running){
                    return;
                }
                this.direction = false;
                this.velocity= (int) (value.second.get(SpaceGame.GRAVITY)*1000);
                this.getAnimator().setDuration((1/this.velocity)*40000);
                this.getAnimator().start();
                break;
            case SpaceGame.MOVE_RIGHT:
                if (!this.running){
                    return;
                }
                this.direction = true;
                this.velocity= (int) (value.second.get(SpaceGame.GRAVITY)*1000);
                this.getAnimator().setDuration((1/this.velocity)*40000);
                this.getAnimator().start();
                break;
            case SpaceGame.FIRE:
                if (!this.running){
                    return;
                }
//                if(lastMissile != null && lastMissile.isAlive())
//                    return;
                AnimatedObject missile = getSpaceGame().missilePool.getMissile();
                SparseArray<Float> values = new SparseArray<>();
                values.put(SpaceGame.X_COORDINATE, (this.getWidth() - 25) / 2 + this.getX());
                values.put(SpaceGame.Y_COORDINATE, (this.getY()));
                values.put(SpaceGame.MOVE_DIRECTION, 1f);
                actions.put(SpaceGame.FIRE, new Pair<>(this, values));
                if (missile != null) {
//                    lastMissile = (Missile) missile;
                    ((Missile) missile).setMissileForm(new LaserBaseMissileForm
                            ((Context) this.getResources().get(SpaceGame.CONTEXT)));
                    missile.handle(actions, SpaceGame.FIRE);
                }

                break;
            case SpaceGame.MOVE_STOP:
                this.getAnimator().pause();
                break;

            case SpaceGame.STRIKE:
                assert value != null;
                if (hitDetection(actions, value.first)) {
                    kill(actions, value.first);
                }
                break;
            case SpaceGame.GAME_PAUSE:
                this.running=false;
                if (this.getAnimator()!=null&&this.getAnimator().isStarted()){
                    this.getAnimator().pause();
                }
                break;
            case SpaceGame.GAME_RESUME:
                this.running=true;
                break;

        }

    }


    private boolean hitDetection(Actions actions, AnimatedObject missile) {
        // get missile's location
        float x = missile.getX();
        float y = missile.getY();

        int missileWidth = missile.getWidth();

        float left, top, bottom, right;
        left = this.getAbsoluteX() + 50;
        top = this.getAbsoluteY();
        bottom = top + this.getHeight();
        right = left + this.getWidth() - 50;
        if ((x >= left && x <= right && y <= bottom && y >= top) || ((x + missileWidth) >= left && (x + missileWidth) <= right && y <= bottom && y >= top)) {
            return true;
        } else {
            return false;
        }
    }


    private void kill(Actions actions, AnimatedObject missile) {
        this.setVisibility(View.INVISIBLE);
        actions.put(SpaceGame.MISSILE_GONE, null);
        missile.handle(actions, SpaceGame.MISSILE_GONE);
        notifySpaceGame();
    }

    /*
    Notify the spaceGame to substitute a new laserBase if possible
    */
    public void notifySpaceGame() {
        SpaceGame.Status status = getStatus();
        Pair<Float, Float> value = status.get(SpaceGame.NUM_LIVES);
        assert value != null;

        // Now laserBase loses one life
        Float numLives = value.first - 1;

        status.put(SpaceGame.NUM_LIVES, new Pair<>(numLives, null));
        getSpaceGame().updateStatus(status);
    }


    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final AnimatedObject that = this;
        return new ValueAnimator.AnimatorUpdateListener() {
            private int times;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                float subFraction = 0.005f;
                float remaining = fraction % subFraction;
                int times = (int) (fraction / subFraction);
                if (remaining >= 0.0 && this.times == times) {
                    return;
                } else if (remaining >= 0.0) {
                    this.times = times;
                }
                Point size = (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                assert size != null;
                if ((that.getX() < 20 && !direction) || (that.getX() > size.x - that.getWidth() - 20 && direction)) {
                    return;
                }
                if (direction) {
                    that.setX(that.getX() + ((LaserBase) that).getDelta());
                } else {
                    that.setX(that.getX() - ((LaserBase) that).getDelta());
                }
            }
        };
    }


    public void spawn() {
        this.setVisibility(View.VISIBLE);
        return;
    }


    public float getDelta() {
        return delta;
    }

    @Override
    public float evaluate(float value) {
        return 0;
    }
}
