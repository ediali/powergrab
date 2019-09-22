package uk.ac.ed.inf;

import static java.lang.Math.*;

public class Position {

    public double latitude;
    public double longitude;
    private final double r = 0.0003;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Position nextPosition(Direction direction) {
        this.latitude += r * cos(toRadians(direction.angle));
        this.longitude += r * sin(toRadians(direction.angle));
        return new Position(latitude, longitude);
    }


    public boolean inPlayArea() {
        return (latitude <= 55.946233 && latitude >= 55.942617) && (longitude >= -3.192473 && longitude <= -3.184319);
    }

}
