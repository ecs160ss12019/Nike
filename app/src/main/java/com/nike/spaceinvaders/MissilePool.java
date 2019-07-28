package com.nike.spaceinvaders;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;

public class MissilePool {
    private ViewGroup layout;
    private int numberOfMissile;
    private Handler mainHandler;
    private Handler processHandler;
    private SpaceGame.Resources resources;
    private SpaceGame spaceGame;
    private SpaceGame.Status status;
    private SoundEngine soundEngine;

  // Missiles that are available
    private SparseArray<Missile> freshMissiles;
  // Missiles that are now used by others
    private SparseArray<Missile> gloriousMissiles;
    private ArrayList<Missile> excessiveMissiles;

    private int checkCount=0;
    private boolean availability=false;

    MissilePool(ViewGroup layout, SpaceGame.Resources resources, SpaceGame spaceGame,
                SpaceGame.Status status, int numberOfMissile, Handler mainHandler,
                Handler processHandler,SoundEngine soundEngine) {
        this.layout = layout;
        this.mainHandler = mainHandler;
        this.processHandler = processHandler;
        this.resources = resources;
        this.spaceGame = spaceGame;
        this.status = status;
        this.numberOfMissile = numberOfMissile;
        this.soundEngine=soundEngine;
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
                    int count=0;
                    for (int index=this.numberOfMissile-1;index>=0;index--){
                        if (count>=difference){
                            break;
                        }
                        int key=missileArray.keyAt(index);
                        Missile missile=missileArray.get(key);
                        if (missile==null){
                            continue;
                        }
                        synchronized (excessiveMissiles){
                            missile.setRecyclable(false);
                            missile.setKey(-1);
                            excessiveMissiles.add(missile);
                        }
                        missileArray.remove(key);
                        count++;
                    }
                }else {
                    int difference=numberOfMissile-missileArray.size();
                    synchronized (excessiveMissiles){
                        if (excessiveMissiles.size()>0){
                            for (int index=excessiveMissiles.size()-1;index>=0&&index>=missileArray.size()-difference;index--){
                                Missile missile=excessiveMissiles.get(index);
                                boolean status=k==0;
                                if (missile.isAlive()==status){
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
        instantiate(numberOfMissile);
    }

    private void instantiate(final int numberOfMissile){
        final MissilePool that=this;
        processHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (that.freshMissiles){
                    for (int index=0;index<numberOfMissile;index++){
                        Context context= (Context) that.resources.get(SpaceGame.CONTEXT);
                        ImageView missileView=new ImageView(context);
                        Missile missile=new Missile(missileView,
                                resources,spaceGame,status,mainHandler,processHandler,soundEngine)
                                .initKey(index).initRecyclable(true).initPool(that);
                        that.freshMissiles.put(index,missile);
                        missile.initialize();
//                        missile.attachTo(that.layout);
                    }
                }
                that.availability=true;
            }
        });
    }

    /*
        Recycle missile into freshArray
     */
    public void recycle( Missile missile) throws Exception {
        if (!this.availability){
            return;
        }
        if (missile.getPool()!=this){

            throw new Exception("This Missile is not in this pool");
        }
        if (!missile.isRecyclable()){
            synchronized (this.excessiveMissiles){
                missile.detachFrom(layout);
                this.excessiveMissiles.remove(missile);
            }
        }else{

            synchronized (this.gloriousMissiles){
                missile.setAliveStatus(true);
                missile.detachFrom(layout);
                this.gloriousMissiles.remove(missile.getKey());
            }
            synchronized (this.freshMissiles){
                this.freshMissiles.put(missile.getKey(),missile);
            }

        }
    }

    // Get a vacant Missile
    public Missile getMissile(){
        if (!this.availability){
            return null;
        }
        synchronized (freshMissiles){
            synchronized (gloriousMissiles){
                if (freshMissiles.size()>0){
                    int size=freshMissiles.size();
                    this.checkCount++;
                    Missile missile=freshMissiles.get(freshMissiles.keyAt(freshMissiles.size()-1));
                    missile.setAliveStatus(false);
                    missile.setTime(System.currentTimeMillis());
                    missile.attachTo(layout);
                    freshMissiles.remove(freshMissiles.keyAt(size-1));
                    gloriousMissiles.put(freshMissiles.keyAt(size-1),missile);
                    gc();
                    missile.initialize();
                    return missile;
                }
            }

        }
        synchronized (excessiveMissiles){
            Context context= (Context) this.resources.get(SpaceGame.CONTEXT);
            ImageView missileView=new ImageView(context);
            Missile missile=new Missile(missileView,
                    resources,spaceGame,status,mainHandler,processHandler,soundEngine)
                    .initKey(-1).initPool(this).initRecyclable(false);
            excessiveMissiles.add(missile);
            missile.initialize();
            missile.attachTo(layout);
            missile.setTime(System.currentTimeMillis());
            this.checkCount++;
            gc();
            missile.initialize();
            return missile;
        }
    }

    // A garbage collector
    private void gc(){
        if (checkCount>5){
            processHandler.post(new Runnable() {
                @Override
                public void run() {
                    checkCount=0;
                    synchronized (excessiveMissiles){
                        for (int index=excessiveMissiles.size()-1;index>=0;index--){
                            final Missile missile=excessiveMissiles.get(index);
                            if (System.currentTimeMillis()-missile.getTime()>20000){
                                mainHandler.post(() -> {
                                    try {
                                        recycle(missile);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }
                    synchronized (gloriousMissiles){
                        for (int index=gloriousMissiles.size()-1;index>=0;index--){
                            final Missile missile=gloriousMissiles.get(index);
                            if (missile==null){
                                continue;
                            }
                            if (System.currentTimeMillis()-missile.getTime()>20000){
                                mainHandler.post(()->{
                                    try {
                                        recycle(missile);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }

    }

    public static class Builder {
        private ViewGroup layout;
        private int numberOfMissile;
        private Handler mainHandler;
        private Handler processHandler;
        private SpaceGame.Resources resources;
        private SpaceGame spaceGame;
        private SpaceGame.Status status;
        private SoundEngine soundEngine;


        public Builder(int capacity)
        {
            this.numberOfMissile = capacity;
        }

        public Builder(ViewGroup layout, SpaceGame.Resources resources, SpaceGame spaceGame,
                       SpaceGame.Status status, int numberOfMissile, Handler mainHandler,
                       Handler processHandler)
        {
            this.layout = layout;
            this.mainHandler = mainHandler;
            this.processHandler = processHandler;
            this.resources = resources;
            this.spaceGame = spaceGame;
            this.status = status;
            this.numberOfMissile = numberOfMissile;
        }


        public Builder setLayout(ViewGroup layout)
        {
            this.layout = layout;
            return this;
        }

        public Builder setMainHandler(Handler mainHandler)
        {
            this.mainHandler = mainHandler;
            return this;
        }

        public Builder setProcessHandler(Handler processHandler)
        {
            this.processHandler = processHandler;
            return this;
        }

        public Builder setResources(SpaceGame.Resources resources)
        {
            this.resources = resources;
            return this;
        }


        public Builder setSpaceGame(SpaceGame spaceGame)
        {
            this.spaceGame = spaceGame;
            return this;
        }


        public Builder setStatus(SpaceGame.Status status)
        {
            this.status = status;
            return this;
        }

        public MissilePool build()
        {
            return new MissilePool(layout, resources, spaceGame, status,
                    numberOfMissile, mainHandler, processHandler,soundEngine);
        }

        public Builder setSoundEngine(SoundEngine soundEngine) {
            this.soundEngine = soundEngine;
            return this;
        }
    }

}
