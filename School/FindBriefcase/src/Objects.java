/**
 * CS 141: Introduction to Programming and Problem Solving
 * Professor: Edwin Rodr&iacute;guez
 *
 * Programming Assignment Ninja Game
 *
 * Develop a text based game for finding a briefcase in a dark
 * house while dodging Ninjas
 *
 * Team Half & Half
 *   Karen Cheung
 *   Victor Kwak
 *   Serenity Waits
 *   Tristan Warner
 */


import java.io.Serializable;

/**
 * The superclass to all objects used in the game's grid. Implements serializable so that
 * all the objects in the grid can be saved.
 */
abstract public class Objects implements Serializable {

    int x;
    int y;
    boolean hidden;

    /**
     * Sets the X coordinate values for objects.
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the Y coordinate values for objects.
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * returns the X coordinate values for objects
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * returns the Y coordinate values for objects
     *
     * @return
     */
    public int getY() {
        return y;
    }
}