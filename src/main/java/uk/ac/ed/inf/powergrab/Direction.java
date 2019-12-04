package uk.ac.ed.inf.powergrab;

/**
 * Enum class which holds all the 16 possible directions together with the latitude and longitude when moving
 * in that direction.
 */
public enum Direction {
    E(0, 0.000300),
    ENE(0.000115, 0.000277),
    NE(0.000212, 0.000212),
    NNE(0.000277, 0.000115),
    N(0.000300, 0),
    NNW(0.000277, -0.000115),
    NW(0.000212, -0.000212),
    WNW(0.000115, -0.000277),
    W(0, -0.000300),
    WSW(-0.000115, -0.000277),
    SW(-0.000212, -0.000212),
    SSW(-0.000277, -0.000115),
    S(-0.000300, 0),
    SSE(-0.000277, 0.000115),
    SE(-0.000212, 0.000212),
    ESE(-0.000115, 0.000277);

    private double latitude;
    private double longitude;

    Direction(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
