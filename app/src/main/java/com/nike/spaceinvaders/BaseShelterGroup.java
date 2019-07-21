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
    private int numShelters;

    BaseShelterGroup(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame,
                     SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super( null, view, resources, spaceGame,status, mainHandler, processHandler);

        numShelters = this.getChildCount();
        shelters = new ArrayList<>(numShelters);

        for (int i = 0; i < numShelters; i++){
            ImageView sheltersView = (ImageView) this.getChildAt(i);
            shelters.add(new BaseShelter(sheltersView, this.getResources(),
                    this.getSpaceGame(),this.getStatus(),
                    this.getMainHandler(),this.getProcessHandler()));
        }
    }


    @Override
    protected void handle(Actions actions, Set keys) {
        Set<Integer> oldKeys = actions.keySet();
        for (Integer key:oldKeys){
            switch (key){
                case SpaceGame.STRIKE:
                    for(int i = 0; i < numShelters; i++) {
                        shelters.get(i).handle(actions, keys);
                    }

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
