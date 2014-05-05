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
 * The radar will let the player know which room the briefcase is in.
 */
public class Radar extends Objects {
    private String radar;

    /**
     * Contructor for the Radar Class.
     */
    public Radar() {
        radar = " R ";
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
     * @return Either the "empty" if hidden, or "radar" if not.
     */
    public String toString() {
        if (hidden) {
            return " - ";
        }
        return radar;
    }
}