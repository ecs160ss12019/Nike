package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.ArraySet;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;


/**
 * This class is a manager class for the entire BaseShelters.
 * It holds and manages an {@link ConstraintLayout} object that holds the layout of the BaseShelters.
 * Also, it holds a {@link ArrayList<BaseShelter>} that holds the all BaseShelters.
 */
public class BaseShelterGroup extends AnimatedObject<ConstraintLayout> {

    private ArrayList<BaseShelter> shelters;
    private int numShelters;

    BaseShelterGroup(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame,
                     SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(null, view, resources, spaceGame, status, mainHandler, processHandler);

        numShelters = this.getChildCount();
        shelters = new ArrayList<>(numShelters);

        for (int i = 0; i < numShelters; i++) {
            ImageView sheltersView = (ImageView) this.getChildAt(i);
            shelters.add(new BaseShelter(i,sheltersView, this.getResources(),
                    this.getSpaceGame(),this.getStatus(),
                    this.getMainHandler(),this.getProcessHandler()));
        }
    }


    @Override
    protected void handle(Actions actions, Integer key) {

        switch (key) {
            case SpaceGame.STRIKE:
                strikeBaseShelter(actions, SpaceGame.STRIKE);
                break;

        }
    }


    @Override
    public void setSpaceGame(SpaceGame spaceGame) {
        super.setSpaceGame(spaceGame);
        for (BaseShelter baseShelter : shelters) {
            baseShelter.setSpaceGame(getSpaceGame());
        }
    }

    private void strikeBaseShelter(Actions actions, Integer key) {
        for (BaseShelter baseShelter : shelters) {
            baseShelter.handle(actions, key);
        }
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }
}
