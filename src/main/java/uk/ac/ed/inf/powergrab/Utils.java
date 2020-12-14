package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;

public class Utils {
    public static ArrayList<Feature> features = new ArrayList<>();

    public Utils(){
    }

    public static ArrayList<Feature> getFeatures() {
        return features;
    }

    public static void setFeatures(ArrayList<Feature> features) {
        Utils.features = features;
    }
}