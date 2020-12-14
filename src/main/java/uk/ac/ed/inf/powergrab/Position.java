package uk.ac.ed.inf.powergrab;

/**
 * Position class which holds latitude and longitude.
 */
public class Position {

    public double latitude;
    public double longitude;

    Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * This method returns a new position given a direction. It adds the direction's latitude and longitude components
     * to its latitude and longitude attributes.
     * @param direction Direction to go towards.
     * @return Position with the new values of latitude and longitude
     */
    public Position nextPosition(Direction direction) {
        double new_lat = this.latitude + direction.getLatitude();
        double new_long = this.longitude + direction.getLongitude();
        return new Position(new_lat, new_long);
    }


    /**
     * This method checks if the position's latitude and longitude attributes are within the given range of values and
     * thus within the allowed play area.
     * @return True if it is in play area, False otherwise
     */
    public boolean inPlayArea() {
        return (latitude < 55.946233 && latitude > 55.942617) && (longitude > -3.192473 && longitude < -3.184319);
    }

    /**
     * This method returns the attributes of the position in the form of a tuple
     * @return [latitude, longitude]
     */
    public double[] getCoords(){
        double [] out = new double[2];
        out[0] = longitude;
        out[1] = latitude;
        return out;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }



}
