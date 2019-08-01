package com.nike.spaceinvaders;

/*
- Manage a group of baseShelters
- Get the action signal from the outside world and then distribute it to every baseShelter
 */

import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.SparseArray;
import android.widget.ImageView;

import java.util.ArrayList;



/**
 * This class is a manager class for the entire BaseShelters.
 * It holds and manages an {@link ConstraintLayout} object that holds the layout of the BaseShelters.
 * Also, it holds a {@link ArrayList<BaseShelter>} that holds the all BaseShelters.
 */
public class BaseShelterGroup extends AnimatedObject<ConstraintLayout> {

    private ArrayList<BaseShelter> shelters;
    private int numShelters;

    BaseShelterGroup(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame,
                     SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(null, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);

        numShelters = this.getChildCount();
        shelters = new ArrayList<>(numShelters);

        for (int i = 0; i < numShelters; i++) {
            ImageView sheltersView = (ImageView) this.getChildAt(i);
            shelters.add(new BaseShelter(sheltersView, this.getResources(),
                    this.getSpaceGame(), this.getStatus(),
                    this.getMainHandler(), this.getProcessHandler(),this.getSoundEngine()));
        }
    }


    @Override
    protected void handle(Actions actions, Integer key) {

        switch (key) {
            case SpaceGame.STRIKE:
                traverseBaseShelter(actions, SpaceGame.STRIKE);
                break;

            case SpaceGame.CONTACT:
                traverseBaseShelter(actions, SpaceGame.CONTACT);
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

    private void traverseBaseShelter(Actions actions, Integer key) {
        for (BaseShelter baseShelter : shelters) {
            if(baseShelter.isAlive())
                baseShelter.handle(actions, key);
        }
    }

    @Override
    public void setHitDetection(SparseArray<HitDetection> hitDetection) {
        for (BaseShelter baseShelter : shelters) {
            baseShelter.setHitDetection(hitDetection);
        }
    }

    @Override
    public void setHitDetection(int key,HitDetection hitDetection) {
        for (BaseShelter baseShelter : shelters) {
            baseShelter.setHitDetection(key,hitDetection);
        }
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }

    @Override
    public void updateStatus(SpaceGame.Status status) {

    }
}
