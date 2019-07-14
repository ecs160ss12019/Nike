package com.nike.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class UHD implements GameObject{
    private int mScreenX;
    private int mScreenY;
    private int score;
    private int level;

    public UHD(int x,int y){
        //init
        this.mScreenX = x;
        this.mScreenY = y;
        score=0;
        level=1;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(50);
        canvas.drawText("Score: "+score,mScreenX/2,50,paint);
    }

    @Override
    public void update() {
        //score++;
    }


    //used when reset game
    public void reset(){

    }


}
