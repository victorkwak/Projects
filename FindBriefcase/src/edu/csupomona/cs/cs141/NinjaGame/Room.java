package edu.csupomona.cs.cs141.NinjaGame;

/**
 * CS 141: Introduction to Programming and Problem Solving
 * Professor: Edwin Rodriguez
 * <p/>
 * Programming Assignment #N
 * <p/>
 * <description-of-assignment>
 * <p/>
 * Team #N / Victor Kwak
 * <team-member-names-if-team-assignment>
 */

/**
 * The rooms are where the briefcase, the objective of the game, is hidden.
 */
public class Room extends Objects {
    private String room;

    /**
     * Constructor for the Room class.
     */
    public Room() {
        room = "|_|";
        setX(x);
        setY(y);
        getX();
        getY();

    }

    /**
     * Returns the symbolic representation of "room"
     *
     * @return
     */
    public String toString() {
        return room;
    }
}
