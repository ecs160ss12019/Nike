package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public abstract class  AnimatedObject <View extends android.view.View>{
    SpaceGame spaceGame;
    PointF position;
    Size size;
    Handler mainHandler;
    Handler processHandler;

    ValueAnimator animator;
    View view;
    HashMap<String, Resources> resources;

    AnimatedObject(PointF position, Size size, ValueAnimator animator, View view, HashMap<String, Resources> resources, SpaceGame spaceGame,
                   Handler mainHandler, Handler processHandler){
        this.position=position;
        this.size=size;
        this.animator=animator;
        this.view = view;
        this.resources=resources;
        this.spaceGame=spaceGame;
        this.mainHandler=mainHandler;
        this.processHandler=processHandler;
    }

    public void setSpaceGame(SpaceGame spaceGame) {
        this.spaceGame = spaceGame;
    }

    public SpaceGame getSpaceGame() {
        return spaceGame;
    }

    public void setAnimator(ValueAnimator animater) {
        this.animator = animater;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ValueAnimator getAnimator() {
        return animator;
    }

    public View getView() {
        return view;
    }

    public void setResources(HashMap<String, Resources> resources) {
        this.resources = resources;
    }

    public HashMap<String, Resources> getResources() {
        return resources;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return size;
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

    static class Size{
        private int height;
        private int width;
        Size(int height,int width){
            this.height=height;
            this.width=width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }
    static class Actions{
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
