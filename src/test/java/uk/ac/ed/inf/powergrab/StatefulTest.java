package uk.ac.ed.inf.powergrab;

import org.json.JSONObject;
import org.junit.Before;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.*;

public class StatefulTest {


    @Test
    public void testNeverHitNegativeStation() throws IOException {
        App.readFeatures("2019", "06", "04");
        Stateful drone = new Stateful(new Position(55.944425, -3.188396));
        int moves = 0;
        double prevCoins = 0;
        while (drone.power > 0 && moves < 250) {
            prevCoins = drone.coins;
            drone.getMove();
            drone.updateCoinsAndPower();
            assertTrue(drone.coins >= prevCoins);
            moves++;
        }
        System.out.println(drone.coins);

    }

    @Test
    public void testNeverGoOutOfBounds() throws IOException {
        App.readFeatures("2019", "06", "04");
        Stateful drone = new Stateful(new Position(55.944425, -3.188396));
        int moves = 0;
        while (drone.power > 0 && moves < 250) {
            drone.getMove();
            assertTrue(drone.currentPosition.inPlayArea());
            drone.updateCoinsAndPower();
            moves++;
        }
        System.out.println(drone.coins);
    }


}