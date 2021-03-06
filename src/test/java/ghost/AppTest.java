/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ghost;

import processing.core.PImage;
import processing.core.PFont;
import processing.core.PApplet;
import java.util.HashMap;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class AppTest{
    @Test 
    public void simpleTest() {

        //Test the creation of an App instance.
        App game = new App();
        assertNotNull(game);
        assertNull(game.map);

        String[] main = new String[2];

        game.main(main);

        HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("map.txt", collection);
        int[] waka = map.getWakaPosition();
        Waka w = new Waka(1, waka, map, collection);
        ArrayList<String[]> ghosts = map.getGhostPosition();
        int[] modes = new int[]{10, 20, 10, 20, 10};
        int[] ghostcoords = new int[] {Integer.parseInt(ghosts.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};

        Ghost g = new Ghost(1, "chaser", ghostcoords, modes, w, 10, collection, map);
        
        w.lives = 0;


        game.map = map;
        game.ghosts.add(g);
        game.waka = w;

        //tests app keyPressed()
        game.keyCode = 32;
        game.keyPressed();
        assertTrue(game.ghosts.get(0).debug);

        game.keyCode = 37;
        game.keyPressed();
        assertEquals(game.waka.direction, "left");

        game.main(main);

    }
}
