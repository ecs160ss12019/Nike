package com.nike.spaceinvaders;

import android.graphics.RectF;

class BaseShelter {
    ShelterBlock[] mBaseShelter;

    BaseShelter(int screenX){
        mBaseShelter = new ShelterBlock[10];
        for (int i = 0; i < 10; i++) {
            mBaseShelter[i] = new ShelterBlock(screenX);
        }
    }


    //collision with invader
    public boolean isCollision(Invader invader) {
        return false;
    }


    //collision with missile
    public boolean isCollision(Missile missile) {
        return false;
    }

}
