package com.nike.spaceinvaders;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

interface HitDetection {
    Point hitDetection(AnimatedObject animatedObject,PointF position, AnimatedObject.Size size);
}

class NormalHitDetection implements HitDetection{

    @Override
    public Point hitDetection(AnimatedObject animatedObject,PointF position, AnimatedObject.Size size) {
        float x = position.x;
        float y = position.y;
        int missileWidth = size.getWidth();
        float left, top, bottom, right;
        float padding = 50;
        left = animatedObject.getAbsoluteX() + padding;
        top = animatedObject.getAbsoluteY();
        bottom = top + animatedObject.getHeight();
        right = left + animatedObject.getWidth() - padding;
        if ((x >= left && x <= right && y <= bottom && y >= top) || ((x + missileWidth) >= left && (x + missileWidth) <= right && y <= bottom && y >= top)) {
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