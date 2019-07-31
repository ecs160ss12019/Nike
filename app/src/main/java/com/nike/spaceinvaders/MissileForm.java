package com.nike.spaceinvaders;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

interface MissileForm {
    void playMissileSound();
    void setMissileImage(Missile missile, Resources resources);
}


class YellowInvaderMissileForm implements MissileForm
{
    private SoundEngine se;

    public YellowInvaderMissileForm(Context context)
    {
        se = SoundEngine.getInstance(context);
    }

    public void playMissileSound()
    {
        se.playYellowInvaderMissile();
    }

    public void setMissileImage(Missile missile, Resources resources)
    {
        assert resources != null;
        Drawable drawable = resources.getDrawable(R.drawable.bananamissile, null);
        missile.setDrawable(drawable);
    }
}



class BlueInvaderMissileForm implements MissileForm
{
    private SoundEngine se;

    public BlueInvaderMissileForm(Context context)
    {
        se = SoundEngine.getInstance(context);
    }

    public void playMissileSound()
    {
        se.playBlueInvaderMissile();
    }

    public void setMissileImage(Missile missile, Resources resources)
    {
        assert resources != null;
        Drawable drawable = resources.getDrawable(R.drawable.greenmissile, null);
        missile.setDrawable(drawable);
    }

}




class OrangeInvaderMissileForm implements MissileForm
{
    private SoundEngine se;

    public OrangeInvaderMissileForm(Context context)
    {
        se = SoundEngine.getInstance(context);
    }

    public void playMissileSound()
    {
        se.playOrangeInvaderMissile();
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