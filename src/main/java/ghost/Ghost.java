package ghost;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Random; 
import java.util.HashMap;


/**
* Ghost object within the game, Waka's antagonist.
*/
public class Ghost extends Character implements Functions{
    /**
    * Type of ghost.
    */
    public String type;

    /**
    * Array of mode lengths.
    */
    public int[] modes;

    /**
    * Current mode.
    */
    public int mode;

    /**
    * Number of frames left in the mode.
    */
    public int frames;

    /**
    * Whether the ghosts are in scatter or chase mode.
    */
    public boolean scatter;

    /**
    * The player's waka.
    */
    public Waka waka;

    /**
    * Ghost's current target.
    */
    public int[] target;

    /**
    * Ghost's corner targets.
    */
    public int[] cornerTarget;

    /**
    * Whether debug mode is on or not.
    */
    public boolean debug;

    /**
    * ArrayList of all the ghosts in the map.
    */
    public ArrayList<Ghost> ghosts;

    /**
    * Length of frightMode;
    */
    public int frightLength;

    /**
    * If ghost is dead.
    */
    public boolean dead;


    /**
    * Constructor of Ghost. 
    * Requires speed of movement, ghost type, ghost coordinates, ghost modes, player's waka, length of frightMode, game map and collection of graphic. 
    * By default mode is 0, scatter is true, frames is 0, debug is false, dead is false, ghosts is an empty array list, target is an array and corner target is dependent on the type of ghosts.
    * @param speed Ghost's speed
    * @param type Ghost's type
    * @param coords Ghosts's coordinates
    * @param modes Ghost's modes
    * @param waka player's waka
    * @param frightLength length of frightMode
    * @param map Game map
    * @param collection Collection of PImage graphics
    */
    public Ghost(int speed, String type, int[] coords, int[] modes, Waka waka, int frightLength, HashMap<String, PImage> collection,  Map map){
        super(speed, null, coords, collection, map);
        this.type = type;
        this.modes = modes;
        this.mode = 0;
        this.scatter = true;
        this.frames = 0;
        this.waka = waka;
        this.debug = false;
        this.dead = false;
        this.frightLength = frightLength;
        this.ghosts = new ArrayList<Ghost>();
        if (type.equals("chaser")) {
            this.cornerTarget = new int[] {0, 0};
        }
        else if (type.equals("ambusher")) {
            this.cornerTarget = new int[] {447, 0};
        } else if (type.equals("ignorant")) {
            this.cornerTarget = new int[] {0, 575};
        } else if (type.equals("whim")) {
            this.cornerTarget = new int[] {447, 575};
        }
        this.target = new int[2];
    }

    /**
    * Calls the move method for the ghost if it is not dead.
    */
    public void tick(){
        if (!this.dead) {
            this.move();
        }
    }

    /**
    * Renders ghost displays.
    * Displays the ghosts if they are not dead.
    * If waka has eaten superfruit then frightmode is true and frightened sprite will be displayed.
    * If debug mode is true then a line between ghost and their target will also be displayed.
    * @param app instance of the game application
    */
    public void draw(PApplet app){
        if (!this.dead) {
            if (this.frightMode) {
            app.image(this.collection.get("frightened"), this.x-6, this.y-6);
            } else {
                app.image(this.collection.get(this.type), this.x-6, this.y-6);
                if (this.debug == true) {
                    app.line(this.target[0] + 12, this.target[1] + 12, this.x + 12, this.y + 12);
                    app.stroke(225);
                }
            }
        }
    }
    
    /**
    * Changes the modes of ghosts.
    * Turns frightMode off after the length determined in the json document.
    * Changes ghost mode between scatter or chase depending on the number of frames passed.
    */
    public void changeModes(){
        if (this.frightMode) {
            if (this.frames == this.frightLength*60) {
                this.frames = 0;
                this.frightMode = false;
            }
        }
        else if ((this.modes[this.mode])*60==this.frames){
            this.mode++;
            this.scatter = !this.scatter;
            this.frames = 0;
            if (this.mode>=this.modes.length){
                this.mode = 0;
            }
        }
    }

