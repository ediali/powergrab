package uk.ac.ed.inf.powergrab;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Position {

    public double latitude;
    public double longitude;
    private final double r = 0.0003;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Position nextPosition(Direction direction) {
        double angle = Math.toRadians(direction.ordinal()*22.5);
        double new_lat = this.latitude + r * sin(angle);
        double new_long = this.longitude + r * cos(angle);
        return new Position(new_lat, new_long);
    }


    public boolean inPlayArea() {
        return (latitude < 55.946233 && latitude > 55.942617) && (longitude > -3.192473 && longitude < -3.184319);
    }


    public double[] getCoords(){
        double [] out = new double[2];
        out[0] = longitude;
        out[1] = latitude;
        return out;
    }

}
