package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public abstract class  AnimatedObject <View extends android.view.View>{
    private SpaceGame spaceGame;
    private Handler mainHandler;
    private Handler processHandler;

    private SpaceGame.Status status;

    private ValueAnimator animator;
    private View view;
    private HashMap<Integer, Object> resources;

    AnimatedObject( ValueAnimator animator, View view, HashMap<Integer, Object> resources, SpaceGame spaceGame, SpaceGame.Status status,
                   Handler mainHandler, Handler processHandler){

        this.view = view;
        this.resources=resources;
        this.spaceGame=spaceGame;
        this.status=status;
        this.mainHandler=mainHandler;
        this.processHandler=processHandler;
    }

    public void setX(float x){
        this.view.setX(x);
    }

    public void setY(float y){
        this.view.setY(y);
    }

    public void setBitmap(Bitmap bitmap){
        if (this.view instanceof ImageView){
            ImageView imageView=(ImageView)this.view;
            imageView.setImageBitmap(bitmap);
        }
    }

    public void setDrawable(Drawable drawable){
        if (this.view instanceof ImageView){
            ImageView imageView=(ImageView)this.view;
            imageView.setImageDrawable(drawable);
        }
    }

    public int getChildCount(){
        if (this.view instanceof ViewGroup){
            ViewGroup viewGroup=(ViewGroup)this.view;
            return viewGroup.getChildCount();
        }else {
            return 0;
        }
    }

    public android.view.View getChildAt(int index){
        if (this.view instanceof ViewGroup){
            ViewGroup viewGroup=(ViewGroup)this.view;
            return viewGroup.getChildAt(index);
        }else {
            return null;
        }
    }

    public void setAlpha(float alpha){
        view.setAlpha(alpha);
    }

    public void setHeight(int height){
        view.layout((int)this.getX(),(int)this.getY(),(int)(this.getX()+this.getWidth()),(int)(this.getY()+height));

    }

    public void setWidth(int width){
        view.layout((int)this.getX(),(int)this.getY(),(int)(this.getX()+width),(int)(this.getY()+this.getHeight()));

    }

    public void setSize(int height,int width){
        view.layout((int)this.getX(),(int)this.getY(),(int)(this.getX()+width),(int)(this.getY()+height));
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

    public void setResources(HashMap<Integer, Object> resources) {
        this.resources = resources;
    }

    public HashMap<Integer, Object> getResources() {
        return resources;
    }

    protected void handle(Actions actions){
        handle(actions,null);
    }

    abstract protected void handle (Actions actions, Set keys);

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

    public Handler getMainHandler() {
        return mainHandler;
    }

    public Handler getProcessHandler() {
        return processHandler;
    }

    public SpaceGame.Status getStatus() {
        return status;
    }

    public void setStatus(SpaceGame.Status status) {
        this.status = status;
    }

    public float getX() {
        return this.view.getX();
    }

    public float getY() {
        return this.view.getY();
    }

    public int getWidth(){
        return this.view.getWidth();
    }

    public int getHeight(){
        return this.view.getHeight();
    }

    static class Size{
        private int height;
        private int width;
        Size(int height,int width){
            this.height=height;
            this.width=width;
        }

        private void setHeight(int height) {
            this.height = height;
        }

        private void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }
    static class Actions extends  HashMap<Integer,Pair<AnimatedObject,ArrayList<Float>>>{

    }
}
