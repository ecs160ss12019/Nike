package com.nike.spaceinvaders;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private SpaceGame spaceGame;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running){
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder,SpaceGame spaceGame){
        super();
        this.surfaceHolder = surfaceHolder;
        this.spaceGame = spaceGame;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime=1000/MAX_FPS;

        while(running){
            startTime=System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.spaceGame.update();
                    this.spaceGame.draw(canvas);
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime()-startTime)/100000;
            waitTime = targetTime - timeMillis;
            try{
                if(waitTime > 0){
                    this.sleep(waitTime);
                }
            }catch (Exception e){e.printStackTrace();}
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount==MAX_FPS){
                //reset time
                averageFPS=1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS); //print aveFPS
            }
        }
    }
}
