package com.nike.spaceinvaders;

/*
- Manage a group of invaders
 */
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.widget.ImageView;

import java.util.ArrayList;



class InvaderGroup extends AnimatedObject<ConstraintLayout> {
    private int aliveInvaders;
    private ArrayList<Invader> invaders;
    private int duration = 30000;
    private int velocity = 200;
    private int horizontalTimes = 20;
    private PointF initialCoordinates;
    private int numCol = 5;

    private boolean hit[][];
    private SparseArray<SpaceGame.MutablePair<Integer, Integer>> hitStatus;

    private boolean detection = true;

    private int invaderWidth;
    private int invaderHeight;

    /* Invader Setting Flags */
    public final static int VELOCITY=0b1;



    InvaderGroup(ConstraintLayout view, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, Handler mainHandler, Handler processHandler,SoundEngine soundEngine) {

        super(null, view, resources, spaceGame, status, mainHandler, processHandler,soundEngine);

        this.aliveInvaders = this.getChildCount();
        invaders = new ArrayList<>(this.getChildCount());
        this.hit = new boolean[(int) Math.ceil(this.getChildCount() / (float) this.numCol)][this.numCol];
        this.hitStatus = new SparseArray<>();
        for (int i = 0; i < this.getChildCount(); i++) {
            ImageView invaderView = (ImageView) this.getChildAt(i);
            this.invaderWidth = invaderView.getWidth();
            this.invaderHeight = invaderView.getHeight();
            invaders.add(new Invader(i / this.numCol, i, this.getAnimator(), invaderView, resources, spaceGame, status, mainHandler, processHandler,soundEngine));
            hit[i / this.numCol][i % this.numCol] = true;
            SpaceGame.MutablePair<Integer, Integer> value = this.hitStatus.get(i / this.numCol);
            if (value == null) {
                value = new SpaceGame.MutablePair<>(this.numCol, null);
                this.hitStatus.put(i / this.numCol, value);
            } else {
                value.first = this.numCol;
            }
            SpaceGame.MutablePair<Integer, Integer> value2 = this.hitStatus.get(i % this.numCol);
            if (value2 == null) {
                value2 = new SpaceGame.MutablePair<>(null, (int) Math.ceil(this.getChildCount() / (float) this.numCol));
                this.hitStatus.put(i % this.numCol, value2);
            } else {
                value2.second = (int) Math.ceil(this.getChildCount() / (float) this.numCol);
            }
        }

        for(Invader invader: invaders)
            invader.setHitDetection(new NormalHitDetection());

        return;
    }

    @Override
    public void setSpaceGame(SpaceGame spaceGame) {
        super.setSpaceGame(spaceGame);
        for (Invader invader : invaders) {
            invader.setSpaceGame(getSpaceGame());
        }
    }

    @Override
    protected void handle(Actions actions) {
        super.handle(actions);
    }

