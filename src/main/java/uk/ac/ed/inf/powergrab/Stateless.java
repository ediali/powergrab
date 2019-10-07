package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Random;

public class Stateless {
    public double coins = 0.0;
    public double power = 250.0;
    public Position currentPosition;
    private ArrayList<Feature> featuresInMoveRange = new ArrayList<Feature>();
    private ArrayList<Feature> featuresInRange = new ArrayList<Feature>();

    public Stateless(Position initialPosition){
        this.currentPosition = initialPosition;
    }

    public void getFeaturesInRange(){
        for (Feature feature : App.features) {
            double distanceFromFeature = findDistance(feature.latitude, feature.longitude, currentPosition.latitude, currentPosition.longitude);
            if (distanceFromFeature > 0.00025 & distanceFromFeature <= 0.00030){
                this.featuresInMoveRange.add(feature);

            }
            else if(distanceFromFeature <= 0.00025){
                this.featuresInRange.add(feature);
            }

        }
    }


    public void updateCoinsAndPower(){
        getFeaturesInRange();
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
        featuresInRange.get(featuresInRange.indexOf(minFeature)).coins = 0;
        featuresInRange.get(featuresInRange.indexOf(minFeature)).power = 0;
    }

    public Direction getNextMove(){
        getFeaturesInRange();
        for(Feature feature : featuresInMoveRange){
            if (feature.getSymbol().equals("lighthouse") && feature.getPower() > 0) {
                for(Direction  d: Direction.values()){
                    Position temp = currentPosition.nextPosition(d);
                    if (findDistance(temp.latitude, temp.longitude, feature.latitude, feature.longitude) <= 0.00025){
                        this.currentPosition = temp;
                        return d;
                    }
                }
            }
        }
        return Direction.values()[new Random().nextInt(Direction.values().length)];
    }

    private double findDistance(double latitude1, double longitude1, double latitude2, double longitude2){
        double diffLatitude = Math.abs(latitude1 - latitude2);
        double diffLongitude = Math.abs(longitude1 - longitude2);
        return Math.sqrt(Math.pow(diffLatitude, 2) + Math.pow(diffLongitude, 2));
    }
}