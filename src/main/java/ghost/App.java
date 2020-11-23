package ghost;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.util.HashMap;
import processing.data.JSONObject;
import processing.data.JSONArray;
import java.util.ArrayList;


/**
* Game application.
*/
public class App extends PApplet {

    /**
    * Width of the board.
    */
    public static final int WIDTH = 448;

    /**
    * Height of the board.
    */
    public static final int HEIGHT = 576;

    /**
    * Game map.
    */
    public Map map;

    /**
    * Waka controlled by the player.
    */
    public Waka waka;

    /**
    * Time passed for the result screens.
    */
    public int time;

    /**
    * Font of the result screens.
    */
    public PFont font;

    /**
    * Array list containing the ghosts in the map.
    */
    public ArrayList<Ghost> ghosts;

    /**
    * Constructor for the game.
    * It doesn't need any inputs as all attributes will be set to the default value of null. 
    * A hashmap and arraylist will be created for collection and ghosts respectively.
    */
    public App(){
        this.waka = null;
        this.ghosts = new ArrayList<Ghost>();
        this.time = 0;
        this.font = null;
        this.map = null;
    }

    /**
    * Setting up the game, including instantiating game objects.
    * Setup will read and load all graphics from the resource folder and create instances of the Waka and Ghost objects
    */
    public void setup() {
        frameRate(60);
        textFont(createFont("src/main/resources/PressStart2P-Regular.ttf", 30));
        textAlign(CENTER);
        
        HashMap<String, PImage> collection = new HashMap<String, PImage>();
        
        collection.put("1", loadImage("src/main/resources/horizontal.png", "png"));
        collection.put("2", loadImage("src/main/resources/vertical.png", "png"));
        collection.put("3", loadImage("src/main/resources/upLeft.png", "png"));
        collection.put("4", loadImage("src/main/resources/upRight.png", "png"));
        collection.put("5", loadImage("src/main/resources/downLeft.png", "png"));
        collection.put("6", loadImage("src/main/resources/downRight.png", "png"));
        collection.put("7", loadImage("src/main/resources/fruit.png", "png"));
        collection.put("8", loadImage("src/main/resources/superFruit.png", "png"));
        collection.put("right", loadImage("src/main/resources/playerRight.png", "png"));
        collection.put("left", loadImage("src/main/resources/playerLeft.png", "png"));
        collection.put("up", loadImage("src/main/resources/playerUp.png", "png"));
        collection.put("down", loadImage("src/main/resources/playerDown.png", "png"));
        collection.put("closed", loadImage("src/main/resources/playerClosed.png", "png"));
        collection.put("ambusher", loadImage("src/main/resources/ambusher.png", "png"));
        collection.put("chaser", loadImage("src/main/resources/chaser.png", "png"));
        collection.put("ignorant", loadImage("src/main/resources/ignorant.png", "png"));
        collection.put("whim", loadImage("src/main/resources/whim.png", "png"));
        collection.put("frightened", loadImage("src/main/resources/frightened.png", "png"));

        JSONObject json = loadJSONObject("config.json");
        int speed = (int) json.get("speed");
        int fright = (int) json.get("frightenedLength");

        int[] modes = new int[json.getJSONArray("modeLengths").size()];
        for (int i = 0; i < json.getJSONArray("modeLengths").size(); i++) {
            modes[i] = (int) json.getJSONArray("modeLengths").get(i);
        }
        
        this.map = new Map((String) json.get("map"), collection);

        int[] coords = map.getWakaPosition();
        this.waka = new Waka(speed, coords, this.map, collection);

        ArrayList<String[]> ghostPos = map.getGhostPosition();

        for (int i = 0; i<ghostPos.size(); i++){
            int[] ghostcoords = new int[] {Integer.parseInt(ghostPos.get(i)[1]), Integer.parseInt(ghostPos.get(i)[2])};
            this.ghosts.add(new Ghost(speed, ghostPos.get(i)[0], ghostcoords, modes, this.waka, fright, collection, this.map));
        }
        for (Ghost g : this.ghosts){
            g.ghosts = this.ghosts;
        }
    }

    /**
    * Sets the width and height of the display to the predetermined width and height.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
    * Renders all the graphics to the screen.
    */
    public void draw() { 
        background(0, 0, 0); 
        if (this.end()) {
            return;
        }
        this.map.tick(this.waka);
        this.map.draw(this);

        this.waka.move();
        this.waka.draw(this);

        for (Ghost g : this.ghosts){
            g.tick();
            g.draw(this);
        }
    }


    /**
    * Checks if it is the end of the game.
    * Identifies if player has lost or won and displays the approapriate screen to the screen for 10 seconds before restarting the game.
    * @return true or false, depending on whether the game has ended.
    */
    public boolean end() {
        if (this.waka.lives == 0 || this.map.end) {
            if (this.time == 10*60){
                this.waka = null;
                this.ghosts = new ArrayList<Ghost>();
                this.time = 0;
                this.map = null;
                this.setup();
            } 
            if (this.map.end) {
                text("YOU WIN",this.width/2,2*this.height/5); 
            } else {
                text("GAME OVER",this.width/2,2*this.height/5); 
            }
            this.time ++;
            return true;
        }
        return false;
    }

    /**
    * If a key has been pressed call Waka and Ghosts to react accordingly.
    */
    public void keyPressed(){
        for (Ghost g : this.ghosts){
            g.keyPressed(keyCode);
        }
        this.waka.keyPressed(keyCode);
    }

    /**
    * Main method of the class.
    * @param args command argument
    */
    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }

}
