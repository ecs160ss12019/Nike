package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.os.Handler;


public class Invader extends AnimatedObject <ImageView> {
    private boolean status=true;
    private Missile missile;
    private int[][] hitbox;
    private int abstractionLevel=10;
    private int index;
    private int shootcd=100;

    Invader(int index,ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(animator, view, resources, spaceGame, status,mainHandler, processHandler);
        this.index=index;
    }

    private void kill(Actions actions,AnimatedObject missile){
        this.status=false;
        this.setVisibility(View.INVISIBLE);
        Set<Integer> keys=new ArraySet<>();
        keys.add(SpaceGame.MISSILE_GONE);
        keys.add(SpaceGame.HIT);
        actions.put(SpaceGame.MISSILE_GONE,null);
        actions.put(SpaceGame.HIT,new Pair<>(this,null));
        missile.handle(actions,keys);
        this.getSpaceGame().invaderGroup.handle(actions,keys);
        notifySpaceGame();
    }

    private void notifySpaceGame(){
        SpaceGame.Status status=getStatus();
        Pair<Float,Float> value=status.get(SpaceGame.SCORES);
        assert value != null;
        status.put(SpaceGame.SCORES,new Pair<>(value.first+10,null));
        getSpaceGame().updateStatus(status);
    }

    public boolean diagnose(){
        return this.status;
    }



    private boolean hitDetection(Actions actions,AnimatedObject missile){
        if (!status){
            return false;
        }
        float x=missile.getX();
        float y=missile.getY();
        int missileWidth=missile.getWidth();
        float left,top,bottom,right;
        left=this.getAbsoluteX()+50;
        top=this.getAbsoluteY();
        bottom=top+this.getHeight();
        right=left+this.getWidth()-50;
        if ((x>=left&&x<=right&&y<=bottom&&y>=top)||((x+missileWidth)>=left&&(x+missileWidth)<=right&&y<=bottom&&y>=top)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void handle(Actions actions, Set<Integer> keys) {
        //Log.d("invader handle","in handle");
        for (Integer key: keys){
            Pair<AnimatedObject, SparseArray<Float>> value=actions.get(key);

            switch (key){
                case SpaceGame.STRIKE:
                    assert value != null;
                    if (hitDetection(actions,value.first)){
                        kill(actions,value.first);
                    }
                case SpaceGame.INVADERS_ATTACK:
                    shootcd--;
                    //Log.d("current shootcd ",shootcd+"");
                    if(shootcd==0&&this.status){
                        shootcd=100;
                        shootMissile(actions);
                    }

            }
        }
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
            }
        };
    }

    protected void shootMissile(Actions actions){
        Log.d("in invader shoot missile","a invader should be shooting missile");
        AnimatedObject missile=getSpaceGame().missilePool.getMissile();
        SparseArray<Float> values=new SparseArray<>();
        values.put(SpaceGame.X_COORDINATE,(this.getWidth()-25)/2+this.getX());
        values.put(SpaceGame.Y_COORDINATE,(this.getY()));
        values.put(SpaceGame.MOVE_DIRECTION,1f);
        actions.put(SpaceGame.INVADERS_FIRE,new Pair<>(this,values));
        Set<Integer> newKeys=new ArraySet<>();
        newKeys.add(SpaceGame.INVADERS_FIRE);
        if(missile!=null){
            missile.handle(actions,newKeys);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
