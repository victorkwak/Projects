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
 * A placeholder for empty spaces on the grid.
 */
public class Empty extends Objects {
    private String empty;

    /**
     * Constructor for the Empty class.
     */
    public Empty() {
        empty = " - ";
    }

    /**
     * This method returns the "empty" symbol.
     *
     * @return empty
     */
    public String toString() {
        return empty;
    }
}