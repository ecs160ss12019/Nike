package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Point;
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


public class MysteryInvader extends Invader {
    public boolean alive = true;
    private boolean status = true;
    private boolean direction;
    private boolean isAppearing;
    private Random rand;
    private int appearcd;
    private int award;


    MysteryInvader(int index, ValueAnimator animator, ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(index, animator, view, resources, spaceGame, status, mainHandler, processHandler);
        appearcd = rand.nextInt(1000);
        award = 100;
        direction = rand.nextBoolean();
        isAppearing = rand.nextBoolean();
    }
}

