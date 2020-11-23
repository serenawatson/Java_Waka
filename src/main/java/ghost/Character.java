package ghost;
import java.lang.Math;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.HashMap;


/**
* Character objects within the game.
*/
public class Character{

    /**
    * Speed of the character.
     */
    protected int speed;

    /**
    * Direction which the character is moving.
     */
    protected String direction;

    /**
    * Starting positions for the character.
     */
    protected int[] startPos;

    /**
    * Character's x coordinate.
     */
    protected int x;

    /**
    * Character's y coordinate.
     */
    protected int y;

    /**
    * Whether superfruit has been eaten or not.
     */
    protected boolean frightMode;

    /**
    * Collection containing graphics of the game.
     */
    protected HashMap<String, PImage> collection;

    /**
    * Game map.
     */
    protected Map map;
    

    /** 
    * Constructor of Character.
    * Requires speed, direction, coordinates, graphics collection and game map.
    * By default frightMode is false, the coordinates of the characters are paramter coordinates multiplied by 16 for map scaling.
    * @param speed of the character
    * @param direction direction of the Character
    * @param coords character coordinates
    * @param map game map
    * @param collection collection of graphics
     */
    public Character(int speed, String direction, int[] coords, HashMap<String, PImage> collection, Map map){
        this.speed = speed;
        this.x = coords[0]*16;
        this.y = coords[1]*16;
        this.startPos = new int[] {this.x, this.y};
        this.direction = direction;
        this.frightMode = false;
        this.collection = collection;
        this.map = map;
    }

    /** 
    * Checks if the character could make a specific move on the map.
    * @param direction direction which the character wants to go.
    * @return true or false, depending on whether the character could move in a particular direction.
    */
    public boolean checkMove(String direction){
        if (direction.equals("left")) {
            return this.checkLeft();
        }
        else if (direction.equals("right")) {
            return this.checkRight();
        }
        else if (direction.equals("up")) {
            return this.checkUp();
        }
        else if (direction.equals("down")) {
            return this.checkDown();
        }
        return false;
    }

    /** 
    * Checks if the character could move left on the map.
    * @return true or false, depending on whether the character could move left.
    */
    public boolean checkLeft(){
        if (this.map.Move(this.x-this.speed, this.y)){
            if (this.map.Move(this.x-this.speed, this.y+15)){
                return true;
            }
        }
        return false;
    }

    /** 
    * Checks if the character could move right on the map.
    * @return true or false, depending on whether the character could move right.
    */
    public boolean checkRight(){
        if (this.map.Move(this.x+16, this.y)){
            if (this.map.Move(this.x+16, this.y+15)){
                return true;
            }
        }
        return false;
    }

    /** 
    * Checks if the character could move up on the map.
    * @return true or false, depending on whether the character could move up.
    */
    public boolean checkUp(){
        if (this.map.Move(this.x, this.y-this.speed)){
            if (this.map.Move(this.x+15, this.y-this.speed)){
                return true;
            }
        }
        return false;
    }

    /** 
    * Checks if the character could move down on the map.
    * @return true or false, depending on whether the character could move down.
    */
    public boolean checkDown(){
        if (this.map.Move(this.x, this.y+16)){
            if (this.map.Move(this.x+15, this.y+16)){
                return true;
            }
        }
        return false;
    }


    /** 
    * Moving the character in a certain direction.
    * @param direction which the character should move.
    */
    public void makeMove(String direction){
        if (direction.equals("left")) {
            this.moveLeft();
        }
        else if (direction.equals("right")) {
            this.moveRight();
        }
        else if (direction.equals("up")) {
            this.moveUp();
        }
        else if (direction.equals("down")) {
            this.moveDown();
        }
        return;
    }

    /** 
    * Moves the character left.
    */
    public void moveLeft(){
        this.x -= this.speed;
    }

    /** 
    * Moves the character right.
    */
    public void moveRight(){
        this.x += this.speed;
    }

    /** 
    * Moves the character up.
    */
    public void moveUp(){
        this.y -= this.speed;
    }

    /** 
    * Moves the character down.
    */
    public void moveDown(){
        this.y += this.speed;
    }

}