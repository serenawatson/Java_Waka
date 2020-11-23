package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.util.HashMap;

import java.util.ArrayList;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MapTest{

    //tests all of map's methods using 3 different maps
    @Test 
    public void simpleTest() {

        HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map m = new Map("map1.txt", collection);
        assertNotNull(m);

        assertEquals(m.matrix.length, 36);
        assertEquals(m.matrix[0].length, 28);
        assertEquals(m.map.length, 576);
        assertEquals(m.map[0].length, 448);

        assertEquals(m.matrix[0][0], "0");
        assertTrue(m.map[0][0]);

        int[] waka = m.getWakaPosition();
        assertEquals(waka.length, 2);

        Waka w = new Waka(1, waka, m, collection);

        Map m2 = new Map("map.txt", collection);

        ArrayList<String[]> ghosts = m2.getGhostPosition();
        assertEquals(ghosts.size(), 4);

        m.haveFruit();
        assertFalse(m.end);

        Map m3 = new Map("noFruitMap.txt", collection);

        m3.haveFruit();
        assertTrue(m3.end);

        m.updateMatrix(4*16, 26*16);
        assertEquals(m.matrix[26][4], "0");

        m.updateMatrix(3*16, 26*16);
        assertEquals(m.matrix[26][3], "0");
        assertTrue(m.frightMode);
        m.frightMode = false;

        boolean left = m.Move(w.x-1, w.y);
        assertFalse(left);

        boolean right = m.Move(w.x+1, w.y);
        assertTrue(right);

        boolean[][] map = m.getMap();
        assertEquals(map.length, 576);

        m.frightMode = true;
        m.tick(w);
        assertFalse(m.frightMode);


    }



}