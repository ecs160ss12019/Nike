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
                evaluator.setScale(perksOfLaserBase);
                return evaluator;
        }
        return null;

    }

    static class LaserBaseEvaluator extends Evaluator{
        private int initialMissileInterval=500;
        @Override
        float evaluate(int type) {
            switch (type){
                case LaserBase.RATE_OF_MISSILE:
                    float speed=initialMissileInterval-(this.getScale()-1)*70;
                    if (speed<0){
                        speed=0;
                    }
                    return speed;
                case LaserBase.RATE_OF_MOVEMENT:
                    return this.getScale()*5;
                default:
                    return 0;
            }
        }
    }

    static class InvaderEvaluator extends Evaluator{

        @Override
        public float evaluate(int type) {

            switch (type){
                case InvaderGroup.VELOCITY:
                    float speed= (float) ((1+(getScale()-1)*0.1)*0.000013);
                    if (getScale()>7){
                        speed= (float) ((1+(7)*0.1)*0.000013);
                    }
                    return speed;
                case InvaderGroup.RATE_OF_MISSILE:
                    float rate= (float) (800*(1-(getScale()-1)*0.1));
                    if (rate<0||getScale()>7){
                        rate= (float) (800*(1-7*0.1));
                    }
                    return rate;
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
