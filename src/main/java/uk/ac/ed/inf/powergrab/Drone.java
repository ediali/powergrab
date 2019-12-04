package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

/**
 * Abstract Drone class which holds many common methods between the different implementations of the drone but has an
 * abstract getMove() method.
 */
public abstract class Drone {
    public double coins = 0.0;
    public double power = 250.0;
    public Position currentPosition;

    /**
     * Returns an ArrayList of features which are within a radius of 0.00025 of the given position.
     * It does this by running through a for loop of all the App's features and returns those whose distance are
     * less than or equal to 0.00025
     * @param position Position
     * @return ArrayList of features which are within 0.00025
     */
    private ArrayList<Feature> getFeaturesInRange(Position position){
        ArrayList<Feature> featuresInRange = new ArrayList<Feature>();
        for (Feature feature : App.features) {
            double distanceFromFeature = findDistance(feature.getPosition(), position);
            if(distanceFromFeature <= 0.00025){
                featuresInRange.add(feature);
            }
        }
        return featuresInRange;
    }

    /**
     * Returns the distance between two given positions. It does this simply by using Pythagorean theorem.
     * @param position1 Position
     * @param position2 Position
     * @return double representing the distance between the two positions
     */
    double findDistance(Position position1, Position position2){
        double diffLatitude = Math.abs(position1.getLatitude() - position2.getLatitude());
        double diffLongitude = Math.abs(position1.getLongitude() - position2.getLongitude());
        return Math.sqrt(Math.pow(diffLatitude, 2) + Math.pow(diffLongitude, 2));
    }

    /**
     * Finds whether the closest feature to the given position is negative or zero.
     * Returns false if there it is not near a negative or zero station.
     * @param position a given position, usually the drone's
     * @return false if near negative or empty, true otherwise
     */
    boolean nearNegativeOrEmpty(Position position){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(position);
        if (featuresInRange.size() == 0){
            return false;
        }
        double min = Double.MAX_VALUE;
        Feature minFeature = featuresInRange.get(0);
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.getPosition(), position);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }

        return minFeature.getPower() <= 0;
    }

    /**
     * Finds whether the closest feature to the given position is negative.
     * Returns false if there it is not near a negative station.
     * @param position a given position, usually of the drone
     * @return True if not near a negative, false otherwise
     */
    boolean nearNegative(Position position){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(position);
        if (featuresInRange.size() == 0){
            return true;
        }
        double min = Double.MAX_VALUE;
        Feature minFeature = featuresInRange.get(0);
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.getPosition(), position);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }

        return minFeature.getPower() >= 0;
    }

    /**
     * Updates the coins and power of the drone.
     * It will find the closest station and dock to it, or if there are none within 0.00025, simply
     * subtract 1.25 from the drone's power.
     */
    public void updateCoinsAndPower(){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(currentPosition);
        if (featuresInRange.size() == 0){
            power -= 1.25;
            return;
        }
        Feature minFeature = null;
        double min = Double.MAX_VALUE;
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.getPosition(), currentPosition);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }
        coins += minFeature.getCoins();
        power += minFeature.getPower() - 1.25;
        App.features.get(App.features.indexOf(minFeature)).setCoins(0);
        App.features.get(App.features.indexOf(minFeature)).setPower(0);
    }

    /**
     * Return the feature which you will land on given a position.
     * Returns null if there are no features within 0.00025
     * @param position drone's position
     * @return The closest feature to which the drone would dock to
     */
    Feature findLandingFeature(Position position){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(position);
        if (featuresInRange.size() == 0){
            return null;
        }
        double min = Double.MAX_VALUE;
        Feature minFeature = null;
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.getPosition(), position);
            if (distance < min){
                minFeature = feature;
                min = distance;
            }
        }

        return minFeature;
    }

    /**
     * Finds the direction to make the drone go to, changes its position and returns the direction
     * @return Direction in which the drone will make a move
     */
    abstract Direction getMove();

}
