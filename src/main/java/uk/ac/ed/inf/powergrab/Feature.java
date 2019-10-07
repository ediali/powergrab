package uk.ac.ed.inf.powergrab;

public class Feature {

    public final String id;
    public double coins;
    public double power;
    public String symbol;
    public String color;
    public double latitude;
    public double longitude;

    public Feature(String id, double coins, double power, String symbol, String color, double latitude, double longitude){
        this.id = id;
        this.coins = coins;
        this.power = power;
        this.symbol = symbol;
        this.color = color;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString(){
        return  "\nid: " + id +
                "\ncoins: " + coins +
                "\npower: " + power +
                "\nsymbol: " + symbol +
                "\ncolor: " + color +
                "\ncoords: " + latitude +
                ", " + longitude;
    }

}
