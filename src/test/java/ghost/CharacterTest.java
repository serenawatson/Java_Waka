package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest{
    @Test 
    public void simpleTest() {
        App game = new App();

        HashMap<String, PImage> collection = new HashMap<String, PImage>();
        
        //tests map constructor
        Map m = new Map("map1.txt", collection);
        assertNotNull(m);
        assertTrue(collection.isEmpty());

        Map m2 = new Map("map2.txt", collection);

        int[] w = m.getWakaPosition();
        int[] w2 = m2.getWakaPosition();

        //tests character constructor
        Character c = new Character(1, "right", w, collection, m);
        Character c2 = new Character(1, "right", w2, collection, m2);
        assertNotNull(c);

        //tests checkMove() method
        //this will indirectly test all of the check___() methods
        assertFalse(c.checkMove("left"));
        assertFalse(c.checkMove("up"));
        assertFalse(c.checkMove("down"));
        assertTrue(c.checkMove("right"));
        assertFalse(c.checkMove("hello"));

        c.x = 416;
        assertFalse(c.checkMove("right"));

        
        assertTrue(c2.checkMove("right"));
        assertTrue(c2.checkMove("up"));
        assertTrue(c2.checkMove("left"));
        assertTrue(c2.checkMove("down"));

        //tests makeMove() method
        //this will indirectly test all of the make___() methods
        c2.makeMove("left");
        assertEquals(c2.x, w2[0]*16-1);
        c2.makeMove("up");
        assertEquals(c2.x, w2[0]*16-1);
        assertEquals(c2.y, w2[1]*16-1);
        c2.makeMove("down");
        assertEquals(c2.x, w2[0]*16-1);
        assertEquals(c2.y, w2[1]*16);
        c2.makeMove("right");
        assertEquals(c2.x, w2[0]*16);
        assertEquals(c2.y, w2[1]*16);
        c2.makeMove("hello");
        assertEquals(c2.x, w2[0]*16);
        assertEquals(c2.y, w2[1]*16);
    }



}