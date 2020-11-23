package ghost;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.util.HashMap;

/**
* Game object controlled by the player.
*/
public class Waka extends Character implements Functions{
    /**
    * Direction of waka's movement - direction of waka's image.
    */
    public String picDir;

    /**
    * Whether waka's mouth is closed or open.
    */
    public boolean closed;

    /**
    * Number of waka lives.
    */
    public int lives;

    /**
    * Constructor of Waka. 
    * Requires speed of movement, x and y coordinates, game map and collection of graphic. 
    * Waka closed is defaults to false, waka life defaults to 3 and previous and current direction defaulted to left. 
    * @param speed Waka's speed
    * @param coords Waka's coordinates
    * @param map Game map
    * @param collection Collection of PImage graphics
    */
    public Waka(int speed, int[] coords, Map map, HashMap<String, PImage> collection){
        super(speed, "left", coords, collection, map);
        this.picDir = "left";
        this.closed = false;
        this.lives = 3;
    }

    /**
    * Displays a graphic of waka which corresponds to its current direction. 
    * This will alternate between closed and open every 8 frames.
    * @param app instance of the application
     */
    public void draw(PApplet app) {
        if (this.isClosed(app.frameCount%8==0)) {
            app.image(this.collection.get("closed"),this.x-5, this.y-5);
        } else {
            app.image(this.collection.get(this.picDir), this.x-5, this.y-5);
        }
        for (int life = 0; life<this.lives; life++){
            app.image(this.collection.get("right"),life*26+5, 34*16);
        }
    }

    /**
    * Checks waka's current direction to see if a move can be made. 
    * If the move is available, then it is made and the previous direction will be the current direction.
    * If not then waka's previous direction will be used and Waka's position is changed. 
     */
    public void move(){

        if (this.checkMove(this.direction)){
            this.makeMove(this.direction);
            this.picDir = this.direction;
        } else {
            if(this.checkMove(this.picDir)){
                this.makeMove(this.picDir);
            }
        }
    }

    /**
    * Returns if the waka's graphic should reflect a closed or open mouth.
    * The parameter 'change' will be true every 8 frames, changing the opening/closing of the mouth
    * @param change depends on the number of frames
    * @return true or false depending on the state of the attribute 'closed'
    */
    public boolean isClosed(boolean change){
        if(change){
            this.closed = !this.closed;
        }
        return this.closed;
    }

    /**
    * Handles key presses from the main application.
    * Waka's direction will change depending on player's key press
    * @param keyCode is the keyCode of the key pressed
    */
    public void keyPressed(int keyCode){
        if(keyCode == 37){
            this.direction = "left";
        }
        else if(keyCode == 38){
            this.direction = "up";
        }
        else if(keyCode == 39){
            this.direction = "right";
        }
        if(keyCode == 40){
            this.direction = "down";
        }
    }

    /**
    *When Waka is eaten by Ghost it will restart in its original position.
    *The direction will return to the default direction and the lives will decrease by one.
    */
    public void caught(){
        this.x = this.startPos[0];
        this.y = this.startPos[1];
        this.direction = "left";
        this.lives--;
    }

}