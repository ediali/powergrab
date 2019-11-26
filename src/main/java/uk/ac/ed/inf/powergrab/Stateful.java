package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Stateful extends Drone {
    private Feature currentFeature = null;
    private int moves = 0;
    Stateful(Position initialPosition){
        super.currentPosition = initialPosition;
    }

    private Feature getClosestFeature(Position position, List<Feature> features){
        double minDistance = Double.MAX_VALUE;
        Feature minFeature = null;
        for(Feature feature: features){
            double distance = findDistance(position.latitude, position.longitude, feature.latitude, feature.longitude);
            if (distance < minDistance && feature.getPower() > 0){
                minDistance = distance;
                minFeature = feature;
            }
        }
        return minFeature;
    }

    @Override
    public Direction getMove(){
        Feature closest;
        if (currentFeature != null){
            closest = currentFeature;
        }
        else{
            closest = getClosestFeature(currentPosition, App.features);
            currentFeature = closest;
        }

        if (moves >= 20){
            Random random = new Random();
            Feature randomFeature = App.features.get(random.nextInt(App.features.size()));
            while (randomFeature.getPower()<= 0){
                randomFeature = App.features.get(random.nextInt(App.features.size()));
            }
            currentFeature = randomFeature;
            if (randomFeature.getId().equals(closest.getId())){
                currentFeature = null;
            }
            closest = currentFeature;
            moves = 0;
        }
        // If all positive stations have been collected
        if (closest == null){
            for (Direction d: Direction.values()){
                if(!nearNegative(currentPosition.nextPosition(d)) && currentPosition.nextPosition(d).inPlayArea()){
                    currentPosition = currentPosition.nextPosition(d);
                    return d;
                }
            }
        }
        double shortest_dist = Double.MAX_VALUE;
        Direction shortest_dir = null;
        for (Direction d : Direction.values()){
            Position nextPosition = currentPosition.nextPosition(d);
            double dist = findDistance(nextPosition.latitude, nextPosition.longitude, closest.latitude, closest.longitude);
            if (dist < shortest_dist && !nearNegative(nextPosition) && nextPosition.inPlayArea()){
                double distanceFromClosest = findDistance(closest.latitude, closest.longitude, nextPosition.latitude, nextPosition.longitude);
                if (distanceFromClosest <= 0.00025 && findLandingFeature(nextPosition) == closest){
                    currentFeature = null;
                    shortest_dir = d;
                    shortest_dist = dist;
                    moves = 0;
                }
                else if (distanceFromClosest > 0.00025){
                    shortest_dir = d;
                    shortest_dist = dist;
                }
            }
        }
        moves ++;
        currentPosition = currentPosition.nextPosition(shortest_dir);
        return shortest_dir;
    }


    private Feature findPositiveLandingFeature(Position position){
        ArrayList<Feature> featuresInRange = getFeaturesInRange(position);
        if (featuresInRange.size() == 0){
            return null;
        }
        double min = Double.MAX_VALUE;
        Feature minFeature = null;
        for (Feature feature : featuresInRange){
            double distance = findDistance(feature.latitude, feature.longitude, position.latitude, position.longitude);
            if (distance < min && feature.coins > 0){
                minFeature = feature;
                min = distance;
            }
        }

        return minFeature;
    }

}
