package com.nike.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class UHD implements GameObject{
    public Rect rectangle;
    private int score;
    private int level;

    public UHD(Rect rec){
        this.rectangle=rec;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(rectangle,paint);
    }

    @Override
    public void update() {

    }
    //TODO: Just test
    public void update(Point point){
        rectangle.set(point.x-rectangle.width()/2,point.y-rectangle.height()/2,point.x+rectangle.width()/2,point.y+rectangle.height()/2);
    }


    //used when reset game
    public void reset(){

    }


}
