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

    private SparseArray<Missile> freshMissiles;
    private SparseArray<Missile> gloriousMissiles;
    private ArrayList<Missile> excessiveMissiles;

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
            final SparseArray<Missile> missileArray;
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
                                Missile missile=excessiveMissiles.get(index);
                                boolean status=k==0;
                                if (missile.isStatus()==status){
                                    missile.setRecyclable(true);
                                    missile.setKey(missileArray.size());
                                    missileArray.put(missileArray.size(),missile);
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

    public void recycle( Missile missile) throws Exception {
        if (missile.getPool()!=this){
            throw new Exception("This Missile is not in this pool");
        }
        if (!missile.isRecyclable()){
            synchronized (this.excessiveMissiles){
                this.excessiveMissiles.remove(missile);
            }
        }else{
            missile.setStatus(true);
            synchronized (this.gloriousMissiles){
                this.gloriousMissiles.remove(missile.getKey());
            }
            synchronized (this.freshMissiles){
                this.freshMissiles.put(missile.getKey(),missile);
            }

        }
    }

    public Missile getMissile(){

        synchronized (freshMissiles){
            if (freshMissiles.size()>0){
                this.checkCount++;
                Missile missile=freshMissiles.get(freshMissiles.keyAt(freshMissiles.size()-1));
                missile.setStatus(false);
                missile.setTime(System.currentTimeMillis());
                gc();
                return missile;
            }
        }
        synchronized (excessiveMissiles){
            Context context= (Context) this.resources.get(SpaceGame.CONTEXT);
            ImageView missileView=new ImageView(context);
            Missile missile=new Missile(-1,false,this,missileView,resources,spaceGame,status,mainHandler,processHandler);
            excessiveMissiles.add(missile);
            missile.initialize();
            this.layout.addView(missileView);
            this.checkCount++;
            gc();
            return missile;
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
                            Missile missile=excessiveMissiles.get(index);
                            if (System.currentTimeMillis()-missile.getTime()>20000){
                                missile.initialize();
                                missile.detachFrom(layout);
                                excessiveMissiles.remove(index);
                            }
                        }
                    }
                    synchronized (gloriousMissiles){
                        for (int index=gloriousMissiles.size()-1;index>=0;index--){
                            Missile missile=gloriousMissiles.get(index);
                            if (System.currentTimeMillis()-missile.getTime()>20000){
                                missile.initialize();
                                missile.detachFrom(layout);
                                gloriousMissiles.remove(gloriousMissiles.keyAt(index));
                            }
                        }
                    }
                }
            });
        }

    }

}
