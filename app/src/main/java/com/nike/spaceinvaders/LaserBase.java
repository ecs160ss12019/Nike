package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
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

/**
 * Developer Henry Yi
 */
class LaserBase extends AnimatedObject <ImageView>{
    private int velocity;
    private float delta= 10f;
    private boolean direction;


    LaserBase(  ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
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
    protected void handle(Actions actions, Set<Integer> keys) {
        if (this.getAnimator()==null){
            this.setAnimator(new ValueAnimator());
            this.getAnimator().setIntValues(1,100);
            this.getAnimator().setDuration(200);
            this.getAnimator().setRepeatMode(ValueAnimator.RESTART);
            this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
            this.getAnimator().setInterpolator(null);
            this.getAnimator().addUpdateListener(animatorListenerConfigure());
        }


        for (Integer key: keys) {
            Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);
//            SimpleMatrix a=new SimpleMatrix(new double[2][3]);
//            a.getDDRM();
//            Equation e=new Equation();
//            e.process();

//            e.alias();
            switch (key) {
                case SpaceGame.MOVE_LEFT:
                    this.direction=false;
                    this.getAnimator().start();
                    break;
                case SpaceGame.MOVE_RIGHT:
                    this.direction=true;
                    this.getAnimator().start();
                    break;
                case SpaceGame.FIRE:
//                    getStatus().put(SpaceGame.NUM_LIVES,new Pair<Float, Float>(4f,1f));
//                    this.getSpaceGame().hud.updateStatus(getStatus());
                    Actions actions1=new Actions();
                    actions1.put(SpaceGame.TEST,null);
                    this.getSpaceGame().baseShelterGroup.handle(actions1);
                    break;
                case SpaceGame.MOVE_STOP:
                    this.getAnimator().pause();
                    break;
            }
        }
    }




    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final AnimatedObject that=this;
        return new ValueAnimator.AnimatorUpdateListener() {
            private int times;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction=animation.getAnimatedFraction();
                float subFraction=0.005f;
                float remaining=fraction%subFraction;
                int times= (int) (fraction/subFraction);
                if (remaining>=0.0&&this.times==times){
                    return;
                }else if (remaining>=0.0){
                    this.times=times;
                }
                Point size= (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                assert size != null;
                if ((that.getX()<20&&!direction)||(that.getX()>size.x-that.getWidth()-20&&direction)){
                    return;
                }
                if (direction){
                    that.setX(that.getX()+((LaserBase) that).getDelta());
                }else {
                    that.setX(that.getX()-((LaserBase) that).getDelta());
                }
            }
        };
    }

    public float getDelta() {
        return delta;
    }
}
