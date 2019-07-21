package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class BaseShelterGroup extends AnimatedObject  <ConstraintLayout>  {

    private ArrayList<BaseShelter> shelters;

    BaseShelterGroup(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame,
                     SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super( null, view, resources, spaceGame,status, mainHandler, processHandler);

        shelters = new ArrayList<>(this.getChildCount());

        for (int i=0;i<this.getChildCount();i++){
            ImageView sheltersView= (ImageView) this.getChildAt(i);
            shelters.add(new BaseShelter(sheltersView, this.getResources(),
                    this.getSpaceGame(),this.getStatus(),
                    this.getMainHandler(),this.getProcessHandler()));
        }
    }


    @Override
    protected void handle(Actions actions, Set keys) {
        Set<Integer> oldKeys=actions.keySet();
        for (Integer key:oldKeys){
            switch (key){
                case SpaceGame.STRIKE:
                    ArrayList<Float> data = Objects.requireNonNull(actions.get(key)).second;
                    float x=data.get(0);
                    float y=data.get(1);

                    break;
            }
        }
    }


    private void strikeBaseShelter(){

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
