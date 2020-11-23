package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WakaTest{

    //tests all of waka's methods and movements, not including the methods inherited from Character
    @Test 
    public void simpleTest() {

        HashMap<String, PImage> collection = new HashMap<String, PImage>();
        
        Map m = new Map("map1.txt", collection);
        int[] w = m.getWakaPosition();

        Waka waka = new Waka(1, w, m, collection);
        assertNotNull(waka);

        waka.move();
        assertEquals(waka.x, w[0]*16);

        waka.picDir = "right";
        waka.move();
        assertEquals(waka.x, (w[0]*16)+1);

        waka.direction = "right";
        waka.move();
        assertEquals(waka.x, (w[0]*16)+2);


        assertFalse(waka.closed);
        assertFalse(waka.isClosed(false));
        assertTrue(waka.isClosed(true));
        assertFalse(waka.isClosed(true));

        waka.keyPressed(38);
        assertEquals(waka.direction, "up");
        assertEquals(waka.picDir, "right");

        waka.keyPressed(37);
        assertEquals(waka.direction, "left");

        waka.keyPressed(39);
        assertEquals(waka.direction, "right");

        waka.keyPressed(40);
        assertEquals(waka.direction, "down");

        waka.caught();
        assertEquals(waka.x, w[0]*16);
        assertEquals(waka.y, w[1]*16);
        assertEquals(waka.lives, 2);

    }

}