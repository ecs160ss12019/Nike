package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.animation.BaseInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Developer Henry Yi & Xuanchen Zhou
 */
class InvaderGroup extends AnimatedObject  <ConstraintLayout> {
    private int aliveInvaders;
    private ArrayList<Invader> invaders;
    private int duration=30000;
    private int velocity=200;
    private int horizontalTimes=20;
    private PointF initialCoordinates;
    private int numCol=5;

    private boolean hit[][];
    private SparseArray<SpaceGame.MutablePair<Integer,Integer>> hitStatus;

    private boolean detection=true;

    private int invaderWidth;
    private int invaderHeight;

    InvaderGroup(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {

        super( null, view, resources, spaceGame,status, mainHandler, processHandler);

        this.aliveInvaders=this.getChildCount();
        invaders=new ArrayList<>(this.getChildCount());
        this.hit=new boolean[(int) Math.ceil(this.getChildCount() / (float)this.numCol)][ this.numCol];
        this.hitStatus=new SparseArray<>();
        for (int i=0;i<this.getChildCount();i++){
            ImageView invaderView= (ImageView) this.getChildAt(i);
            this.invaderWidth=invaderView.getWidth();
            this.invaderHeight=invaderView.getHeight();
            invaders.add(new Invader(i,this.getAnimator(),invaderView,resources,spaceGame,status,mainHandler,processHandler));
            hit[i/this.numCol][i%this.numCol]=true;
            SpaceGame.MutablePair<Integer,Integer> value=this.hitStatus.get(i/this.numCol);
            if (value==null){
                value=new SpaceGame.MutablePair<>(this.numCol,null);
                this.hitStatus.put(i/this.numCol,value);
            }else {
                value.first=this.numCol;
            }
            SpaceGame.MutablePair<Integer,Integer> value2=this.hitStatus.get(i%this.numCol);
            if (value2==null){
                value2=new SpaceGame.MutablePair<>(null,(int)Math.ceil(this.getChildCount()/(float)this.numCol));
                this.hitStatus.put(i%this.numCol,value2);
            }else {
                value2.second=(int)Math.ceil(this.getChildCount()/(float)this.numCol);
            }
        }

        return;
    }

    @Override
    public void setSpaceGame(SpaceGame spaceGame) {
        super.setSpaceGame(spaceGame);
        for (Invader invader:invaders){
            invader.setSpaceGame(getSpaceGame());
        }
    }

    @Override
    protected void handle(Actions actions) {
        super.handle(actions);
    }

    @Override
    protected void handle(Actions actions, Set<Integer> keys) {
        for (Integer key: keys){
            Pair<AnimatedObject, SparseArray<Float>> value=actions.get(key);
            switch (key){
                case SpaceGame.GAMESTART:
                    if (this.getAnimator()==null){
                        this.setAnimator(new ValueAnimator());
                        this.getAnimator().setIntValues(1,100);
                        this.getAnimator().setDuration(this.duration);
                        this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
                        this.getAnimator().setRepeatMode(ValueAnimator.RESTART);
                        this.getAnimator().setInterpolator(null);
                        this.getAnimator().addUpdateListener(animatorListenerConfigure());
                        this.getAnimator().start();
                    }

                    break;
                case SpaceGame.STRIKE:
                    Set<Integer> x=new ArraySet<>();
                    x.add(SpaceGame.STRIKE);
                    traverseInvaders(actions,x);
                    break;
                case SpaceGame.HIT:
                    Invader invader= (Invader) actions.get(SpaceGame.HIT).first;
                    int index=invader.getIndex();
                    this.hit[index/this.numCol][index%this.numCol]=false;
                    this.hitStatus.get(index/this.numCol).first-=1;
                    this.hitStatus.get(index%this.numCol).second-=1;
                    this.setDetection(false);
                    break;
            }
        }
    }

    public void setDetection(boolean detection) {
        this.detection = detection;
    }

    public boolean isDetection() {
        return detection;
    }

    private void traverseInvaders(Actions actions, Set<Integer> keys){

        for (Invader invader:invaders){
            if (!this.detection){
                break;
            }
            invader.handle(actions,keys);
        }
        this.detection=true;
    }




    @Override
    public int getWidth() {
        int available=this.numCol;
        for (int index=0;index<this.numCol;index++){
            if (this.hitStatus.get(index).second<=0&&(index==0||this.hitStatus.get(index-1).second<=0)){
                available--;
            }else {
                break;
            }
        }
        for (int index=this.numCol-1;index>=0;index--){
            if (this.hitStatus.get(index).second<=0&&(index==this.numCol-1||this.hitStatus.get(index+1).second<=0)){
                available--;
            }else {
                break;
            }
        }
        return (int) (super.getWidth()*(available/(float)this.numCol));
    }

    public int getDeltaX(){
        int available=this.numCol;
        for (int index=0;index<this.numCol;index++){
            if (this.hitStatus.get(index).second<=0&&(index==0||this.hitStatus.get(index-1).second<=0)){
                available--;
            }else {
                break;
            }
        }

        return this.invaderWidth*(this.numCol-available);
    }

    @Override
    public int getHeight() {
        int available= (int) Math.ceil(this.getChildCount()/this.numCol);
        for (int index=(int) Math.ceil(this.getChildCount()/this.numCol)-1;index>=0;index--){
            if (this.hitStatus.get(index).first<=0&&(index==Math.ceil(this.getChildCount()/this.numCol)-1||this.hitStatus.get(index+1).first<=0)){
                available--;
            }else {
                break;
            }
        }
        return (int) (super.getHeight()*(available/ Math.ceil(this.getChildCount()/this.numCol)));
    }

    private int getOriginalHeight(){
        return super.getHeight();
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final InvaderGroup that=this;
        Actions actions=new Actions();
        actions.put(SpaceGame.STRIKE,new Pair<AnimatedObject, SparseArray<Float>>(that,null));
        return new ValueAnimator.AnimatorUpdateListener() {

            private int deltaDeltaX;
            private int deltaX;
            private int width;
            private int deltaWidth;
            private float variationStart;
            private float absVariationEnd;
            private boolean speedShift=false;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Log.d("in onAnimationUpdate","InvaderGroup");
                Actions regularAction = new Actions();
                regularAction.put(SpaceGame.INVADERS_ATTACK,new Pair<AnimatedObject, SparseArray<Float>>(that,null));
                Set<Integer> newKeys=new ArraySet<>();
                newKeys.add(SpaceGame.INVADERS_ATTACK);
                traverseInvaders(regularAction,newKeys);

                if (initialCoordinates==null){
                    initialCoordinates=new PointF(that.getAbsoluteX(),that.getAbsoluteY());
                }
                if (width==0){
                    width=that.getWidth();
                    deltaX=that.getDeltaX();
                }
                float fraction=animation.getAnimatedFraction();
                if (fraction==1.0||that.getHeight()+that.getY()>=that.getSpaceGame().laserBase.getY()){
//                    animation.cancel();
//                    killLaserBase();

//                    return;
                }


                Point size= (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                assert size != null;
                int deltaY=100;
                int lengthY=size.y;
                float subFraction=1f/that.horizontalTimes;
                int status= (int) (fraction/subFraction);
                float fractionX=(fraction%subFraction)*that.horizontalTimes;

                int tempDeltaX=getDeltaX();
                int tempWidth=getWidth();
                if (!this.speedShift&&(tempDeltaX!=deltaX||tempWidth!=width)){
                    variationStart=fractionX;
                    absVariationEnd=fraction+subFraction;
                    deltaDeltaX=tempDeltaX-deltaX;
                    deltaWidth=tempWidth-width;
                    this.speedShift=true;
                }
                if (fraction>=absVariationEnd||fractionX<variationStart){
                    width=that.getWidth();
                    deltaX=that.getDeltaX();
                    this.speedShift=false;
                }
                if (this.speedShift){
                    tempDeltaX= deltaX+(int) (deltaDeltaX*((fractionX-variationStart)/(1-variationStart)));
                    tempWidth=width+(int) (deltaWidth*((fractionX-variationStart)/(1-variationStart)));
                }
                int lengthX=size.x-(tempWidth);

                if(status%2==0){
                    that.setXRaw(-tempDeltaX+lengthX*fractionX);
                }else if (fraction!=1.0){
                    that.setXRaw(-tempDeltaX+lengthX*(1-fractionX));
                }

                that.setYRaw(that.initialCoordinates.y+lengthY*fraction);

                that.getSpaceGame().laserBase.handle(actions);


            }
        };
    }

    private void killLaserBase(){
        Pair<Float,Float> value=getStatus().get(SpaceGame.NUM_LIVES);
        assert value != null;
        getStatus().put(SpaceGame.NUM_LIVES,new Pair<>(value.first-1,null));
        this.getSpaceGame().updateStatus(getStatus());
    }

    @Override
    public void setX(float x) {
        if (this.initialCoordinates!=null){
            this.initialCoordinates.x=x;
        }
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        if (this.initialCoordinates!=null){
            this.initialCoordinates.y=y;
        }
        super.setY(y);
    }

    public void setYRaw(float y) {
        super.setY(y);
    }

    public void setXRaw(float x) {
        super.setX(x);
    }
}
