package com.nike.spaceinvaders;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.security.Principal;
import java.util.ArrayList;

public class MissilePool {
    private ViewGroup layout;
    private int numberOfMissile;
    private Handler mainHandler;
    private Handler processHandler;
    private SpaceGame.Resources resources;
    private SpaceGame spaceGame;
    private SpaceGame.Status status;

    private SparseArray<MissileQuiver> freshMissiles;
    private SparseArray<MissileQuiver> gloriousMissiles;
    private ArrayList<MissileQuiver> excessiveMissiles;

    private int checkCount=0;

    MissilePool(ViewGroup layout, SpaceGame.Resources resources, SpaceGame spaceGame, SpaceGame.Status status, int numberOfMissile, Handler mainHandler, Handler processHandler){
        this.layout=layout;
        this.mainHandler=mainHandler;
        this.processHandler=processHandler;
        this.resources=resources;
        this.spaceGame=spaceGame;
        this.status=status;
        configureCapacity(numberOfMissile);
    }

    public void configureCapacity(int numberOfMissile){
        for (int k=0;k<2;k++){
            final SparseArray<MissileQuiver> missileArray;
            if (k==0){
                if (this.freshMissiles==null){
                    this.freshMissiles=new SparseArray<>(numberOfMissile);
                }
                missileArray=freshMissiles;
            }else {
                if (this.gloriousMissiles==null){
                    this.gloriousMissiles=new SparseArray<>(numberOfMissile);
                }
                missileArray=gloriousMissiles;
            }
            if (excessiveMissiles==null){
                excessiveMissiles=new ArrayList<>(numberOfMissile/2);
            }
            synchronized (missileArray){
                if (missileArray.size()>numberOfMissile){
                    int difference=missileArray.size()-numberOfMissile;
                    for (int index=missileArray.size()-1;index>=missileArray.size()-difference;index--){
                        int key=missileArray.keyAt(index);
                        synchronized (excessiveMissiles){
                            excessiveMissiles.add(missileArray.get(key));
                        }
                        missileArray.remove(key);
                    }
                }else {
                    int difference=numberOfMissile-missileArray.size();
                    synchronized (excessiveMissiles){
                        if (excessiveMissiles.size()>0){
                            for (int index=excessiveMissiles.size()-1;index>=0&&index>=missileArray.size()-difference;index--){
                                MissileQuiver missileQuiver=excessiveMissiles.get(index);
                                boolean status=k==0;
                                if (missileQuiver.status==status){
                                    missileQuiver.recyclable=true;
                                    missileQuiver.key=missileArray.size();
                                    missileArray.put(missileArray.size(),missileQuiver);
                                    excessiveMissiles.remove(index);
                                }else {
                                    difference++;
                                }
                            }
                        }
                    }
                }
            }

        }

        this.numberOfMissile=numberOfMissile;
    }

    private void recycle( MissileQuiver missileQuiver) throws Exception {
        if (missileQuiver.pool!=this){
            throw new Exception("This Missile is not in this pool");
        }
        if (!missileQuiver.recyclable){
            synchronized (this.excessiveMissiles){
                this.excessiveMissiles.remove(missileQuiver);
            }
        }else{
            missileQuiver.status=true;
            synchronized (this.gloriousMissiles){
                this.gloriousMissiles.remove(missileQuiver.key);
            }
            synchronized (this.freshMissiles){
                this.freshMissiles.put(missileQuiver.key,missileQuiver);
            }

        }
    }

    public MissileQuiver getMissile(){

        synchronized (freshMissiles){
            if (freshMissiles.size()>0){
                this.checkCount++;
                MissileQuiver missileQuiver=freshMissiles.get(freshMissiles.keyAt(freshMissiles.size()-1));
                missileQuiver.status=false;
                missileQuiver.time=System.currentTimeMillis();
                gc();
                return missileQuiver;
            }
        }
        synchronized (excessiveMissiles){
            Context context= (Context) this.resources.get(SpaceGame.CONTEXT);
            ImageView missileView=new ImageView(context);
            Missile missile=new Missile(missileView,resources,spaceGame,status,mainHandler,processHandler);
            MissileQuiver missileQuiver=new MissileQuiver(-1,missile ,false,this);
            excessiveMissiles.add(missileQuiver);
            missile.initialize();
            this.layout.addView(missileView);
            this.checkCount++;
            gc();
            return missileQuiver;
        }
    }

    private void gc(){
        if (checkCount>5){
            processHandler.post(new Runnable() {
                @Override
                public void run() {
                    checkCount=0;
                    synchronized (excessiveMissiles){
                        for (int index=excessiveMissiles.size()-1;index>=0;index--){
                            MissileQuiver missileQuiver=excessiveMissiles.get(index);
                            if (System.currentTimeMillis()-missileQuiver.time>20000){
                                missileQuiver.missile.initialize();
                                missileQuiver.missile.detachFrom(layout);
                                excessiveMissiles.remove(index);
                            }
                        }
                    }
                    synchronized (gloriousMissiles){
                        for (int index=gloriousMissiles.size()-1;index>=0;index--){
                            MissileQuiver missileQuiver=gloriousMissiles.get(index);
                            if (System.currentTimeMillis()-missileQuiver.time>20000){
                                missileQuiver.missile.initialize();
                                missileQuiver.missile.detachFrom(layout);
                                gloriousMissiles.remove(gloriousMissiles.keyAt(index));
                            }
                        }
                    }
                }
            });
        }

    }

    private class MissileQuiver {
        private long time;
        private boolean recyclable;
        private boolean status=true;
        private int key;
        private Missile missile;
        private MissilePool pool;

        MissileQuiver(int key, Missile missile, boolean recyclable, MissilePool pool){
            this.key = key;
            this.missile=missile;
            this.recyclable=recyclable;
            this.pool=pool;
            this.time=System.currentTimeMillis();
        }

        public int getKey() {
            return key;
        }

        public Missile getMissile() {
            return missile;
        }

        public void recycle() throws Exception {
            this.missile.initialize();
            this.pool.recycle(this);
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

}
