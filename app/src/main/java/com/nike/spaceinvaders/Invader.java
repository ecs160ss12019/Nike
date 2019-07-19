package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PointF;
import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.os.Handler;

public class Invader extends AnimatedObject <ImageView> {
    private boolean status=true;
    private Missile missile;
    Invader(PointF position, Size size, ValueAnimator animator, ImageView view, HashMap<String, Object> resources, SpaceGame spaceGame, Handler mainHandler, Handler processHandler) {
        super(position, size, animator, view, resources, spaceGame, mainHandler, processHandler);

    }

    private void kill(Pair<AnimatedObject, ArrayList<Float>> strike){
        Missile missile= (Missile) strike.first;
        PointF position= missile.getPosition();
        Size size=missile.getSize();

        float groupX=strike.second.get(0);
        float groupY=strike.second.get(1);
        float realX=groupX+this.getView().getX();
        float realY=groupY+this.getView().getY();

        if (position.x>=realX&&position.x<=realX+this.getView().getWidth()
                &&position.y>=realY&&position.y<=realY+this.getView().getHeight()){
            this.status=false;
            this.getView().setImageAlpha(0);
        }
        notifySpaceGame();
    }

    private void notifySpaceGame(){

    }

    public boolean diagnose(){
        return this.status;
    }



    @Override
    protected void handle(Actions actions) {
        HashMap<String, Pair<AnimatedObject, ArrayList<Float>>> actionSet=actions.getActionSet();
        Set<String> keys=actionSet.keySet();
        for (String key: keys){
            Pair<AnimatedObject, ArrayList<Float>> value=actionSet.get(key);
            actionSet.entrySet();
            switch (key){
                case "strike":
                    assert value != null;
                    kill(value);
            }
        }
    }

    @Override
    protected void handle(Actions actions, Set keys) {

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
