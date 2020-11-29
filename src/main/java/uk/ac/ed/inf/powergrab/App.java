package uk.ac.ed.inf.powergrab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The class which reads the json files and runs either of the two drones and writes the output in txt and json format.
 */
public class App {

    /**
     * Reads the json text from the buffered reader
     * @param rd Reader
     * @return String
     * @throws IOException exception
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    /**
     * Returns a json Object containing all the features.
     * @param year The year of the map
     * @param month The month of the map
     * @param day The day of the map
     * @return JSON Object
     * @throws IOException exception
     */
    public static JSONObject readFeatures(String year, String month, String day) throws IOException {
        String url = String.format("http://homepages.inf.ed.ac.uk/stg/powergrab/%s/%s/%s/powergrabmap.geojson", year, month, day);
        System.out.println(url);
        JSONObject json = readJsonFromUrl(url);
        JSONArray allFeatures = json.getJSONArray("features");

        for (Object feature : allFeatures) {
            JSONObject f = new JSONObject(feature.toString());
            JSONObject geometry = f.getJSONObject("geometry");
            JSONArray coords = geometry.getJSONArray("coordinates");
            JSONObject properties = f.getJSONObject("properties");
            String id = properties.get("id").toString();
            double coins = Double.parseDouble(properties.get("coins").toString());
            double power = Double.parseDouble(properties.get("power").toString());
            String symbol = properties.get("marker-symbol").toString();
            String color = properties.get("marker-color").toString();

            Feature finalFeature = new Feature(id, coins, power, symbol, color, coords.getDouble(1), coords.getDouble(0));
            Utils.getFeatures().add(finalFeature);
        }
        return json;
    }

    /**
     * Writes a json file containing a linestring.
     * @param droneCoords coordinates of the drone
     * @param json json object
     * @return String
     */
    private static String writeJson(List<double[]> droneCoords, JSONObject json){
        JSONObject linestringJSON = new JSONObject();
        linestringJSON.put("properties", new HashMap<String, Object>());
        linestringJSON.put("type", "Feature");
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("coordinates", droneCoords);
        temp.put("type", "LineString");
        linestringJSON.put("geometry", temp);
        json.getJSONArray("features").put(linestringJSON);
        return json.toString();
    }

    /**
     *Initialize file writers and run the drone given the command line arguments. Then save all the drone moves to the
     * appropriate files.
     * @param args command line arguments
     * @throws IOException exception
     */
    public static void main( String[] args ) throws IOException {
        List<double[]> droneCoords = new ArrayList<>();

        JSONObject json = readFeatures(args[2], args[1],args[0]);
        String droneType = args[6];

        FileWriter txtFile = new FileWriter(String.format("%s-%s-%s-%s.txt",droneType, args[0], args[1],args[2]));
        PrintWriter txtPrinter = new PrintWriter(txtFile);

        FileWriter geoFile = new FileWriter(String.format("%s-%s-%s-%s.geojson",droneType, args[0], args[1],args[2]));
        PrintWriter geoPrinter = new PrintWriter(geoFile);

        Drone drone = null;

        switch (droneType){
            case "stateless":
                drone = new Stateless(new Position(Double.parseDouble(args[3]), Double.parseDouble(args[4])), Long.parseLong(args[5]));
                break;
            case "stateful":
                drone = new Stateful(new Position(Double.parseDouble(args[3]), Double.parseDouble(args[4])));
                break;
        }

        int moves = 0;
        assert drone != null;
        droneCoords.add(drone.currentPosition.getCoords());

        while (drone.power > 0 && moves < 250) {
            double currLatitude = drone.currentPosition.getLatitude();
            double currLongitude = drone.currentPosition.getLongitude();
            Direction d = drone.getMove();
            drone.updateCoinsAndPower();
            txtPrinter.printf("%f,%f,%s,%f,%f,%f,%f\n",
                    currLatitude,
                    currLongitude,
                    d.toString(),
                    drone.currentPosition.getLatitude(),
                    drone.currentPosition.getLongitude(),
                    drone.coins,
                    drone.power
                    );
            droneCoords.add(drone.currentPosition.getCoords());
            moves++;
        }

        txtPrinter.close();

        geoPrinter.print(writeJson(droneCoords, json));
        geoPrinter.close();

        System.out.println(drone.coins);
    }
}

