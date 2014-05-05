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
 * This class defines the player's attributes.
 */
public class Player extends Objects {

    private boolean hasBullet;
    private boolean hasShield;
    String playerChar;

    /**
     * Constructor for the Player class.
     */
    public Player() {
        playerChar = " P ";
        hasBullet = true;
        hasShield = false;
        setX(x);
        setY(y);
        getX();
        getY();
    }

    /**
     * Returns if the player has the shield or not.
     *
     * @return
     */
    public boolean getHasShield() {
        return hasShield;
    }

    /**
     * Sets whether or not the player has the shield.
     *
     * @param shieldShit
     */
    public void setHasShield(boolean shieldShit) {
        hasShield = shieldShit;
    }

    /**
     * Sets whether or not the player has a bullet.
     *
     * @param bulletButts
     */
    public void setHasBullet(boolean bulletButts) {
        this.hasBullet = bulletButts;
    }

    /**
     * Returns if the player has a bullet or not.
     *
     * @return
     */
    public boolean getHasBullet() {
        return hasBullet;
    }

    /**
     * Generates messages based on how many lives the player has left.
     *
     * @param lives Current number of lives.
     */
    public void die(int lives) {
        if (lives == 0) {
            System.out.println("You suck. Game over.");
        } else if (lives > 1) {
            System.out.println("Hey, you died! You have " + lives + " lives left.");
            System.out.println("Maybe don't suck so much next time.");
        } else {
            System.out.println("Hey, you died! You have " + lives + " life left.");
        }
    }

    /**
     * Returns the symbolic representation of player.
     *
     * @return
     */
    public String toString() {
        return playerChar;
    }
}