package com.nike.spaceinvaders;


/*
- Base class of every animated object created in the game that holds a view of
    object on the game screen and the SpaceGame object
- Stores and manages view, animator, and handlers
- Handle notification issued by other objects
- Configure the animator listener
 */



import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Set;


/**
 * This class is an abstract class that is extended by all the AnimatedObjects in this game.
 * It holds and manages a view object, the SpaceGame, the Threads handlers, ValueAnimator, Status of the game,
 * Resources, and parenntCoordinates;
 */
public abstract class  AnimatedObject <View extends android.view.View> implements StatusManager{
    private SpaceGame spaceGame;
    private Handler mainHandler;
    private Handler processHandler;

    private SpaceGame.Status status;

    private ValueAnimator animator;
    private View view;
    private SpaceGame.Resources resources;

    private PointF parentCoordinates;
    private SoundEngine soundEngine;

    private AI.Evaluator evaluator;

    private int[] hitBox;

    private SparseArray<HitDetection> hitDetection;

    private boolean isAlive=true;

    AnimatedObject( ValueAnimator animator, View view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status,
                   Handler mainHandler, Handler processHandler,SoundEngine soundEngine){

        this.view = view;
        this.resources=resources;
        this.spaceGame=spaceGame;
        this.status=status;
        this.mainHandler=mainHandler;
        this.processHandler=processHandler;
        this.soundEngine=soundEngine;
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

    public Drawable getDrawable(){
        if (this.view instanceof ImageView){
            ImageView imageView=(ImageView)this.view;
            return imageView.getDrawable();
        }else {
            return null;
        }
    }

    public void setDrawable(Drawable drawable){
        if (this.view instanceof ImageView){
            ImageView imageView=(ImageView)this.view;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
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

    public void setVisibility( int visibility){
        view.setVisibility(visibility);
    }

    public void setAlpha(float alpha){
        view.setAlpha(alpha);
    }

    public void setHeight(int height){
        ViewGroup.LayoutParams layoutParams=this.view.getLayoutParams();
        if (layoutParams!=null){
            layoutParams.height=height;
            this.view.requestLayout();
        }

    }

    public void setWidth(int width){
        ViewGroup.LayoutParams layoutParams=this.view.getLayoutParams();
        if (layoutParams!=null){
            layoutParams.width=width;
            this.view.requestLayout();
        }
    }

    public void setSize(int height,int width){
        ViewGroup.LayoutParams layoutParams=this.view.getLayoutParams();
        if (layoutParams!=null){
            layoutParams.width=width;
            layoutParams.height=height;
            this.view.requestLayout();
        }
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

    public void setResources(SpaceGame.Resources resources) {
        this.resources = resources;
    }

    public SpaceGame.Resources getResources() {
        return resources;
    }

    protected void handle(Actions actions){
        Set<Integer> keys=null;
        if (actions!=null){
            keys=actions.keySet();
        }
        for (Integer key: keys)
            handle(actions,key);
    }

    abstract protected void initialize();

    abstract protected void handle (Actions actions, Integer key);


    protected boolean hitDetection(AnimatedObject hittingObject) {
        float x = hittingObject.getAbsoluteX();
        float y = hittingObject.getAbsoluteY();

        int hittingObjectWidth = hittingObject.getWidth();
        float left, top, bottom, right;
        float padding = 20;
        left = this.getAbsoluteX() + padding;
        top = this.getAbsoluteY();
        bottom = top + this.getHeight();
        right = left + this.getWidth() - padding;
        if ((x >= left && x <= right && y <= bottom && y >= top) || ((x + hittingObjectWidth) >= left && (x + hittingObjectWidth) <= right && y <= bottom && y >= top)) {
            return true;
        } else {
            return false;
        }
    }

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

    /*
    Get the relative X coordinate to its parent view and/or its initial location
    if the "View" has been moved
     */
    public float getX() {
        return this.view.getX();
    }

    /*
    Get the relative Y coordinate to its parent view and/or its initial location
    if the "View" has been moved
    */
    public float getY() {
        return this.view.getY();
    }


    /*
    Get the absolute X coordinate on the screen,
    given its relative X pos
     */
    public float getAbsoluteX(){
        android.view.View parentView = (android.view.View) this.view.getParent();
        float parentX=0,parentY=0;
        if (parentView!=null){
            parentX=parentView.getX();
            parentY=parentView.getY();
        }
        if (this.parentCoordinates==null){
            this.parentCoordinates=new PointF(parentX,parentY);
        }else {
            this.parentCoordinates.x=parentX;
        }
        return this.getX() + this.parentCoordinates.x;
    }

    /*
    Get the absolute Y coordinate on the screen
     */
    public float getAbsoluteY(){
        android.view.View parentView = (android.view.View) this.view.getParent();
        float parentX=0,parentY=0;
        if (parentView!=null){
            parentX=parentView.getX();
            parentY=parentView.getY();
        }
        if (this.parentCoordinates==null){

            this.parentCoordinates=new PointF(parentX,parentY);
        }else {
            this.parentCoordinates.y=parentY;
        }
        return this.getY() + this.parentCoordinates.y;
    }

    /*
    Get the relative X coordinate of its image, given its
    absolute X on the screen
     */
    public float getRelativeX(float absX){
        if (this.parentCoordinates==null){
            android.view.View parentView = (android.view.View) this.view.getParent();
            this.parentCoordinates=new PointF(parentView.getX(),parentView.getY());
            Log.d("parentCoordinate",this.parentCoordinates.toString());
        }
        return absX - this.parentCoordinates.x;
    }


    /*
    Get the relative Y coordinate of its image, given its
    absolute Y on the screen
     */
    public float getRelativeY(float absY){
        if (this.parentCoordinates==null){
            android.view.View parentView = (android.view.View) this.view.getParent();
            this.parentCoordinates=new PointF(parentView.getX(),parentView.getY());
        }
        return absY - this.parentCoordinates.y;
    }


    public int getWidth(){
        return this.view.getWidth();
    }

    public int getHeight(){
        return this.view.getHeight();
    }

    public void addView(android.view.View view){
        if (this.view instanceof ViewGroup){
            ViewGroup viewGroup=(ViewGroup) this.view;
            viewGroup.addView(view);
        }
    }

    public void removeView(android.view.View view){
        if (this.view instanceof ViewGroup){
            ViewGroup viewGroup=(ViewGroup) this.view;
            viewGroup.removeView(view);
        }
    }

    public void attachTo(ViewGroup layout){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.getWidth(),this.getHeight());
        layout.addView(this.view,layoutParams);
    }

    public void detachFrom(ViewGroup layout){
        layout.removeView(this.view);
    }

    public SoundEngine getSoundEngine() {
        return soundEngine;
    }

    public void setSoundEngine(SoundEngine soundEngine) {
        this.soundEngine = soundEngine;
    }

    public AI.Evaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(AI.Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public int[] getHitBox() {
        return hitBox;
    }

    public void setHitBox(int[] hitBox) {
        this.hitBox = hitBox;
    }

    public SparseArray<HitDetection> getHitDetection() {
        return hitDetection;
    }

    public void setHitDetection(SparseArray<HitDetection> hitDetection) {
        this.hitDetection = hitDetection;
    }

    public void setHitDetection(int key,HitDetection hitDetection) {
        if (this.hitDetection!=null){
            this.hitDetection.put(key,hitDetection);
        }else {
            this.hitDetection=new SparseArray<>();
            this.hitDetection.put(key,hitDetection);
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
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



    static class Actions extends  HashMap<Integer,Pair<AnimatedObject, SparseArray<Float>>>{

    }
}
