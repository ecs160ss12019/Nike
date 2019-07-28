package com.nike.spaceinvaders;

import android.os.Handler;
import android.view.ViewGroup;

public class MissilePoolBuilder {
    private ViewGroup layout;
    private int numberOfMissile;
    private Handler mainHandler;
    private Handler processHandler;
    private SpaceGame.Resources resources;
    private SpaceGame spaceGame;
    private SpaceGame.Status status;
    private SoundEngine soundEngine;


    public MissilePoolBuilder(int capacity)
    {
        this.numberOfMissile = capacity;
    }

    public MissilePoolBuilder(ViewGroup layout, SpaceGame.Resources resources, SpaceGame spaceGame,
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


    public MissilePoolBuilder setLayout(ViewGroup layout)
    {
        this.layout = layout;
        return this;
    }

    public MissilePoolBuilder setMainHandler(Handler mainHandler)
    {
        this.mainHandler = mainHandler;
        return this;
    }

    public MissilePoolBuilder setProcessHandler(Handler processHandler)
    {
        this.processHandler = processHandler;
        return this;
    }

    public MissilePoolBuilder setResources(SpaceGame.Resources resources)
    {
        this.resources = resources;
        return this;
    }


    public MissilePoolBuilder setSpaceGame(SpaceGame spaceGame)
    {
        this.spaceGame = spaceGame;
        return this;
    }


    public MissilePoolBuilder setStatus(SpaceGame.Status status)
    {
        this.status = status;
        return this;
    }

    public MissilePool build()
    {
        return new MissilePool(layout, resources, spaceGame, status,
                numberOfMissile, mainHandler, processHandler,soundEngine);
    }

    public MissilePoolBuilder setSoundEngine(SoundEngine soundEngine) {
        this.soundEngine = soundEngine;
        return this;
    }
}
