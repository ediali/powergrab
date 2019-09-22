package uk.ac.ed.inf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class App 
{
    public static void main( String[] args )
    {
        try {
            URL url = new URL("http://homepages.inf.ed.ac.uk/stg/powergrab/2019/09/22/powergrabmap.geojson");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = "";
            while (null != (str = br.readLine())) {
                System.out.println(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }    }
}
