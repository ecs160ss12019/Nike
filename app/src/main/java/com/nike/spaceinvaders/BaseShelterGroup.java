package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class BaseShelterGroup extends AnimatedObject  <ConstraintLayout>  {
    BaseShelterGroup(ConstraintLayout view, HashMap<Integer, Object> resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super( null, view, resources, spaceGame,status, mainHandler, processHandler);
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
