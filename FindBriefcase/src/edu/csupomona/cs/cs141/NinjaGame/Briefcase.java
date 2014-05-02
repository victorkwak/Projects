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
package edu.csupomona.cs.cs141.NinjaGame;


/**
 * Obtaining the briefcase will render the player victorious.
 */
public class Briefcase extends Objects {

    private String briefcase;

    /**
     * Constructor for the Briefcase class.
     */
    public Briefcase() {
        briefcase = "xXx";
        hidden = true;
        setX(x);
        setY(y);
        getX();
        getY();
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
     * @return Either the "room" if hidden, or "briefcase" if not.
     */
    public String toString() {
        if (hidden) {
            return "|_|";
        }
        return briefcase;
    }
}