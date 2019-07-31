package com.nike.spaceinvaders;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Objects;

/**
 * This class is an AI class that would give the configuration to the game that set the difficulty of the game.
 */
public class AI {
    private static AI instance;
    private static HashMap<String,Evaluator> cache=new HashMap<>();

    public static Evaluator getAI(int classType,SpaceGame.Status status){
        String key=String.valueOf(classType);
        Evaluator evaluator;
        switch (classType){
            case SpaceGame.INVADER_GROUP:
                float level= Objects.requireNonNull(status.get(SpaceGame.LEVEL)).first;
                key+="-Level1";
                if ((evaluator=cache.get(key))==null){
                    Evaluator tempEvaluator=new InvaderEvaluator();
                    cache.put(key,tempEvaluator);
                    evaluator=tempEvaluator;
                }
                evaluator.setScale(level);
                return evaluator;
            case SpaceGame.LASER_BASE:
                float perksOfLaserBase= Objects.requireNonNull(status.get(SpaceGame.PERKS_OF_LASERBASE)).first;
                key+="-Level1";
                if ((evaluator=cache.get(key))==null){
                    Evaluator tempEvaluator=new LaserBaseEvaluator();
                    cache.put(key,tempEvaluator);
                    evaluator=tempEvaluator;
                }
                evaluator.setScale(perksOfLaserBase/10);
                return evaluator;
        }
        return null;

    }

    static class LaserBaseEvaluator extends Evaluator{

        @Override
        float evaluate(int type) {
            return getScale();
        }
    }

    static class InvaderEvaluator extends Evaluator{

        @Override
        public float evaluate(int type) {

            switch (type){
                case InvaderGroup.VELOCITY:
                    return getScale()*100;
            }
            return 0;
        }
    }


    static abstract class Evaluator {
        private float scale=1;
        abstract float evaluate(int type);

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }
    }
}
