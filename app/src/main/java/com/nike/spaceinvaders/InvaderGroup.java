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
import java.util.Set;


class InvaderGroup extends AnimatedObject<ConstraintLayout> {
    private int aliveInvaders;
    private ArrayList<Invader> invaders;
    private int duration = 30000;
    private float velocity =  0.000033f;
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
    public final static int RATE_OF_MISSILE=0b10;



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
            invader.setHitDetection(Invader.HIT_DETECTION,new NormalHitDetection());

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
    protected void initialize() {
        for(Invader invader: invaders)
            invader.initialize();
        AI.Evaluator evaluator= AI.getAI(SpaceGame.INVADER_GROUP,this.getStatus());
        assert evaluator != null;
        this.velocity=evaluator.evaluate(InvaderGroup.VELOCITY);
        Log.d("ValueVelocity", String.valueOf(this.velocity));
        this.duration= (int) (1/this.velocity);
        if (this.getAnimator()==null){
            this.setAnimator(new ValueAnimator());
            this.getAnimator().setInterpolator(null);
            this.getAnimator().addUpdateListener(animatorListenerConfigure());
        }
        if (this.getAnimator().isRunning()||this.getAnimator().isStarted()){
            this.getAnimator().end();
        }
        this.getAnimator().setIntValues(1, 100);
        Log.d("ValueDuration", String.valueOf(this.duration));
        this.getAnimator().setDuration(this.duration);
        this.getAnimator().start();
    }

    @Override
    protected void handle(Actions actions, Integer key) {
        Pair<AnimatedObject, SparseArray<Float>> value = actions.get(key);
        switch (key) {
            case SpaceGame.GAME_START:
                initialize();
                break;
            case SpaceGame.STRIKE:
                strikeInvaders(actions, SpaceGame.STRIKE);
                break;
            case SpaceGame.HIT://Callback from member invader that one of them was destroyed.
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

    private SpaceGame.Status updateStatusSelf(){
        SpaceGame.Status status=getStatus();
        if (whetherNextLevel()){

            Pair<Float,Float> level=status.get(SpaceGame.LEVEL);
            assert level != null;
            status.put(SpaceGame.LEVEL,new Pair<>(level.first+1,null));
        }
        return status;
    }

    private boolean whetherNextLevel(){
        int numOfAliveInvaders=0;
        for (int index=0;index<this.hitStatus.size();index++){
            numOfAliveInvaders+=this.hitStatus.get(index).first;
        }
        return numOfAliveInvaders <= 0;
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
                if (initialCoordinates == null) {
                    initialCoordinates = new PointF(that.getAbsoluteX(), that.getAbsoluteY());
                }

                if (width == 0) {
                    width = that.getWidth();
                    deltaX = that.getDeltaX();
                }
                float fraction = animation.getAnimatedFraction();
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

                for (Invader invader : invaders) {
                    if (invader.isAlive() && invader.toShoot()){
                        invader.shootMissile();}

                    if(invader.isAlive() && fraction > 0.1){
                        // When invader is alive and animation has already started
                        // The second checking is very important and may have bugs
                        if (checkInvaded(invader))
                        {
                            // Send a signal back to SpaceGame to end the game
                            notifyGameOver();
                            break;
                        }

                        Actions actions = new Actions();
                        actions.put(SpaceGame.CONTACT, new Pair<>(invader, null));
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
        Set<Integer> keys=status.keySet();
        Pair<Float,Float> newValue;
        Pair<Float,Float> oldValue;
        for (Integer key:keys) {
            switch (key) {
                case SpaceGame.NUM_INVADER:
                    newValue=status.get(key);
                    oldValue=this.getStatus().get(key);

                    assert oldValue != null;
                    assert newValue != null;
                    Log.d("InvaderValue", String.valueOf(newValue));
            }
        }
    }



    public boolean checkInvaded(Invader invader)
    {
         Point screenSize = (Point)this.getResources().get(SpaceGame.WINDOW_SIZE);
         float screenY = screenSize.y;

         if(invader.getAbsoluteY() >= screenY)
             return true;

         return false;
    }


    /*
    Notify the spaceGame that the game is over
     */
    public void notifyGameOver()
    {
        SpaceGame.Status newStatus = new SpaceGame.Status();
        newStatus.put(SpaceGame.GAME_OVER, new Pair<>(null, null));
        getSpaceGame().updateStatus(newStatus);
    }

}
