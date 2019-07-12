package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.util.Pair;
import android.widget.ImageView;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AnimatedObject {
    PointF position;

    ValueAnimator animator;
    ImageView imageView;
    HashMap<String, Resources> resources;

    AnimatedObject(PointF position,ValueAnimator animator,ImageView imageView, HashMap<String, Resources> resources){
        this.position=position;
        this.animator=animator;
        this.imageView=imageView;
        this.resources=resources;
    }

    public void setAnimator(ValueAnimator animater) {
        this.animator = animater;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ValueAnimator getAnimator() {
        return animator;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setResources(HashMap<String, Resources> resources) {
        this.resources = resources;
    }

    public HashMap<String, Resources> getResources() {
        return resources;
    }

    abstract protected void handle (Actions actions);

    /**
     * {@inheritDoc}
     *
     * This is just an example method to configure animatorUpdateListener
     * If the class doesn't need to use animator, you should just leave the body empty.
     * If you have multiple different listeners, you should make another configuration methods like this
     * to configure and initiate the listener and make comments to explain the function of the listener.
     * Don't do it using a anonymous inner class.
     */
    abstract ValueAnimator.AnimatorUpdateListener animatorListenerConfigure();

    class Actions{
        private PointF position;
        private HashMap<String,Pair<AnimatedObject,ArrayList<Float>>> actionSet;

        Actions(PointF position,HashMap<String,Pair<AnimatedObject,ArrayList<Float>>> actionSet){
            this.position=position;
            this.actionSet=actionSet;
        }

        public void setPosition(PointF position) {
            this.position = position;
        }

        public void setActionSet(HashMap<String, Pair<AnimatedObject, ArrayList<Float>>> actionSet) {
            this.actionSet = actionSet;
        }

        public PointF getPosition() {
            return position;
        }

        public HashMap<String, Pair<AnimatedObject, ArrayList<Float>>> getActionSet() {
            return actionSet;
        }
    }
}
