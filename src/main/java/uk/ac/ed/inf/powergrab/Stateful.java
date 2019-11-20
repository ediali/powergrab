package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

public class Stateful {
    public double coins = 0.0;
    public double power = 250.0;
    public Position currentPosition;

    public Stateful(Position initialPosition){
        this.currentPosition = initialPosition;
    }

    public Feature getClosestFeature(Position position){
        double minDistance = findDistance(position.latitude, position.longitude, App.features.get(0).latitude, App.features.get(0).longitude);
        Feature minFeature = App.features.get(0);
        for(Feature feature: App.features){
            double distance = findDistance(position.latitude, position.longitude, feature.latitude, feature.longitude);
            if (distance < minDistance && feature.getPower() > 0){
                minDistance = distance;
                minFeature = feature;
            }
        }
        return minFeature;
    }
    public void getMove(){
        ArrayList<Feature> features = App.features;
        getClosestFeature(currentPosition);

    }

    private double findDistance(double latitude1, double longitude1, double latitude2, double longitude2){
        double diffLatitude = Math.abs(latitude1 - latitude2);
        double diffLongitude = Math.abs(longitude1 - longitude2);
        return Math.sqrt(Math.pow(diffLatitude, 2) + Math.pow(diffLongitude, 2));
    }

}
