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

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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

    public static void main( String[] args ) throws IOException {
        String day = args[0];
        String month = args[1];
        String year = args[2];
        String url = String.format("http://homepages.inf.ed.ac.uk/stg/powergrab/%s/%s/%s/powergrabmap.geojson", year, "10", "11");
        System.out.println(url);
        JSONObject json = readJsonFromUrl(url);
        JSONArray allFeatures = json.getJSONArray("features");
        List<double[]> statelessCoords = new ArrayList<double[]>();

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

        Stateless statelessDrone = new Stateless(new Position(55.944425, -3.188396), Long.parseLong(args[3]));
        int moves = 0;
        statelessCoords.add(statelessDrone.currentPosition.getCoords());
        while (statelessDrone.power > 0 && moves < 250) {
            statelessDrone.getNextDirMove();
            statelessDrone.updateCoinsAndPower();
            statelessCoords.add(statelessDrone.currentPosition.getCoords());
            moves++;
        }
        JSONObject linestringJSON = new JSONObject();
        linestringJSON.put("properties", new HashMap<String, Object>());
        linestringJSON.put("type", "Feature");
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put("coordinates", statelessCoords);
        temp.put("type", "LineString");
        linestringJSON.put("geometry", temp);
        json.getJSONArray("features").put(linestringJSON);
        System.out.println(json.toString());
        System.out.println(statelessDrone.coins);
    }
}

