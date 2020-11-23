package ghost;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GhostTest{

    //simpleTest tests the construction of ghost as well as the keypressed method and debug attribute
    @Test 
    public void simpleTest() {

        HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("map1.txt", collection);
        int[] waka = map.getWakaPosition();
        Waka w = new Waka(1, waka, map, collection);
        ArrayList<String[]> ghosts = map.getGhostPosition();
        int[] ghostcoords = new int[] {Integer.parseInt(ghosts.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};
        int[] modes = new int[]{10, 20, 10, 20, 10};

        int[] a = new int[] {10,10};

        Ghost g = new Ghost(1, ghosts.get(0)[0], ghostcoords, modes, w, 10, collection, map);
        Ghost chaser = new Ghost(1, "chaser", a, modes, w, 10, collection, map);
        Ghost ignore = new Ghost(1, "ignorant", a, modes, w, 10, collection, map);
        Ghost whim = new Ghost(1, "whim", a, modes, w, 10, collection, map);
        
        //tests Ghost constructor
        assertNotNull(g);
        assertNotNull(whim);
        assertNotNull(chaser);
        assertNotNull(ignore);
    
        assertEquals(g.x, 26*16);
        assertEquals(g.startPos[0], 26*16);
        assertEquals(g.cornerTarget[0], 447);

        //tests Ghost key pressed
        g.keyPressed(32);
        assertTrue(g.debug);
        g.keyPressed(32);
        assertFalse(g.debug);

        //tests Ghost tick method
        g.tick();
        assertFalse(g.dead);



    }


    //changeModesTest tests the method changeModes, checking that the number of frames are registered when moved
    //as well as the behavioru when frightMode is true
    @Test
    public void changeModesTest(){
        HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("map1.txt", collection);
        int[] waka = map.getWakaPosition();
        Waka w = new Waka(1, waka, map, collection);
        ArrayList<String[]> ghosts = map.getGhostPosition();
        int[] ghostcoords = new int[] {Integer.parseInt(ghosts.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};
        int[] modes = new int[]{10, 20, 10, 20, 10};

        Ghost g = new Ghost(1, ghosts.get(0)[0], ghostcoords, modes, w, 10, collection, map);

        g.frames = 10;
        g.frightMode = true;
        g.changeModes();
        assertEquals(g.frames, 10);
        g.frames = 600;
        g.changeModes();
        assertEquals(g.frames, 0);

        g.frightMode = false;
        assertEquals(g.mode, 0);
        g.frames = 10*60;
        g.changeModes();
        assertEquals(g.mode, 1);
        g.mode = 4;
        g.frames = 10*60;
        g.changeModes();
        assertEquals(g.mode, 0);

        map.frightMode = true;
        g.move();
        assertEquals(g.frames, 0);
        assertTrue(g.frightMode);
    }


    //test Intersection method, checks that the expected intersections are added to the array list
    @Test 
    public void intersectionsTest(){
        HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("map1.txt", collection);
        int[] waka = map.getWakaPosition();
        Waka w = new Waka(1, waka, map, collection);
        ArrayList<String[]> ghosts = map.getGhostPosition();
        int[] ghostcoords = new int[] {Integer.parseInt(ghosts.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};
        int[] modes = new int[]{10, 20, 10, 20, 10};

        Ghost g = new Ghost(1, ghosts.get(0)[0], ghostcoords, modes, w, 10, collection, map);
        
        assertNull(g.direction);

        ArrayList<String> intOne = g.intersections();
        assertEquals(intOne.size(), 1);

        g.direction = "left";
        ArrayList<String> intTwo = g.intersections();
        assertEquals(intTwo.size(), 0);

        g.direction = "up";
        ArrayList<String> intThree = g.intersections();
        assertEquals(intThree.size(), 1);

        g.move();
        g.direction = "up";
        ArrayList<String> intFour = g.intersections();
        assertEquals(intFour.size(), 2);
    }

    //test intersections using another map
    @Test
    public void intersectionsTestTwo(){
       HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("noFruitMap.txt", collection);
        int[] waka = map.getWakaPosition();
        Waka w = new Waka(1, waka, map, collection);
        ArrayList<String[]> ghosts = map.getGhostPosition();
        int[] ghostcoords = new int[] {Integer.parseInt(ghosts.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};
        int[] modes = new int[]{10, 20, 10, 20, 10};

        Ghost g = new Ghost(1, ghosts.get(0)[0], ghostcoords, modes, w, 10, collection, map);

        assertNull(g.direction);

        ArrayList<String> intOne = g.intersections();
        assertEquals(intOne.size(), 4);

        g.direction = "left";
        ArrayList<String> intTwo = g.intersections();
        assertEquals(intTwo.size(), 2);

        g.direction = "up";
        ArrayList<String> intThree = g.intersections();
        assertEquals(intThree.size(), 2);  

        g.direction = "down";
        ArrayList<String> intFour = g.intersections();
        assertEquals(intFour.size(), 2);   

    }

    //test moveTowards methods by giving it different targets and checking if the right directions are identified in the correct order
    @Test
    public void moveTowardsTest() {
       HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("map2.txt", collection);
        int[] waka = map.getWakaPosition();
        Waka w = new Waka(1, waka, map, collection);
        ArrayList<String[]> ghosts = map.getGhostPosition();
        int[] ghostcoords = new int[] {Integer.parseInt(ghosts.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};
        int[] modes = new int[]{10, 20, 10, 20, 10};

        Ghost g = new Ghost(1, ghosts.get(0)[0], ghostcoords, modes, w, 10, collection, map);
        
        ArrayList<String> dirOne = g.moveTowards(420, 430);
        assertEquals(dirOne.get(0), "down");
        assertEquals(dirOne.get(1), "right");

        ArrayList<String> dirTwo = g.moveTowards(410, 430);
        assertEquals(dirTwo.get(0), "down");
        assertEquals(dirTwo.get(1), "left");
        
        ArrayList<String> dirThree = g.moveTowards(430, 420);
        assertEquals(dirThree.get(0), "right");
        assertEquals(dirThree.get(1), "down");

        ArrayList<String> dirFour = g.moveTowards(430, 410);
        assertEquals(dirFour.get(0), "right");
        assertEquals(dirFour.get(1), "up");

         ArrayList<String> dirFive = g.moveTowards(420, 410);
        assertEquals(dirFive.get(0), "up");
        assertEquals(dirFive.get(1), "right");

        ArrayList<String> dirSix = g.moveTowards(410, 400);
        assertEquals(dirSix.get(0), "up");
        assertEquals(dirSix.get(1), "left");

        ArrayList<String> dirSeven = g.moveTowards(400, 420);
        assertEquals(dirSeven.get(0), "left");
        assertEquals(dirSeven.get(1), "down");

        ArrayList<String> dirEight = g.moveTowards(400, 410);
        assertEquals(dirEight .get(0), "left");
        assertEquals(dirEight .get(1), "up");
    }

    //test setTargets(), sees if the right targets are set for the right ghosts in the right mode.
    @Test
    public void setTargetsTest(){
        HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("map2.txt", collection);
        int[] waka = new int[] {5, 5};
        Waka w = new Waka(1, waka, map, collection);

        int[] modes = new int[]{10, 20, 10, 20, 10};

        int[] a = new int[] {10,10};

        Ghost ambusher = new Ghost(1, "ambusher", a, modes, w, 10, collection, map);
        Ghost chaser = new Ghost(1, "chaser", a, modes, w, 10, collection, map);
        Ghost ignore = new Ghost(1, "ignorant", a, modes, w, 10, collection, map);
        Ghost whim = new Ghost(1, "whim", a, modes, w, 10, collection, map);

        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(ambusher);
        ghosts.add(chaser);
        ghosts.add(ignore);
        ghosts.add(whim);
        
        for (Ghost g : ghosts) {
            g.ghosts = ghosts;
            assertFalse(g.ghosts.isEmpty());
        }

        ambusher.setTargets();
        assertEquals(ambusher.target[0], ambusher.cornerTarget[0]);

        chaser.setTargets();
        assertEquals(chaser.target[0], chaser.cornerTarget[0]);

        ignore.setTargets();
        assertEquals(ignore.target[0], ignore.cornerTarget[0]);

        whim.setTargets();
        assertEquals(whim.target[0], whim.cornerTarget[0]);

        ambusher.scatter = false;
        chaser.scatter = false;
        ignore.scatter = false;
        whim.scatter = false;

        chaser.setTargets();
        assertEquals(chaser.target[0], w.x);

        ignore.setTargets();
        assertEquals(ignore.target[0], ignore.cornerTarget[0]);

        ignore.x = 400;
        ignore.y = 400;
        ignore.setTargets();
        assertEquals(ignore.target[0], w.x);

        whim.ghosts.remove(chaser);
        whim.setTargets();
        assertEquals(whim.target[0], w.x);

        whim.ghosts.add(chaser);

        w.direction = "left";

        w.x = 10;
        w.y = 5;
        ambusher.setTargets();
        assertEquals(ambusher.target[0], 0);
        assertEquals(ambusher.target[1], w.y);

        w.x = 100;
        ambusher.setTargets();
        assertEquals(ambusher.target[0], w.x-64);
        assertEquals(ambusher.target[1], w.y);

        w.x = 10;
        chaser.x = 30;
        chaser.y = 7;
        whim.setTargets();
        assertEquals(whim.target[0], 0);
        assertEquals(whim.target[1], 3);

        chaser.x = 15;
        chaser.y = 7;
        whim.setTargets();
        assertEquals(whim.target[0], 1);
        assertEquals(whim.target[1], 3);

        chaser.x = 15;
        chaser.y = 15;
        whim.setTargets();
        assertEquals(whim.target[0], 1);
        assertEquals(whim.target[1], 0);

        w.direction = "right";

        w.x = 400;
        w.y = 5;
        ambusher.setTargets();
        assertEquals(ambusher.target[0], 447);
        assertEquals(ambusher.target[1], w.y);

        w.x = 100;
        ambusher.setTargets();
        assertEquals(ambusher.target[0], w.x+64);
        assertEquals(ambusher.target[1], w.y);

        w.x = 10;
        chaser.x = 30;
        chaser.y = 7;
        whim.setTargets();
        assertEquals(whim.target[0], 0);
        assertEquals(whim.target[1], 3);

        chaser.x = 15;
        chaser.y = 7;
        whim.setTargets();
        assertEquals(whim.target[0], 9);
        assertEquals(whim.target[1], 3);

        chaser.x = 15;
        chaser.y = 8;
        w.x = 400;
        whim.setTargets();
        assertEquals(whim.target[0], 447);
        assertEquals(whim.target[1], 2);


        w.direction = "up";

        w.x = 10;
        w.y = 100;
        ambusher.setTargets();
        assertEquals(ambusher.target[0], 10);
        assertEquals(ambusher.target[1], 36);

        w.y = 50;
        ambusher.setTargets();
        assertEquals(ambusher.target[0], 10);
        assertEquals(ambusher.target[1], 0);

        chaser.x = 15;
        chaser.y = 8;
        whim.setTargets();
        assertEquals(whim.target[0], 5);
        assertEquals(whim.target[1], 88);

        chaser.x = 30;
        chaser.y = 7;
        whim.setTargets();
        assertEquals(whim.target[0], 0);
        assertEquals(whim.target[1], 89);

        chaser.x = 10;
        chaser.y = 300;
        whim.setTargets();
        assertEquals(whim.target[0], 10);
        assertEquals(whim.target[1], 0);


        w.direction = "down";

        w.x = 10;
        w.y = 550;
        ambusher.setTargets();
        assertEquals(ambusher.target[0], 10);
        assertEquals(ambusher.target[1], 575);

        w.y = 100;
        ambusher.setTargets();
        assertEquals(ambusher.target[0], 10);
        assertEquals(ambusher.target[1], 164);

        chaser.x = 10;
        chaser.y = 30;
        whim.setTargets();
        assertEquals(whim.target[0], 10);
        assertEquals(whim.target[1], 174);

        chaser.x = 30;
        chaser.y = 300;
        w.y = 500;
        whim.setTargets();
        assertEquals(whim.target[0], 0);
        assertEquals(whim.target[1], 575);

    }


    //test move(), checks that ghosts move as they should.
    @Test
    public void moveTest(){
        HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("map1.txt", collection);
        int[] waka = map.getWakaPosition();
        Waka w = new Waka(1, waka, map, collection);
        ArrayList<String[]> ghosts = map.getGhostPosition();
        int[] ghostcoords = new int[] {Integer.parseInt(ghosts.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};
        int[] modes = new int[]{10, 20, 10, 20, 10};

        Ghost g = new Ghost(1, ghosts.get(0)[0], ghostcoords, modes, w, 10, collection, map);
        g.move();
        assertEquals(g.x, 26*16-1);

        g.move();
        assertEquals(g.x, 26*16-2);

        Map map2 = new Map("map2.txt", collection);
        int[] waka2 = map.getWakaPosition();
        Waka w2 = new Waka(1, waka2, map2, collection);
        ArrayList<String[]> ghosts2 = map2.getGhostPosition();
        int[] ghostcoords2 = new int[] {Integer.parseInt(ghosts2.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};

        Ghost g2 = new Ghost(1, "chaser", ghostcoords2, modes, w2, 10, collection, map2);
        
        g2.scatter = false;

        g2.direction = "left";
        g2.move();
        assertEquals(g2.x, 26*16-1);


        w2.x = 26*16-2;
        g2.move();
        assertEquals(g2.x, 26*16-2);

    }

    //test caught(), checks that ghosts are caught and they react right both outside and inside of frightMode.
    @Test
    public void caughtTest(){

        App game = new App();

        HashMap<String, PImage> collection = new HashMap<String, PImage>();

        Map map = new Map("map1.txt", collection);
        int[] waka = map.getWakaPosition();
        Waka w = new Waka(1, waka, map, collection);
        ArrayList<String[]> ghosts = map.getGhostPosition();
        int[] modes = new int[]{10, 20, 10, 20, 10};
        int[] ghostcoords = new int[] {Integer.parseInt(ghosts.get(0)[1]), Integer.parseInt(ghosts.get(0)[2])};

        Ghost g = new Ghost(1, ghosts.get(0)[0], ghostcoords, modes, w, 10, collection, map);

        g.ghosts.add(g);

        assertEquals(g.x, 416);
    
        g.moveRight();
        assertEquals(g.x, 417);

        g.caught();

        assertEquals(g.x, 416);
        assertEquals(w.lives, 2);

        g.frightMode = true;
        g.caught();
        assertTrue(g.dead);


    }

}