package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Random;

public class Stateless {
    public double coins = 0.0;
    public double power = 250.0;
    public Position currentPosition;
    private Random rand = new Random();


    public Stateless(Position initialPosition, long seed){
        this.currentPosition = initialPosition;
        rand.setSeed(seed);
    }

    public ArrayList<Feature> getFeaturesInMoveRange(Position position){
        ArrayList<Feature> featuresInMoveRange = new ArrayList<Feature>();
        for (Feature feature : App.features) {
            double distanceFromFeature = findDistance(feature.latitude, feature.longitude, position.latitude, position.longitude);
            if (distanceFromFeature > 0.00025 & distanceFromFeature <= 0.00030) {
                featuresInMoveRange.add(feature);

            }
        }
        return featuresInMoveRange;
    }

    public ArrayList<Feature> getFeaturesInRange(Position position){
        ArrayList<Feature> featuresInRange = new ArrayList<Feature>();
        for (Feature feature : App.features) {
            double distanceFromFeature = findDistance(feature.latitude, feature.longitude, position.latitude, position.longitude);
            if(distanceFromFeature <= 0.00025){
                featuresInRange.add(feature);
            }
        }
        return featuresInRange;
    }


    public void updateCoinsAndPower(){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(currentPosition);
        if (featuresInRange.size() == 0){
            power -= 1.25;
            return;
        }
        Feature minFeature = featuresInRange.get(0);
        double min = Double.MAX_VALUE;
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.latitude, feature.longitude, currentPosition.latitude, currentPosition.longitude);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }
        coins += minFeature.getCoins();
        power += minFeature.getPower() - 1.25;
        App.features.get(App.features.indexOf(minFeature)).coins = 0;
        App.features.get(App.features.indexOf(minFeature)).power = 0;
    }


    public boolean nearNegativeOrEmpty(Position position){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(position);
        if (featuresInRange.size() == 0){
            return false;
        }
        double min = Double.MAX_VALUE;
        Feature minFeature = featuresInRange.get(0);
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.latitude, feature.longitude, currentPosition.latitude, currentPosition.longitude);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }

        return minFeature.getPower() <= 0;

    }

    public Direction getNextMove(){
        ArrayList<Feature> featuresInMoveRange = getFeaturesInMoveRange(currentPosition);

        for(Feature feature : featuresInMoveRange){
            if (feature.getSymbol().equals("lighthouse") && feature.getPower() > 0) {
                for(Direction  d: Direction.values()){
                    Position nextPosition = currentPosition.nextPosition(d);
                    if (!nextPosition.inPlayArea()){
                        continue;
                    }
                    if (!nearNegativeOrEmpty(nextPosition)){
                        this.currentPosition = nextPosition;
                        return d;
                    }
                }
            }
        }
        Direction d = Direction.values()[rand.nextInt(Direction.values().length)];
        Position nextPosition = currentPosition.nextPosition(d);
        while(!nextPosition.inPlayArea() || nearNegativeOrEmpty(nextPosition)){
            d = Direction.values()[rand.nextInt(Direction.values().length)];
            nextPosition = currentPosition.nextPosition(d);
            if (!nextPosition.inPlayArea()){
                continue;
            }
            if (!nearNegativeOrEmpty(nextPosition)){
                this.currentPosition = nextPosition;
                return d;
            }
        }
        this.currentPosition = nextPosition;
        return d;
    }

    private double findDistance(double latitude1, double longitude1, double latitude2, double longitude2){
        double diffLatitude = Math.abs(latitude1 - latitude2);
        double diffLongitude = Math.abs(longitude1 - longitude2);
        return Math.sqrt(Math.pow(diffLatitude, 2) + Math.pow(diffLongitude, 2));
    }
}