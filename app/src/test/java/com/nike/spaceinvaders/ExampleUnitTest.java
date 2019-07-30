package com.nike.spaceinvaders;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void AI_instantiation(){

    }
    @Test
    public void Gravity_Sensor(){

        SpaceGame spaceGame=new SpaceGame(null,null,null,null,null,null,null,null,null,null,null,null);
        spaceGame.onSensorChanged(new);
    }
}