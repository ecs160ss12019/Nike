package com.nike.spaceinvaders;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * This class is a manager class for BaseShelter.
 * It holds and manages an {@link ImageView} object that holds the image of the BaseShelter.
 */
class BaseShelter extends AnimatedObject<ImageView> {
    int[] hitBox;
    // number of cols and rows of hitBoxes
    private int numRow, numCol;
    // Each hit box is 10 by 10 in pixels
    private int boxSize = 10;


    private Bitmap bitmap;

    private Canvas canvas;

    BaseShelter(ImageView view, SpaceGame.Resources resources, SpaceGame spaceGame,
                SpaceGame.Status status, Handler mainHandler, Handler processHandler) {
        super(null, view, resources, spaceGame,status, mainHandler, processHandler);

        // initialize hitBox

        numRow = this.getHeight() / boxSize;
        numCol = this.getWidth() / boxSize;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        this.bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), conf); // this creates a MUTABLE bitmap

        this.canvas=new Canvas(this.bitmap);
        this.hitBox=new int[this.getHeight()*this.getWidth()];
        Drawable shelter=this.getDrawable();
        shelter.setBounds(shelter.copyBounds());
        shelter.setBounds(0,0, this.getWidth(), this.getHeight());
        shelter.draw(this.canvas);
        this.bitmap.getPixels(this.hitBox,0,this.getWidth(),0,0,this.getWidth(),this.getHeight());
        normalizeHitbox();
    }

    private void normalizeHitbox(){
        for(int i=0;i<this.hitBox.length;i++){
            if (this.hitBox[i]==Color.argb(0,0,0,0)||this.hitBox[i]==Color.argb(255,0,0,0)){
                this.hitBox[i]=1;
            }
        }
    }



    /*
    Only need to handle the strike case
     */
    @Override
    protected void handle(Actions actions, Set<Integer> keys) {

    //    SparseArray<Float> data = Objects.requireNonNull(actions.get(SpaceGame.STRIKE)).second;
        Missile missile = (Missile)Objects.requireNonNull(actions.get(SpaceGame.STRIKE)).first;
//        float missileAbsX = data.get(SpaceGame.X_COORDINATE);
//        float missileAbsY = data.get(SpaceGame.Y_COORDINATE);

        float missileAbsX = missile.getX();
        float missileAbsY = missile.getY();

        Log.d("parentCoordinate2", String.valueOf(missileAbsX));
        // change the absolute missile coordinates to coordinates relative to shelter
        float missileRelX = missileAbsX-this.getAbsoluteX();
        float missileRelY = missileAbsY-this.getAbsoluteY();

        // check they are within hitbox ranges ( 0 < x < numCol && 0 < y < numRow)
       // if(0 < missileRelX && missileRelX < numCol && 0 < missileRelY && missileRelY < numRow)
        //{
            // hit detection
           // PointF boxXY = getBoxCoordinate(missileRelX, missileRelY);
            PointF boxXY = new PointF(missileRelX, missileRelY);
            Point hitPoint = hitDetection(boxXY, new Size(0, this.boxSize));

            Log.d("asdf", "here");

            if(hitPoint != null)
            {
                // draw the hitting effect using bitmap
                Log.d("HitX", String.valueOf(hitPoint.x));
                Log.d("HitY", String.valueOf(hitPoint.y));
                drawDamage(hitPoint.x, hitPoint.y);

                // notify the missile to be gone
                Actions missileGone = new Actions();
                missileGone.put(SpaceGame.MISSILE_GONE, new
                        Pair<AnimatedObject, SparseArray<Float>>(this, null));

                Set<Integer> newKeys=new ArraySet<>();
                newKeys.add(SpaceGame.MISSILE_GONE);
                missile.handle(missileGone, newKeys);
            }

 //       }
        // else return


    }


    private void drawDamage(float x,float y){
//        if (this.bitmap==null) {
//            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
//            this.bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), conf); // this creates a MUTABLE bitmap
//        }
//        if (this.canvas==null){
//            this.canvas=new Canvas(this.bitmap);
//        }
        Resources resources= (Resources) this.getResources().get(SpaceGame.RESOURCES);
        assert resources != null;
//        Drawable shelter=this.getDrawable();
//        shelter.setBounds(shelter.copyBounds());
        Drawable damage=resources.getDrawable(R.drawable.explode,null);
//        shelter.setBounds(0,0, this.getWidth(), this.getHeight());
//        shelter.draw(this.canvas);
        x = x -30;
        y = y -20;
        damage.setBounds((int)x,(int)y, 70 + (int)x, 70 + (int)y);
        damage.draw(this.canvas);
        this.setBitmap(this.bitmap);
//        if (this.hitBox==null){
//            this.hitBox=new int[this.getHeight()*this.getWidth()];
//        }
        this.bitmap.getPixels(this.hitBox,0,this.getWidth(),0,0,this.getWidth(),this.getHeight());
        normalizeHitbox();
        //        for (int index=0;index<10316;index++){
//            pixels[index]=0;
//        }
//        this.bitmap.setPixels(pixels,0,this.getWidth(),0,0,this.getWidth(),this.getHeight());
        return;
    }

    @Override
    ValueAnimator.AnimatorUpdateListener animatorListenerConfigure() {
        return null;
    }

    /*
    Get the coordinate in terms of box
    For example, if a box has relative coordinates of (18, 22), then
    it is in the second column, third row, and thus should have box
    coordinates of (1.8, 2.2) if the box size is 10
     */
//    private PointF getBoxCoordinate(float x,float y){
//        float newX = x / this.boxSize;
//        float newY = y / this.boxSize;
//        return new PointF(newX,newY);
//    }


    /*
    Return the hitting point location if it hits
    Otherwise return null
     */
    private Point hitDetection(PointF position,Size size){
        Log.d("parentposition",position.toString());
        int width=this.getWidth();
        int height=this.getHeight();
        Log.d("height", String.valueOf(height));
        Log.d("width", String.valueOf(width));
        int minX= (int) Math.floor(position.x);
        int maxX= (int) Math.ceil(position.x+size.getWidth());
        int minY= (int) Math.floor(position.y);
        int maxY= (int) Math.ceil(position.y+size.getHeight());
        float slope=(float)(maxY-minY)/(float) (maxX-minX);
        for (int x=minX;x<=maxX;x++){
            int y= (int) ((float)(x-minX)*slope+minY);
            int realCoordinate=this.getWidth()*(y)+x;
            

            if (x>=0&&y>=0&&x<width&&y<height&&realCoordinate>=0&&realCoordinate<hitBox.length&& this.hitBox[realCoordinate] == Color.argb(255, 255, 0, 0)){
                int pixel=this.hitBox[realCoordinate];
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                int alphaValue = Color.alpha(pixel);
                Log.d("ValueARGB", String.valueOf(Color.argb(255,0,0,0)));
                Log.d("Value", String.valueOf(pixel));
                Log.d("AlphaValue", String.valueOf(alphaValue));
                Log.d("RedValue", String.valueOf(redValue));
                Log.d("BlueValue",String.valueOf(blueValue));
                Log.d("GreenValue", String.valueOf(greenValue));
                return new Point(x,y);
            }
        }
        return null;
    }
}
