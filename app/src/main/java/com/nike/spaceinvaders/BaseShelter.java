/* Weili Yin, Roberto Lozano */

package com.nike.spaceinvaders;

import android.graphics.RectF;

class BaseShelter {
    int ShelterNum;
    int ShelterMaxNum = 4;
    RectF mRectF[];

    BaseShelter(){
        mRectF = new RectF[10];

    }
    //TODO: just frame, need work on that
    //collision on invader
    public boolean isCollision(Invader invader) {
        //sound and visual effects
        return false;
    }
    //collision on missile
    public boolean isCollision(Missile missile) {
        return false;
    }

}