    /**
    * Moves the ghost.
    * If frightMode is true, then the ghosts will move randomly.
    * Otherwise if the ghost is at an intersection, then it will move in the direction closest to its target.
    * If this direction is not possible then it will move in another possible direction. 
    * If the ghost is not at an intersection, if will continue moving in its current direction, until an intersection is reached.
    * If ghost is in the same grid as waka then ghost has caught waka and caught() is called. 
    */
    public void move(){
        this.frames ++;
        if (this.map.frightMode) {
            this.frightMode = true;
            this.frames = 0;
        }
        this.setTargets();
        ArrayList<String> intersects = this.intersections();
        boolean made = false;
        if (!intersects.isEmpty()){
            if(this.direction != null && checkMove(this.direction)){
                intersects.add(this.direction);
            }
            if (this.frightMode) {
                Random rand = new Random();
                String d = intersects.get(rand.nextInt(intersects.size())); 
                this.makeMove(d);
                this.direction = d;
            }
            else {
                ArrayList<String> directions = this.moveTowards(this.target[0], this.target[1]);
                for (String dir : directions) {
                    if (intersects.contains(dir)) {
                        this.makeMove(dir);
                        this.direction = dir;
                        made = true;
                        break;
                    }
                }
                if (!made) {
                    this.makeMove(intersects.get(0));
                    this.direction = intersects.get(0);
                }
            }
        }
        else if (this.checkMove(this.direction)){
            this.makeMove(this.direction);
        }
        this.changeModes();
        if (Math.round(this.waka.x/16) == Math.round(this.x/16) && Math.round(this.waka.y/16) == Math.round(this.y/16)) {
            this.caught();
        }
    }

    /**
    * Sets ghosts targets.
    * If scatter mode is on then ghost targets are their respective corners.
    * Otherwise targets are set based on ghost type.
    * Ambusher: Four grid spaces ahead of Waka (based on Waka’s current direction). 
    * Chaser: Waka's position.
    * Ignorant: If more than 8 units away from Waka (straight line distance), target location is Waka. Otherwise, target location is bottom left corner.
    * Whim: Double the vector from Chaser to 2 grid spaces ahead of Waka.
    * If any targets are out of bounds, they are changed to the closets coordinates within the boundary of the display grid.
    */
    public void setTargets(){ 
        if (this.scatter){
            this.target[0] = this.cornerTarget[0];
            this.target[1] = this.cornerTarget[1];
        } else {
            if (this.type.equals("ambusher")) {
                if (this.waka.direction.equals("left")) {
                    if (this.waka.x - 4*16 > 0) {
                        this.target[0] = this.waka.x-4*16;
                        this.target[1] = this.waka.y;
                    } else {
                        this.target[0] = 0;
                        this.target[1] = this.waka.y;
                    }
                } else if (this.waka.direction.equals("right")) {
                    if (this.waka.x + 4*16 < 448) {
                        this.target[0] = this.waka.x+4*16;
                        this.target[1] = this.waka.y;
                    } else {
                        this.target[0] = 447;
                        this.target[1] = this.waka.y;
                    }
                } else if (this.waka.direction.equals("up")) {
                    if (this.waka.y - 4*16 > 0) {
                        this.target[0] = this.waka.x;
                        this.target[1] = this.waka.y-4*16;
                    } else {
                        this.target[0] = this.waka.x;
                        this.target[1] = 0;
                    }
                } else if (this.waka.direction.equals("down")) {
                    if (this.waka.y + 4*16 < 576) {
                        this.target[0] = this.waka.x;
                        this.target[1] = this.waka.y+4*16;
                    } else {
                        this.target[0] = this.waka.x;
                        this.target[1] = 575;
                    }
                }
            } else if (this.type.equals("chaser")) {
                this.target[0] = this.waka.x;
                this.target[1] = this.waka.y;
            } else if (this.type.equals("ignorant")) {
                if (Math.sqrt(Math.pow(Math.abs(this.x - this.waka.x), 2)+Math.pow(Math.abs(this.y - this.waka.y), 2)) > 8*16) {
                    this.target[0] = this.waka.x;
                    this.target[1] = this.waka.y;
                } else {
                    this.target[0] = this.cornerTarget[0];
                    this.target[1] = this.cornerTarget[1];
                }
            } else if (this.type.equals("whim")) {
                Ghost chaser = null;
                for (Ghost g : this.ghosts){
                    if (g.type.equals("chaser") && !g.dead){
                        chaser = g;
                    }
                }
                if (chaser == null) {
                    this.target[0] = this.waka.x;
                    this.target[1] = this.waka.y;
                } else {
                    int x = 0;
                    int y = 0;
                    if (this.waka.direction.equals("left")) {
                        x = chaser.x - 2 * (chaser.x-(this.waka.x-2));
                        y = chaser.y - 2 * (chaser.y-this.waka.y);
                    } else if (this.waka.direction.equals("right")) {
                        x = chaser.x - 2 * (chaser.x-(this.waka.x+2));
                        y = chaser.y - 2 * (chaser.y-this.waka.y);
                    } else if (this.waka.direction.equals("up")) {
                        x = chaser.x - 2 * (chaser.x-this.waka.x);
                        y = chaser.y - 2 * (chaser.y-(this.waka.y-2));
                    } else if (this.waka.direction.equals("down")) {
                        x = chaser.x - 2 * (chaser.x-this.waka.x);
                        y = chaser.y - 2 * (chaser.y-(this.waka.y+2));
                    }
                    if (x < 0) {
                        x = 0;
                    } else if (x > 448) {
                        x = 447;
                    }
                    if (y < 0) {
                        y = 0;
                    } else if (y > 576) {
                        y = 575;
                    }
                    this.target[0] = x;
                    this.target[1] = y;
                }
            }
        }
    }

