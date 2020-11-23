package ghost;
import processing.core.PApplet;

/**
* Functions applicable to both Waka and Ghosts.
*/
public interface Functions{

    /**
    * Method used to move character objects.
    */
    public void move();

    /**
    * Method used when character object has been caught/has collided.
    */
    public void caught();

    /**
    * Drawing method used when rendering graphics to the application.
    * @param app PApplet App object
    */
    public void draw(PApplet app);

    /**
    * Handling player key presses.
    * @param keyCode keyCode of player key press
    */
    public void keyPressed(int keyCode);

}