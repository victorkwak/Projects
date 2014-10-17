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

/**
 * The NPC class. This is the Ninja.
 */
public class Ninja extends Objects {

    private String ninja;
    private boolean isAlive;

    /**
     * Constructor for the Ninja class.
     */
    public Ninja() {
        ninja = " N ";
        isAlive = true;
        hidden = true;
        setX(x);
        setY(y);
        getX();
        getY();
    }

    /**
     * Checks if the ninja is alive or not.
     *
     * @return true (alive) or false (dead)
     */
    public boolean checkIsAlive() {
        if (isAlive) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets a ninja to be dead.
     */
    final public void die() {
        isAlive = false;
    }

    /**
     * This method is used for the debug mode, and turns the item's visibility on or off.
     *
     * @param b boolean turns visibility on or off.
     */
    public void setPrintBoolean(boolean b) {
        this.hidden = b;
    }

    /**
     * This method returns the symbolic representation of the objects.
     *
     * @return Either the "empty" if hidden, or "ninja" if not.
     */
    public String toString() {
        if (hidden) {
            return " - ";
        }
        return ninja;
    }
}