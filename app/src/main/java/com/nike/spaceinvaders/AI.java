package com.nike.spaceinvaders;

/**
 * This class is an AI class that would give the configuration to the game that set the difficulty of the game.
 */
public class AI {
    private static AI instance;

    public static AI getInstance(){
        if (instance==null){
            instance=new AI();
        }
        return instance;
    }




    interface Evaluator {
         float evaluate(float value);
    }

    interface Bonus{
        float stimulate(float value);
    }
}
