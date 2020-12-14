package uk.ac.ed.inf.powergrab;

import java.util.Random;

/**
 * This class is the stateless extension for the Drone class, which has limited look ahead and thus often performs random
 * moves.
 */
public class Stateless extends Drone{
    private Random rand = new Random();

    Stateless(Position initialPosition, long seed){
        super.currentPosition = initialPosition;
        rand.setSeed(seed);
    }

    public Direction getMove(){
        Feature maxFeature = null;
        Direction dir = null;
        // go through all directions to find the feature which has the most coins.
        for(Direction d : Direction.values()){
            Position nextPosition = currentPosition.nextPosition(d);
            if (!nearNegativeOrEmpty(nextPosition) && nextPosition.inPlayArea()){
                Feature temp = findLandingFeature(nextPosition);
                if (maxFeature == null && temp != null){
                    maxFeature = temp;
                    dir = d;
                }
                else if (temp != null){
                    if (temp.getCoins() > maxFeature.getCoins()) {
                        maxFeature = temp;
                        dir = d;
                    }
                }
            }
        }
        // If a positive station within the allowed moves is found, return the direction to go to it
        if (dir != null){
            this.currentPosition = currentPosition.nextPosition(dir);
            return dir;
        }
        Direction d = Direction.values()[rand.nextInt(Direction.values().length)];
        Position nextPosition = currentPosition.nextPosition(d);
        int count = 0;
        /*
        If nothing is found, take a random direction which does not go out of the play area and does not
        lead to a negative station.
          */
        while(!nextPosition.inPlayArea() || nearNegativeOrEmpty(nextPosition) && count <= 16){
            d = Direction.values()[rand.nextInt(Direction.values().length)];
            nextPosition = currentPosition.nextPosition(d);
            if (!nearNegativeOrEmpty(nextPosition) && nextPosition.inPlayArea()){
                this.currentPosition = nextPosition;
                return d;
            }
            count += 1;
        }
        // If the drone gets stuck in a loop, it will not check that it is not docking to a negative station
        if (count >= 16){
            while(!nextPosition.inPlayArea()){
                d = Direction.values()[rand.nextInt(Direction.values().length)];
                nextPosition = currentPosition.nextPosition(d);
                if (!nearNegativeOrEmpty(nextPosition) && nextPosition.inPlayArea()){
                    this.currentPosition = nextPosition;
                    return d;
                }
                count += 1;
            }
        }
        this.currentPosition = nextPosition;
        return d;
    }

}