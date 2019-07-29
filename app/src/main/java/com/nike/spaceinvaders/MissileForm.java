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
        se.playInvaderMissile1();
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
        se.playInvaderMissile2();
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
        se.playInvaderMissile3();
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
        se.playLaserBaseMissile();
    }
}