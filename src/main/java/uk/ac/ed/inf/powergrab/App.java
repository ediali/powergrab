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

public class App {

    public static ArrayList<Feature> features = new ArrayList<Feature>();

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static JSONObject readFeatures(String year, String month, String day) throws IOException {
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
            features.add(finalFeature);
        }
        return json;
    }

    private static String writeJson(List<double[]> droneCoords, JSONObject json){
        JSONObject linestringJSON = new JSONObject();
        linestringJSON.put("properties", new HashMap<String, Object>());
        linestringJSON.put("type", "Feature");
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put("coordinates", droneCoords);
        temp.put("type", "LineString");
        linestringJSON.put("geometry", temp);
        json.getJSONArray("features").put(linestringJSON);
        return json.toString();
//        System.out.println(json.toString());
    }

    public static void main( String[] args ) throws IOException {
        List<double[]> statelessCoords = new ArrayList<double[]>();
        List<double[]> statefulCoords = new ArrayList<double[]>();
        JSONObject json = readFeatures(args[2], args[1],args[0]);
//        Drone drone = new Stateless(new Position(55.944425, -3.188396), Long.parseLong(args[3]));
        String droneType = args[6];


        FileWriter txtFile = new FileWriter(String.format("%s-%s-%s-%s.txt",droneType, args[0], args[1],args[2]));
        PrintWriter txtPrinter = new PrintWriter(txtFile);

        FileWriter geoFile = new FileWriter(String.format("%s-%s-%s-%s.geojson",droneType, args[0], args[1],args[2]));
        PrintWriter geoPrinter = new PrintWriter(geoFile);

        long seed =  Long.parseLong(args[5]);
        Drone drone = new Stateful(new Position(Double.parseDouble(args[3]), Double.parseDouble(args[4])));
        int moves = 0;
        statefulCoords.add(drone.currentPosition.getCoords());
        while (drone.power > 0 && moves < 250) {
            double currLatitude = drone.currentPosition.latitude;
            double currLongitude = drone.currentPosition.longitude;
            Direction d = drone.getMove();
            drone.updateCoinsAndPower();
            txtPrinter.printf("%f,%f,%s,%f,%f,%f,%f\n",
                    currLatitude,
                    currLongitude,
                    d.toString(),
                    drone.currentPosition.latitude,
                    drone.currentPosition.longitude,
                    drone.coins,
                    drone.power
                    );
            statefulCoords.add(drone.currentPosition.getCoords());
            moves++;
        }

        txtPrinter.close();

        geoPrinter.print(writeJson(statefulCoords, json));
        geoPrinter.close();

        System.out.println(drone.coins);
    }
}

