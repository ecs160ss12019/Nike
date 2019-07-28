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
import java.util.Random;
import java.util.Set;

import android.os.Handler;


public class MysteryInvader extends AnimatedObject<ImageView> {
    public boolean alive = true;
    private boolean status = true;
    private int[][] hitbox;
    private int index;
    private int appearcd;
    private Random rand;

    MysteryInvader(int index, ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {
        super(animator, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);
        this.index = index;
        rand = new Random();
        appearcd = 50 + rand.nextInt(1000);
    }

    private void kill(Actions actions, AnimatedObject missile) {
        alive = false;
        this.status = false;
        this.setVisibility(View.INVISIBLE);
        Set<Integer> keys = new ArraySet<>();
        actions.put(SpaceGame.MISSILE_GONE, null);
        actions.put(SpaceGame.HIT, new Pair<>(this, null));
        missile.handle(actions, SpaceGame.MISSILE_GONE);
        this.getSpaceGame().invaderGroup.handle(actions, SpaceGame.HIT);
        notifySpaceGame();
    }

    private void notifySpaceGame() {
        SpaceGame.Status status = getStatus();
        Pair<Float, Float> value = status.get(SpaceGame.SCORES);
        assert value != null;
        status.put(SpaceGame.SCORES, new Pair<>(value.first + 10, null));
        getSpaceGame().updateStatus(status);
    }

    private boolean hitDetection(Actions actions, AnimatedObject missile) {
        if (!status) {
            return false;
        }
        float x = missile.getX();
        float y = missile.getY();
        int missileWidth = missile.getWidth();
        float left, top, bottom, right;
        left = this.getAbsoluteX() + 50;
        top = this.getAbsoluteY();
        bottom = top + this.getHeight();
        right = left + this.getWidth() - 50;
        if ((x >= left && x <= right && y <= bottom && y >= top) || ((x + missileWidth) >= left && (x + missileWidth) <= right && y <= bottom && y >= top)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void handle(Actions actions, Integer key) {
        //Log.d("invader handle","in handle");

        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);

        switch (key) {
            case SpaceGame.STRIKE:
                assert value != null;
                if (hitDetection(actions, value.first)) {
                    kill(actions, value.first);
                }

        }

    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
            }
        };
    }
}