    /**
    * Find the directions which will allow ghost to move closest to its target.
    * @param x x coordinate of the target
    * @param y y coordinate of the target
    * @return move - an arraylist including the two potential directions, prioritised in which moves ghost closer to its target.
    */
    public ArrayList<String> moveTowards(int x, int y){
        ArrayList<String> move = new ArrayList<String>();
    
        if (Math.abs(this.x - x) >= Math.abs(this.y - y)) {
            if (this.x - x >= 0) {
                move.add("left");
            } else {
                move.add("right");
            }
            if (this.y - y >= 0) {
                move.add("up");
            } else {
                move.add("down");
            }
        } else {
            if (this.y - y >= 0) {
                move.add("up");
            } else {
                move.add("down");
            }
            if (this.x - x >= 0) {
                move.add("left");
            } else {
                move.add("right");
            }
        }
        return move;
    }

    /**
    * Find the available intersections for the ghost, depending on ghost's current direction.
    * @return moves - an arraylist including the intersections available, not including its current direction.
    */
    public ArrayList<String> intersections() {
        ArrayList<String> moves = new ArrayList<String>();

        if (this.direction == null) {
            if (this.checkMove("up")) {
                moves.add("up");
            }
            if (this.checkMove("down")) {
                moves.add("down");
            }
            if (this.checkMove("left")){
                moves.add("left");
            }
            if (this.checkMove("right")) {
                moves.add("right");
            }
            return moves;  
        }
        if (this.direction.equals("left")||this.direction.equals("right")) {
            if (this.checkMove("up")) {
                moves.add("up");
            }
            if (this.checkMove("down")) {
                moves.add("down");
            }
        } else if (this.direction.equals("up")||this.direction.equals("down")) {
            if (this.checkMove("left")){
                moves.add("left");
            }
            if (this.checkMove("right")) {
                moves.add("right");
            }
        }
        return moves;
    }

    /**
    * Handles when ghost catches waka.
    * If frightMode is on, then the ghost is dead.
    * Otherwise, ghost will reset to its original position, previously dead ghosts will be revived and waka will be reset and lose a life.
    */
    public void caught(){
        if (this.frightMode) {
            this.dead = true;
        }
        else {
            for (Ghost g : this.ghosts) {
                g.dead = false;
                g.frightMode = false;
                g.x = g.startPos[0];
                g.y = g.startPos[1];
                g.frames = 0;
                g.mode = 0;
                g.scatter = true;
                g.direction = null;
            }
            this.waka.caught();
        }
    }

    /**
    * Handles when player presses a key.
    * If keyCode is 32 - player has pressed space bar - debug mode will be turned on/off.
    * @param keyCode an integer representing the key pressed
    */
    public void keyPressed(int keyCode) {
        if (keyCode == 32) {
            this.debug = !this.debug;
        }
    }

}