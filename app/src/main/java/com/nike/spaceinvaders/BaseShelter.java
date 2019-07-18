package com.nike.spaceinvaders;

import android.graphics.RectF;

class BaseShelter {
    ShelterBlock[] mBaseShelter;
    int ySpacing;
    int xSpacing;
    int xCord;
    int yCord;


    BaseShelter(int screenX, int screenY, int shelterNum){
        xSpacing = screenX/9;
        ySpacing = screenY - (screenY/20);

        switch (shelterNum) {
            case 1: xSpacing = xSpacing * 1;
                    break;
            case 2: xSpacing = xSpacing * 3;
                    break;
            case 3: xSpacing = xSpacing * 5;
                    break;
            case 4: xSpacing = xSpacing * 7;
                    break;
        }

        xCord = xSpacing;
        yCord = ySpacing;

        mBaseShelter = new ShelterBlock[10];

        for (int i = 0; i < 4; i++) {                                            //[][][][]
            mBaseShelter[i] = new ShelterBlock(screenX, screenY, xCord, yCord);  //. . . .
            xCord = xCord + screenX/20;                                          //.     .
        }

        xCord = xSpacing;
        yCord = ySpacing + (screenX/20);

        for (int i = 4; i < 8; i++) {                                            //. . . .
            mBaseShelter[i] = new ShelterBlock(screenX, screenY, xCord, yCord);  //[][][][]
            xSpacing = xSpacing + screenX/20;                                    //.     .
        }

        xCord = xSpacing;                                                     //. . . .
        yCord = ySpacing + (2*(screenX/20));                                  //. . . .
        mBaseShelter[8] = new ShelterBlock(screenX, screenY, xCord, yCord);   //[]    .

                                                                              //. . . .
        xCord = xCord + (screenX/20) + (screenX/20) + (screenX/20);           //. . . .
        mBaseShelter[9] = new ShelterBlock(screenX, screenY, xCord, yCord);   //.    []
    }

    void draw() {

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
