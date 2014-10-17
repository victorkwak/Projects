import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Victor Kwak
 * CS240
 * Implement a Set ADT using a singly linked list
 * July 01, 2014
 */
public class Project1 {
    private static String output = ""; // holds all information to be saved to output file.

    /**
     * Prints out in console as well saves to output variable for later use.
     * @param toPrint string to print and save.
     */
    public static void printOut(String toPrint) {
        output += toPrint + '\n';
        System.out.println(toPrint);
    }

    /**
     * Saves a string to a file "output.txt." The method creates a new file and overwrites a previous version
     * of "output.txt" if it exists.
     * @param toSave string to save to file
     */
    public static void saveToFile(String toSave) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));
            bufferedWriter.write(toSave);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Integer[] aI; // Array of elements to be inserted into set A
        Integer[] bI; // Array of elements to be inserted into set B
        Set<Integer> a;
        Set<Integer> b;

        // Case 1 - A and B are equal but distinct sets
        printOut("--------------------------------------------");
        printOut("Case 1 - A and B are equal but distinct sets");
        printOut("--------------------------------------------");
        aI = new Integer[] {1, 2, 3};
        bI = new Integer[] {2, 1, 3};
        a = new Set<Integer>(aI);
        b = new Set<Integer>(bI);

        printOut("Set A is: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("Set B is: " + b);
        printOut("The size of set B is: " + b.size() + '\n');

        printOut("A contains 1: " + a.contain(1));
        printOut("A contains 4: " + a.contain(1) + '\n');

        printOut("A is a subset of B: " + a.subsetOf(b));
        printOut("B is a subset of A: " + b.subsetOf(a) + '\n');

        printOut("A is equal to B: " + a.isEqual(b));
        printOut("B is equal to A: " + b.isEqual(a) + '\n');

        printOut("A union B is: " + a.union(b));
        printOut("A intersection B is: " + a.intersection(b));
        printOut("A complement B is: " + a.complement(b) + '\n');

        printOut("1 can be removed from set A: " + a.remove(1));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("5 can be removed from set A: " + a.remove(5));
        printOut("Set A is now: " + a);
        printOut("The size of set a is: " + a.size() + '\n');

        printOut("1 can be added to set A: " + a.addElement(1));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("2 can be added to set A: " + a.addElement(2));
        printOut("set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        // Case 2 - A and B are such that they have different sizes but one is a subset of the other
        printOut("-----------------------------------------------------------------------------------------");
        printOut("Case 2 - A and B are such that they have different sizes but one is a subset of the other");
        printOut("-----------------------------------------------------------------------------------------");
        aI = new Integer[]{1};
        bI = new Integer[]{1, 2};
        a = new Set<Integer>(aI);
        b = new Set<Integer>(bI);

        printOut("Set A is: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("Set B is: " + b);
        printOut("The size of set B is: " + b.size() + '\n');

        printOut("A contains 1: " + a.contain(1));
        printOut("A contains 4: " + a.contain(4) + '\n');

        printOut("A is a subset of B: " + a.subsetOf(b));
        printOut("B is a subset of A: " + b.subsetOf(a) + '\n');

        printOut("A is equal to B: " + a.isEqual(b));
        printOut("B is equal to A: " + b.isEqual(a) + '\n');

        printOut("A union B is: " + a.union(b));
        printOut("A intersection B is: " + a.intersection(b));
        printOut("A complement B is: " + a.complement(b) + '\n');

        printOut("1 can be removed from set A: " + a.remove(1));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("5 can be removed from set A: " + a.remove(5));
        printOut("Set A is now: " + a);
        printOut("The size of set a is: " + a.size() + '\n');

        printOut("2 can be added to set A: " + a.addElement(2));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("8 can be added to set A: " + a.addElement(8));
        printOut("set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        // Case 3 - A and B are non-empty and different in size but have common elements
        printOut("-----------------------------------------------------------------------------");
        printOut("Case 3 - A and B are non-empty and different in size but have common elements");
        printOut("-----------------------------------------------------------------------------");
        aI = new Integer[]{1, 2, 3};
        bI = new Integer[]{2, 3, 4, 5};
        a = new Set<Integer>(aI);
        b = new Set<Integer>(bI);

        printOut("Set A is: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("Set B is: " + b);
        printOut("The size of set B is: " + b.size() + '\n');

        printOut("A contains 1: " + a.contain(1));
        printOut("A contains 4: " + a.contain(4) + '\n');

        printOut("A is a subset of B: " + a.subsetOf(b));
        printOut("B is a subset of A: " + b.subsetOf(a) + '\n');

        printOut("A is equal to B: " + a.isEqual(b));
        printOut("B is equal to A: " + b.isEqual(a) + '\n');

        printOut("A union B is: " + a.union(b));
        printOut("A intersection B is: " + a.intersection(b));
        printOut("A complement B is: " + a.complement(b) + '\n');

        printOut("1 can be removed from set A: " + a.remove(1));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("5 can be removed from set A: " + a.remove(5));
        printOut("Set A is now: " + a);
        printOut("The size of set a is: " + a.size() + '\n');

        printOut("2 can be added to set A: " + a.addElement(2));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("8 can be added to set A: " + a.addElement(8));
        printOut("set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        // Case 4 A and B are non-empty with nothing in common
        printOut("-----------------------------------------------------");
        printOut("Case 4 - A and B are non-empty with nothing in common");
        printOut("-----------------------------------------------------");
        aI = new Integer[]{1};
        bI = new Integer[]{2, 3};
        a = new Set<Integer>(aI);
        b = new Set<Integer>(bI);

        printOut("Set A is: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("Set B is: " + b);
        printOut("The size of set B is: " + b.size() + '\n');

        printOut("A contains 1: " + a.contain(1));
        printOut("A contains 4: " + a.contain(4) + '\n');

        printOut("A is a subset of B: " + a.subsetOf(b));
        printOut("B is a subset of A: " + b.subsetOf(a) + '\n');

        printOut("A is equal to B: " + a.isEqual(b));
        printOut("B is equal to A: " + b.isEqual(a) + '\n');

        printOut("A union B is: " + a.union(b));
        printOut("A intersection B is: " + a.intersection(b));
        printOut("A complement B is: " + a.complement(b) + '\n');

        printOut("1 can be removed from set A: " + a.remove(1));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("5 can be removed from set A: " + a.remove(5));
        printOut("Set A is now: " + a);
        printOut("The size of set a is: " + a.size() + '\n');

        printOut("2 can be added to set A: " + a.addElement(2));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("8 can be added to set A: " + a.addElement(8));
        printOut("set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        // Case 5 - One is non-empty and the other empty
        printOut("---------------------------------------------");
        printOut("Case 5 - One is non-empty and the other empty");
        printOut("---------------------------------------------");
        bI = new Integer[]{2, 3, 4, 5};
        a = new Set<Integer>();
        b = new Set<Integer>(bI);

        printOut("Set A is: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("Set B is: " + b);
        printOut("The size of set B is: " + b.size() + '\n');

        printOut("A contains 1: " + a.contain(1));
        printOut("A contains 4: " + a.contain(4) + '\n');

        printOut("A is a subset of B: " + a.subsetOf(b));
        printOut("B is a subset of A: " + b.subsetOf(a) + '\n');

        printOut("A is equal to B: " + a.isEqual(b));
        printOut("B is equal to A: " + b.isEqual(a) + '\n');

        printOut("A union B is: " + a.union(b));
        printOut("A intersection B is: " + a.intersection(b));
        printOut("A complement B is: " + a.complement(b) + '\n');

        printOut("1 can be removed from set A: " + a.remove(1));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("5 can be removed from set A: " + a.remove(5));
        printOut("Set A is now: " + a);
        printOut("The size of set a is: " + a.size() + '\n');

        printOut("2 can be added to set A: " + a.addElement(2));
        printOut("Set A is now: " + a);
        printOut("The size of set A is: " + a.size() + '\n');

        printOut("8 can be added to set A: " + a.addElement(8));
        printOut("set A is now: " + a);
        printOut("The size of set A is: " + a.size());
        saveToFile(output.trim());
    }
}
