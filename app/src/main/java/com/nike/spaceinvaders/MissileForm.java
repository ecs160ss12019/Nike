package com.nike.spaceinvaders;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

interface MissileForm {
    void playMissileSound();
    void setMissileImage(Missile missile, Resources resources);
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

    public void setMissileImage(Missile missile, Resources resources)
    {
        assert resources != null;
        Drawable drawable = resources.getDrawable(R.drawable.bananamissile, null);
        missile.setDrawable(drawable);
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

    public void setMissileImage(Missile missile, Resources resources)
    {
        assert resources != null;
        Drawable drawable = resources.getDrawable(R.drawable.greenmissile, null);
        missile.setDrawable(drawable);
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

    public void setMissileImage(Missile missile, Resources resources)
    {
        assert resources != null;
        Drawable drawable = resources.getDrawable(R.drawable.chompermissile, null);
        missile.setDrawable(drawable);
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

    public void setMissileImage(Missile missile, Resources resources)
    {
        return;
    }
}