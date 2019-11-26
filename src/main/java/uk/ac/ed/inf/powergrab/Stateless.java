package uk.ac.ed.inf.powergrab;

import java.util.Random;

public class Stateless extends Drone{
    private Random rand = new Random();

    Stateless(Position initialPosition, long seed){
        super.currentPosition = initialPosition;
        rand.setSeed(6789);
    }

    @Override
    public Direction getMove(){
        Feature maxFeature = null;
        Direction dir = null;
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
        if (dir != null){
            this.currentPosition = currentPosition.nextPosition(dir);
            return dir;
        }
        Direction d = Direction.values()[rand.nextInt(Direction.values().length)];
        Position nextPosition = currentPosition.nextPosition(d);
        int count = 0;
        while(!nextPosition.inPlayArea() || nearNegativeOrEmpty(nextPosition) && count <= 16){
            d = Direction.values()[rand.nextInt(Direction.values().length)];
            nextPosition = currentPosition.nextPosition(d);
            if (!nearNegativeOrEmpty(nextPosition) && nextPosition.inPlayArea()){
                this.currentPosition = nextPosition;
                return d;
            }
            count += 1;
        }
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