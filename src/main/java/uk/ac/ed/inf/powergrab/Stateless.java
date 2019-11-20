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
        rand.setSeed(6789);
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

    private ArrayList<Feature> getFeaturesInRange(Position position){
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


    private boolean nearNegativeOrEmpty(Position position){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(position);
        if (featuresInRange.size() == 0){
            return false;
        }
        double min = Double.MAX_VALUE;
        Feature minFeature = featuresInRange.get(0);
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.latitude, feature.longitude, position.latitude, position.longitude);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }

        return minFeature.getPower() <= 0;
    }


    private Feature findLandingFeature(Position position){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(position);
        if (featuresInRange.size() == 0){
            return null;
        }
        double min = Double.MAX_VALUE;
        Feature minFeature = featuresInRange.get(0);
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.latitude, feature.longitude, position.latitude, position.longitude);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }

        return minFeature;

    }


    public void getNextDirMove(){
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
            return;
        }
        Direction d = Direction.values()[rand.nextInt(Direction.values().length)];
        Position nextPosition = currentPosition.nextPosition(d);
        int count = 0;
        while(!nextPosition.inPlayArea() || nearNegativeOrEmpty(nextPosition) && count <= 16){
            d = Direction.values()[rand.nextInt(Direction.values().length)];
            nextPosition = currentPosition.nextPosition(d);
            if (!nearNegativeOrEmpty(nextPosition) && nextPosition.inPlayArea()){
                this.currentPosition = nextPosition;
                return;
            }
            count += 1;
        }
        if (count >= 16){
            while(!nextPosition.inPlayArea()){
                d = Direction.values()[rand.nextInt(Direction.values().length)];
                nextPosition = currentPosition.nextPosition(d);
                if (!nearNegativeOrEmpty(nextPosition) && nextPosition.inPlayArea()){
                    this.currentPosition = nextPosition;
                    return;
                }
                count += 1;
            }
        }
        this.currentPosition = nextPosition;
        return;
    }
    private double findDistance(double latitude1, double longitude1, double latitude2, double longitude2){
        double diffLatitude = Math.abs(latitude1 - latitude2);
        double diffLongitude = Math.abs(longitude1 - longitude2);
        return Math.sqrt(Math.pow(diffLatitude, 2) + Math.pow(diffLongitude, 2));
    }
}