    @Override
    protected void handle(Actions actions, Integer key) {
        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);
        switch (key) {
            case SpaceGame.GAME_START:
                if (this.getAnimator() == null) {
                    this.setAnimator(new ValueAnimator());
                    this.getAnimator().setIntValues(1, 100);
                    this.getAnimator().setDuration(this.duration);
                    this.getAnimator().setRepeatCount(ValueAnimator.INFINITE);
                    this.getAnimator().setRepeatMode(ValueAnimator.RESTART);
                    this.getAnimator().setInterpolator(null);
                    this.getAnimator().addUpdateListener(animatorListenerConfigure());
                    this.getAnimator().start();
                }

                break;
            case SpaceGame.STRIKE:
                strikeInvaders(actions, SpaceGame.STRIKE);
                break;
            case SpaceGame.HIT:
                Invader invader = (Invader) actions.get(SpaceGame.HIT).first;
                int index = invader.getIndex();
                this.hit[index / this.numCol][index % this.numCol] = false;
                this.hitStatus.get(index / this.numCol).first -= 1;
                this.hitStatus.get(index % this.numCol).second -= 1;
                this.setDetection(false);
                break;
            case SpaceGame.GAME_PAUSE:
                if (this.getAnimator()!=null&&this.getAnimator().isStarted()){
                    this.getAnimator().pause();
                }
                break;
            case SpaceGame.GAME_RESUME:
                if (this.getAnimator()!=null&&this.getAnimator().isStarted()){
                    this.getAnimator().resume();
                }
                break;

        }


    }

    public void setDetection(boolean detection) {
        this.detection = detection;
    }

    public boolean isDetection() {
        return detection;
    }

    private void strikeInvaders(Actions actions, Integer key) {

        for (Invader invader : invaders) {
            if (!this.detection) {
                break;
            }
            invader.handle(actions, key);
        }
        this.detection = true;
    }


    @Override
    public int getWidth() {
        int available = this.numCol;
        for (int index = 0; index < this.numCol; index++) {
            if (this.hitStatus.get(index).second <= 0 && (index == 0 || this.hitStatus.get(index - 1).second <= 0)) {
                available--;
            } else {
                break;
            }
        }
        for (int index = this.numCol - 1; index >= 0; index--) {
            if (this.hitStatus.get(index).second <= 0 && (index == this.numCol - 1 || this.hitStatus.get(index + 1).second <= 0)) {
                available--;
            } else {
                break;
            }
        }
        return (int) (super.getWidth() * (available / (float) this.numCol));
    }

    public int getDeltaX() {
        int available = this.numCol;
        for (int index = 0; index < this.numCol; index++) {
            if (this.hitStatus.get(index).second <= 0 && (index == 0 || this.hitStatus.get(index - 1).second <= 0)) {
                available--;
            } else {
                break;
            }
        }

        return this.invaderWidth * (this.numCol - available);
    }

    @Override
    public int getHeight() {
        int available = (int) Math.ceil(this.getChildCount() / this.numCol);
        for (int index = (int) Math.ceil(this.getChildCount() / this.numCol) - 1; index >= 0; index--) {
            if (this.hitStatus.get(index).first <= 0 && (index == Math.ceil(this.getChildCount() / this.numCol) - 1 || this.hitStatus.get(index + 1).first <= 0)) {
                available--;
            } else {
                break;
            }
        }
        return (int) (super.getHeight() * (available / Math.ceil(this.getChildCount() / this.numCol)));
    }

    private int getOriginalHeight() {
        return super.getHeight();
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        final InvaderGroup that = this;
        Actions actions = new Actions();
        actions.put(SpaceGame.STRIKE, new Pair<AnimatedObject, SparseArray<Float>>(that, null));
        return new ValueAnimator.AnimatorUpdateListener() {

            private int deltaDeltaX;
            private int deltaX;
            private int width;
            private int deltaWidth;
            private float variationStart;
            private float absVariationEnd;
            private boolean speedShift = false;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                //Set<Integer> newKeys = new ArraySet<>();

                if (initialCoordinates == null) {
                    initialCoordinates = new PointF(that.getAbsoluteX(), that.getAbsoluteY());
                }
                if (width == 0) {
                    width = that.getWidth();
                    deltaX = that.getDeltaX();
                }
                float fraction = animation.getAnimatedFraction();
                if (fraction == 1.0 || that.getHeight() + that.getY() >= that.getSpaceGame().laserBase.getY()) {
//                    animation.cancel();
//                    killLaserBase();

//                    return;
                }

                Point size = (Point) that.getResources().get(SpaceGame.WINDOW_SIZE);
                assert size != null;
                int deltaY = 100;
                int lengthY = size.y;
                float subFraction = 1f / that.horizontalTimes;
                int status = (int) (fraction / subFraction);
                float fractionX = (fraction % subFraction) * that.horizontalTimes;

                int tempDeltaX = getDeltaX();
                int tempWidth = getWidth();
                if (!this.speedShift && (tempDeltaX != deltaX || tempWidth != width)) {
                    variationStart = fractionX;
                    absVariationEnd = fraction + subFraction;
                    deltaDeltaX = tempDeltaX - deltaX;
                    deltaWidth = tempWidth - width;
                    this.speedShift = true;
                }
                if (fraction >= absVariationEnd || fractionX < variationStart) {
                    width = that.getWidth();
                    deltaX = that.getDeltaX();
                    this.speedShift = false;
                }
                if (this.speedShift) {
                    tempDeltaX = deltaX + (int) (deltaDeltaX * ((fractionX - variationStart) / (1 - variationStart)));
                    tempWidth = width + (int) (deltaWidth * ((fractionX - variationStart) / (1 - variationStart)));
                }
                int lengthX = size.x - (tempWidth);

                if (status % 2 == 0) {
                    that.setXRaw(-tempDeltaX + lengthX * fractionX);
                } else if (fraction != 1.0) {
                    that.setXRaw(-tempDeltaX + lengthX * (1 - fractionX));
                }

                that.setYRaw(that.initialCoordinates.y + lengthY * fraction);

                that.getSpaceGame().laserBase.handle(actions);


                for (Invader invader : invaders) {
                    if (invader.isAlive() && invader.toShoot()){
                        invader.shootMissile();}
                    if(invader.isAlive()){
                        Actions actions = new Actions();
                        actions.put(SpaceGame.CONTACT, new Pair<AnimatedObject, SparseArray<Float>>(invader, null));
                        invader.getSpaceGame().baseShelterGroup.handle(actions, SpaceGame.CONTACT);
                    }
                }
            }
        };
    }

    @Override
    public void setX(float x) {
        if (this.initialCoordinates != null) {
            this.initialCoordinates.x = x;
        }
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        if (this.initialCoordinates != null) {
            this.initialCoordinates.y = y;
        }
        super.setY(y);
    }

    public void setYRaw(float y) {
        super.setY(y);
    }

    public void setXRaw(float x) {
        super.setX(x);
    }

    @Override
    public void updateStatus(SpaceGame.Status status) {

    }
}
