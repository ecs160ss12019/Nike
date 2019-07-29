package com.nike.spaceinvaders;

import android.content.Context;

interface MissileForm {
    void playMissileSound();
}


class InvaderAMissileForm implements MissileForm
{
    private SoundEngine se;

    public InvaderAMissileForm(Context context)
    {
        se = SoundEngine.getInstance(context);
    }

    public void playMissileSound()
    {
        se.playMissile();
    }

}


class InvaderBMissileForm implements MissileForm
{
    private SoundEngine se;

    public InvaderBMissileForm(Context context)
    {
        se = SoundEngine.getInstance(context);
    }

    public void playMissileSound()
    {

    }

}




class InvaderCMissileForm implements MissileForm
{
    private SoundEngine se;

    public InvaderCMissileForm(Context context)
    {
        se = SoundEngine.getInstance(context);
    }

    public void playMissileSound()
    {

    }
}




class LaserBaseMissileForm implements MissileForm
{
    private SoundEngine se;

    public LaserBaseMissileForm(Context context)
    {
        se = SoundEngine.getInstance(context);
    }

    public void playMissileSound()
    {

    }
}