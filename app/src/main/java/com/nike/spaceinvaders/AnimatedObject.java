package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Handler;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class  AnimatedObject <View extends android.view.View>{
    private SpaceGame spaceGame;
    private PointF position;
    private Size size;
    private Handler mainHandler;
    private Handler processHandler;

    private ValueAnimator animator;
    private View view;
    private HashMap<String, Object> resources;

    AnimatedObject(PointF position, Size size, ValueAnimator animator, View view, HashMap<String, Object> resources, SpaceGame spaceGame,
                   Handler mainHandler, Handler processHandler){
        this.position=position;
        this.size=size;
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

    public void setResources(HashMap<String, Object> resources) {
        this.resources = resources;
    }

    public HashMap<String, Object> getResources() {
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

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    public Handler getMainHandler() {
        return mainHandler;
    }

    public Handler getProcessHandler() {
        return processHandler;
    }

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
