package uk.ac.ed.inf;

import java.util.ArrayList;

public class Stateless {
    public double coins = 0.0;
    public double power = 250.0;
    public Position currentPosition;

    public Stateless(Position initialPosition){
        this.currentPosition = initialPosition;
    }

    public ArrayList<Feature> getFeaturesInRange(){
        ArrayList<Feature> featuresInRange = new ArrayList<Feature>();
        for (Feature feature : App.features) {
            double diffLatitude = Math.abs(feature.latitude - currentPosition.latitude);
            double diffLongitude = Math.abs(feature.longitude - currentPosition.longitude);
            double distanceFromFeature = Math.sqrt(Math.pow(diffLatitude, 2) + Math.pow(diffLongitude, 2));
            if (distanceFromFeature <= 0.0003){
                featuresInRange.add(feature);
                System.out.print(feature);
            }
        }
        return featuresInRange;
    }


    public Direction makeMove(){
        return Direction.NE;
    }
}