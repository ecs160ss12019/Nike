package com.nike.spaceinvaders;

import android.graphics.Point;

interface HitDetection {
    Point hitDetection();
}

class NormalHitDetectio implements HitDetection{

    @Override
    public Point hitDetection() {
        return null;
    }
}

class PreciseHitDetection implements HitDetection{

    @Override
    public Point hitDetection() {
        return null;
    }
}