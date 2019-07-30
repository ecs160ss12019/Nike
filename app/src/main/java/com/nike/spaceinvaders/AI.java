package com.nike.spaceinvaders;

import java.util.HashMap;

/**
 * This class is an AI class that would give the configuration to the game that set the difficulty of the game.
 */
public class AI {
    private static AI instance;
    private static HashMap<String,Evaluator> cache=new HashMap<>();

    public static AI getAI(SpaceGame.Status status){
        if (instance==null){
            instance=new AI();
        }
        return instance;
    }

    class InvaderEvaluator extends Evaluator{

        @Override
        public float evaluate(int value) {
            switch (value){
                case Invader.
            }
            return 0;
        }
    }


    abstract class Evaluator {
        private float scale=1;
        abstract float evaluate(int value);

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }
    }
}
