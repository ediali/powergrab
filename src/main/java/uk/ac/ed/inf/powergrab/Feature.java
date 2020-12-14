package uk.ac.ed.inf.powergrab;

/**
 * Feature class which holds the required attributes for a station.
 */
public class Feature {

    private final String id;
    private double coins;
    private double power;
    private final String symbol;
    private final String color;
    private final Position position;

    public Feature(String id, double coins, double power, String symbol, String color, double latitude, double longitude){
        this.id = id;
        this.coins = coins;
        this.power = power;
        this.symbol = symbol;
        this.color = color;
        this.position = new Position(latitude, longitude);
    }

    public String getId() {
        return id;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    public Position getPosition() {return this.position;}

    @Override
    public String toString(){
        return  "\nid: " + id +
                "\ncoins: " + coins +
                "\npower: " + power +
                "\nsymbol: " + symbol +
                "\ncolor: " + color +
                "\ncoords: " + position.getLatitude() +
                ", " + position.getLongitude();
    }

}
