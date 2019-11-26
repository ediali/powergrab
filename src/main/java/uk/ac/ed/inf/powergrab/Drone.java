package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

public class Drone {
    public double coins = 0.0;
    public double power = 250.0;
    public Position currentPosition;

    ArrayList<Feature> getFeaturesInRange(Position position){
        ArrayList<Feature> featuresInRange = new ArrayList<Feature>();
        for (Feature feature : App.features) {
            double distanceFromFeature = findDistance(feature.latitude, feature.longitude, position.latitude, position.longitude);
            if(distanceFromFeature <= 0.00025){
                featuresInRange.add(feature);
            }
        }
        return featuresInRange;
    }


    double findDistance(double latitude1, double longitude1, double latitude2, double longitude2){
        double diffLatitude = Math.abs(latitude1 - latitude2);
        double diffLongitude = Math.abs(longitude1 - longitude2);
        return Math.sqrt(Math.pow(diffLatitude, 2) + Math.pow(diffLongitude, 2));
    }


    boolean nearNegativeOrEmpty(Position position){
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

    boolean nearNegative(Position position){
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

        return minFeature.getPower() < 0;
    }

    public void updateCoinsAndPower(){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(currentPosition);
        if (featuresInRange.size() == 0){
            power -= 1.25;
            return;
        }
        Feature minFeature = null;
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

    Feature findLandingFeature(Position position){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(position);
        if (featuresInRange.size() == 0){
            return null;
        }
        double min = Double.MAX_VALUE;
        Feature minFeature = null;
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.latitude, feature.longitude, position.latitude, position.longitude);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }

        return minFeature;
    }

    public Direction getMove(){
        return null;
    }

}
