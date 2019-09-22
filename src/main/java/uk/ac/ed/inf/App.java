package uk.ac.ed.inf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class App 
{
    public static void main( String[] args ) {
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        try {
            String temp = String.format("http://homepages.inf.ed.ac.uk/stg/powergrab/%d/%d/%d/powergrabmap.geojson", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            URL url = new URL("http://homepages.inf.ed.ac.uk/stg/powergrab/2019/09/22/powergrabmap.geojson");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder str = new StringBuilder();
            while (null != (br.readLine())) {
                str.append(br.readLine());
                str.append('\n');
            }
            System.out.println(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
