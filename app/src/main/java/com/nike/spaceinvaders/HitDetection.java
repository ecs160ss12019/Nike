package com.nike.spaceinvaders;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

interface HitDetection {
    Point hitDetection(AnimatedObject animatedObject,PointF position, AnimatedObject.Size size);
}

class NormalHitDetectio implements HitDetection{

    @Override
    public Point hitDetection(AnimatedObject animatedObject,PointF position, AnimatedObject.Size size) {
        float x = position.x;
        float y = position.y;
        int missileWidth = size.getWidth();
        float left, top, bottom, right;
        left = animatedObject.getAbsoluteX() + 50;
        top = animatedObject.getAbsoluteY();
        bottom = top + animatedObject.getHeight();
        right = left + animatedObject.getWidth() - 50;
        if ((x >= left && x <= right && y <= bottom && y >= top) || ((x + missileWidth) >= left && (x + missileWidth) <= right && y <= bottom && y >= top)) {
            animatedObject.getSoundEngine().playInvaderDeath(); //Sound effect for invader being destroyed
            return new Point();
        } else {
            return null;
        }
    }
}

class PreciseHitDetection implements HitDetection{

    @Override
    public Point hitDetection(AnimatedObject animatedObject,PointF position, AnimatedObject.Size size) {

        int width = animatedObject.getWidth();
        int height = animatedObject.getHeight();
        int minX = (int) Math.floor(position.x);
        int maxX = (int) Math.ceil(position.x + size.getWidth());
        int minY = (int) Math.floor(position.y);
        int maxY = (int) Math.ceil(position.y + size.getHeight());
        float slope = (float) (maxY - minY) / (float) (maxX - minX);

        for (int x = minX; x <= maxX; x++) {
            int y = (int) ((float) (x - minX) * slope + minY);
            int realCoordinate = animatedObject.getWidth() * (y) + x;


            if (x >= 0 && y >= 0 && x < width && y < height && realCoordinate >= 0
                    && realCoordinate < animatedObject.getHitBox().length && animatedObject.getHitBox()[realCoordinate] !=
                    Color.argb(255, 0, 0, 0)) {
                return new Point(x, y);
            }
        }
        return null;
    }
}