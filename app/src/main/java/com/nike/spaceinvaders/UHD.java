package com.nike.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class UHD implements GameObject{
    private int mScreenX;
    private int mScreenY;
    public int score;
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

        //draw RGB
        Paint paint=new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.argb(125,199,21,133));
        canvas.drawText("Score: "+score+" Level: "+level,15,50,paint);
        paint.setColor(Color.argb(125,0,0,205));
        canvas.drawText("Score: "+score+" Level: "+level,5,50,paint);
        paint.setColor(Color.argb(255,0,0,0));
        canvas.drawText("Score: "+score+" Level: "+level,10,50,paint);

        //draw Pause
        canvas.drawText("PAUSE",mScreenX-200,50,paint);
    }

    @Override
    public void update() {
        //score++;
    }


    //used when reset game
    public void reset(){
        score=0;
        level=1;
    }


}
