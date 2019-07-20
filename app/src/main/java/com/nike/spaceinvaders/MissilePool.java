package com.nike.spaceinvaders;

import android.os.Handler;
import android.util.SparseArray;
import android.view.ViewGroup;


import java.util.ArrayList;

public class MissilePool {
    private ViewGroup layout;
    private int numberOfMissile;
    private Handler mainHandler;
    private Handler processHandler;

    private SparseArray<MissileQuiver> freshMissiles;
    private SparseArray<MissileQuiver> gloriousMissiles;
    private ArrayList<MissileQuiver> excessiveMissiles;

    MissilePool(ViewGroup layout, int numberOfMissile, Handler mainHandler,Handler processHandler){
        this.layout=layout;
        this.mainHandler=mainHandler;
        this.processHandler=processHandler;
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
            if (missileArray.size()>numberOfMissile){
                int difference=missileArray.size()-numberOfMissile;
                for (int index=missileArray.size()-1;index>=missileArray.size()-difference;index--){
                    int key=missileArray.keyAt(index);
                    synchronized (excessiveMissiles){
                        excessiveMissiles.add(missileArray.get(key));
                    }
                    synchronized (missileArray){
                        missileArray.remove(key);
                    }
                }
            }else {
                int difference=numberOfMissile-missileArray.size();
                if (excessiveMissiles.size()>0){
                    synchronized (excessiveMissiles){
                        for (int index=excessiveMissiles.size()-1;index>=0&&index>=missileArray.size()-difference;index--){
                            MissileQuiver missileQuiver=excessiveMissiles.get(index);
                            if (missileQuiver.status){
                                synchronized (missileArray){
                                    missileArray.put(missileArray.size(),missileQuiver);
                                }
                            }else {
                                difference++;
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

        }
    }

    public Missile getMissile(){
        return null;
    }

    private class MissileQuiver {
        private boolean recyclable;
        private boolean status=true;
        private int index;
        private Missile missile;
        private MissilePool pool;

        MissileQuiver(int index,Missile missile,boolean recyclable,MissilePool pool){
            this.index=index;
            this.missile=missile;
            this.recyclable=recyclable;
            this.pool=pool;
        }

        public int getIndex() {
            return index;
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
    }

}
