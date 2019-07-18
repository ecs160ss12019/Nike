package com.nike.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


public class HUD implements GameObject{
    private int mScreenX;
    private int mScreenY;
    public int score;
    private int level;
    private int Lifes;

    public HUD(int x, int y){
        //init
        this.mScreenX = x;
        this.mScreenY = y;
        score=0;
        level=1;
        Lifes=3;
    }
    @Override
    public void draw(Canvas canvas) {

        //draw RGB
        Paint paint=new Paint();
        paint.setTextSize(50);

/*        paint.setColor(Color.argb(200,199,21,133));
        canvas.drawText("Score: "+score+" Level: "+level,15,50,paint);
        paint.setColor(Color.argb(200,0,0,205));
        canvas.drawText("Score: "+score+" Level: "+level,5,50,paint);
        paint.setColor(Color.argb(255,255,255,255));
        canvas.drawText("Score: "+score+" Level: "+level,10,50,paint);*/
        drawRGB(canvas,"Score: "+score+" Level: "+level,10,50);


        //draw Pause
        //canvas.drawText("PAUSE",mScreenX-200,50,paint);

        //draw Line
        paint.setColor(Color.GREEN);
        canvas.drawLine(0, mScreenY-100, mScreenX, mScreenY-100, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(""+Lifes,10, mScreenY-50,paint);
        drawRGB(canvas,""+Lifes,10,mScreenY-50);
        //draw Lifes
        paint.setColor(Color.GREEN);
        for(int i = 0;i<Lifes;i++){ //TODO: Should be Laserbase shape not Rect, it just a test
            Rect templete = new Rect(50+60*i,mScreenY-90,100+60*i,mScreenY-40);
            canvas.drawRect(templete,paint);
        }


        //draw Credit
        //drawRGB(canvas,"CREDIT: "+score,);
    }

    @Override
    public void update() {

        //score++;
    }


    //used when reset game
    public void reset(){
        score=0;
        level=1;
        Lifes=3;
    }
    //Draw RGB Effect
    public void drawRGB(Canvas canvas,String str,int x,int y){
        Paint paint;
        paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.argb(200,199,21,133));
        canvas.drawText(str,x+5,y,paint);
        paint.setColor(Color.argb(200,0,0,205));
        canvas.drawText(str,x-5,y,paint);
        paint.setColor(Color.argb(255,255,255,255));
        canvas.drawText(str,x,y,paint);
    }

}
