package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;


/**
* Map object used by the game.
*/
public class Map{
    
    /**
    * Matrix of the game map file containing 36 rows and 28 columns
    */
    public String[][] matrix;

    /**
    * Scaled matrix of the gamp map with boolean values 
    */
    public boolean[][] map;

    /**
    * Whether superfruit has been eaten or not.
    */
    public boolean frightMode;

    /**
    * Collection containing graphics of the game.
    */
    public HashMap<String, PImage> collection;

    /**
    * Whether the game has ended.
    */
    public boolean end;

    /**
    * Constructor of Map. 
    * Requires map filename and a collection of PImage graphics. 
    * Produces a 36x28 matrix of the file as well as a scaled map of the matrix. 
    * By default frightMode is false and end is false.
    * If file is not found, a FileNotFoundException will be caught and stacktrace printed.
    * @param filename File name of game map
    * @param collection Collection of PImage graphics
    */
    public Map(String filename, HashMap<String, PImage> collection){
        this.matrix = new String[36][28];
        try{
            File f = new File(filename);
            Scanner scan = new Scanner(f);
            int x = 0;
            while(x<36){
                String s = scan.nextLine();
                this.matrix[x] = s.split("");
                x++;
            }
            this.collection = collection;
            this.map = this.getMap();
            this.frightMode = false;
            this.end = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
    * Displays the maps graphics. 
    * @param app instance of the application
     */
    public void draw(PApplet app){

        for(int i = 0; i<this.matrix.length; i++){
            for(int j = 0; j<this.matrix[i].length; j++){
                if(this.collection.containsKey(this.matrix[i][j])){
                    app.image(this.collection.get(this.matrix[i][j]), j*16, i*16);
                }
            }
        }
    }

    /**
    * Updates the matrix with waka's movements. 
    * @param waka the player's waka
    */
    public void tick(Waka waka){
        if (this.frightMode) {
            this.frightMode = false;
        }
        this.updateMatrix(waka.x, waka.y);
    }
    

    /**
    * Retrieves the waka's starting position. 
    * @return arr which is an integer array which holds the coordinates of waka's starting position from the game map.
     */
    public int[] getWakaPosition(){
        int[] arr = new int[2];
        int i = 0;
        while(i<36){
            int j = 0;
            while(j < 28){
                if(this.matrix[i][j].equals("p")){
                    arr[1] = i;
                    arr[0] = j;
                }
                j++;
            }
            i++;
        }
        return arr;
    }

    /**
    * Retrieves the ghosts starting position. 
    * @return list which is a String array arraylist which holds the coordinates of each ghosts starting position from the game map.
    */
    public ArrayList<String[]> getGhostPosition(){
        ArrayList<String[]> list = new ArrayList<String[]>();
        int i = 0;
        while (i<36){
            int j = 0;
            while (j < 28){
                if (this.matrix[i][j].equals("a")||this.matrix[i][j].equals("c")||this.matrix[i][j].equals("i")||this.matrix[i][j].equals("w")){
                    String[] arr = new String[3];
                    arr[2] = Integer.toString(i);
                    arr[1] = Integer.toString(j);
                    if (this.matrix[i][j].equals("a")){
                        arr[0] = "ambusher";
                    } else if (this.matrix[i][j].equals("c")){
                        arr[0] = "chaser";
                    } else if (this.matrix[i][j].equals("i")){
                        arr[0] = "ignorant";
                    } else if (this.matrix[i][j].equals("w")){
                        arr[0] = "whim";
                    };
                    list.add(arr);
                }
                j++;
            }
            i++;
        }
        return list;
    }

    /**
    * Checks if there are still fruits in the map.
    * If there are no fruits left, then the game is over and player has won.
    */
    public void haveFruit(){
        int fruit = 0;
        int i = 0;
        while(i<36){
            int j = 0;
            while(j < 28){
                if(this.matrix[i][j].equals("7")||this.matrix[i][j].equals("8")){
                    fruit++;
                }
                j++;
            }
            i++;
        }
        if (fruit == 0) {
            this.end = true;
        }
    }

    /**
    * Updates the position of waka on the matrix.
    * If waka is on a fruit, the fruit will disapear as waka has eaten it.
    * If waka is on a superfruit, the fruit will disappear and frightMode will be true.
    * If there are no fruits left, then the game is over and player has won.
    * @param i waka x coordinate
    * @param j waka y coordinate
    */
    public void updateMatrix(int i, int j){

        int x = (int) Math.round(j/16);
        int y = (int) Math.round(i/16);

        if(this.matrix[x][y].equals("7")){
            this.matrix[x][y] = "0";
        } else if(this.matrix[x][y].equals("8")){
            this.matrix[x][y] = "0";
            this.frightMode = true;
        }
        this.haveFruit();
    }

    /**
    * Checks if a character can move onto a certain coordinate.
    * @param x waka x coordinate
    * @param y waka y coordinate
    * @return true or false depending on the boolean in the map
    */
    public boolean Move(int x, int y){
        return this.map[y][x];
    }

    /**
    * Scales the map and substitute objects for booleans.
    * If a character can move onto the object, then it is substituted with true.
    * If the object is a wall, then it is substituted with false.
    * @return map which is a boolean map, a scaled up version of the original Strings game matrix. 
    */
    public boolean[][] getMap(){
        boolean[][] map = new boolean[576][448];
        boolean[][] tempMap = new boolean[36][448];
        int i = 0;
        while(i<36){
            boolean[] arr = new boolean[448];
            int j = 0;
            int x = 0;
            while(j < 28){
                if(this.matrix[i][j].equals("0")||this.matrix[i][j].equals("7")||this.matrix[i][j].equals("8")||this.matrix[i][j].equals("a")||this.matrix[i][j].equals("c")||this.matrix[i][j].equals("i")||this.matrix[i][j].equals("w")||this.matrix[i][j].equals("p")){
                    while (x < (j+1)*16){
                        arr[x] = true;
                        x++;
                    }
                } else {
                    while (x < (j+1)*16){
                        arr[x] = false;
                        x++;
                    }
                }
                j++;
            }
            tempMap[i] = arr;
            i++;
        }
        int q = 0;
        int p = 0;
        while (q<36){
            while (p < (q+1)*16){
                map[p] = tempMap[q];
                p++;
            }
            q++;
        }
        return map;
    }

}