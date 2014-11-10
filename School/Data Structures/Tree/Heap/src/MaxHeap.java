import java.util.Random;
import java.util.Scanner;

/**
 * Victor Kwak
 * MaxHeap
 * Implement a max-heap. Heaps have the advantage of being complete binary trees and so the tree can be
 * implemented in an array.
 */
public class MaxHeap {
    private static int heap[];
    private static int lastIndex;

    public static void main(String[] args) {
        boolean exit = true;
        while (exit) {
            exit = menu();
        }
    }

    /**
     * Prints the menu for the program and takes user input.
     * @return whether to exit program or not.
     */
    public static boolean menu() {
        System.out.println("=====================================================================================\n\n");
        System.out.println("Please select how to test the program:");
        System.out.println("(1) 20 sets of 100 randomly generated integers");
        System.out.println("(2) Fixed integer values 1-100");
        System.out.print("Enter choice: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if (choice.matches("\\s*[12]\\s*")) { // Only accepts 1 or 2 as inputs. The program will exit otherwise.
            int num = Integer.parseInt(choice.trim());
            switch (num) {
                case 1:
                    System.out.println("\n");
                    random();
                    break;
                case 2:
                    System.out.println("\n");
                    fixed();
                    break;
            }
        } else {
            System.out.println("Invalid choice. Exiting...");
            return false;
        }
        System.out.println("\n");
        return true;
    }

    /**
     * Builds two heaps from random values using the 1 - 100 range. Once using the series of insertion method (O(nlogn))
     * and the other, the optimal method (O(n)). Keeps track of the number of swaps needed in order to build the heap
     * and presents it to the user.
     */
    private static void random() {
        Random random = new Random();
        int swaps = 0;

        // Series of insertions
        for (int i = 0; i < 20; i++) {
            heap = new int[100];
            for (int j = 0; j < 100; ) {
                if (insert(1 + random.nextInt(100))) { //Generate random integer from 1 to 100.
                    swaps += reheapUp();
                    ++j;
                }
            }
        }
        System.out.println("Average swaps for series of insertions: " + swaps / 20);

        // Optimal method
        swaps = 0;
        for (int i = 0; i < 20; i++) {
            heap = new int[100];
            for (int j = 0; j < 100; ) {
                if (insert(1 + random.nextInt(100))) { //Generate random integer from 1 to 100.
                    ++j;
                }
            }
            swaps += reheapOptimal();
        }
        System.out.println("Average swaps for optimal method: " + swaps / 20);
    }

    /**
     * Builds two heaps from fixed values from 1 - 100. Once using the series of insertion method (O(nlogn))
     * and the other, the optimal method (O(n)). Keeps track of the number of swaps needed in order to build the
     * heap and presents it to the user.
     */
    private static void fixed() {
        int swaps = 0;

        // Series of insertions
        heap = new int[100];
        for (int i = 0; i < 100; i++) {
            if (insert(i + 1)) {
                swaps += reheapUp();
            }
        }
        System.out.print("Heap built using series of insertions: ");
        for (int i = 0; i < 10; i++) {
            System.out.print(heap[i] + ",");
        }
        System.out.println("...\nNumber of swaps: " + swaps);
        for (int i = 0; i < 10; i++) {
            delete();
        }
        System.out.print("Heap after 10 removals: ");
        for (int i = 0; i < 10; i++) {
            System.out.print(heap[i] + ",");
        }
        System.out.print("...");
        System.out.println("\n");

        // Optimal Method
        heap = new int[100];
        for (int i = 0; i < 100; i++) {
            insert(i + 1);
        }
        swaps = reheapOptimal();
        System.out.print("Heap built using optimal method: ");
        for (int i = 0; i < 10; i++) {
            System.out.print(heap[i] + ",");
        }
        System.out.println("...\nNumber of swaps: " + swaps);
        for (int i = 0; i < 10; i++) {
            delete();
        }
        System.out.print("Heap after 10 removals: ");
        for (int i = 0; i < 10; i++) {
            System.out.print(heap[i] + ",");
        }
        System.out.print("...");
    }

    /**
     * Deletes a value from the heap.
     */
    private static void delete() {
        heap[0] = heap[lastIndex];
        heap[lastIndex] = 0;
        reheapDown(0);
        --lastIndex;
    }

    /**
     * Used when inserting elements into the heap, the method will reheapify the from the bottom-up.
     * @return number of swaps
     */
    private static int reheapUp() {
        int swaps = 0;
        int parent = (lastIndex - 1) / 2;
        int current = lastIndex;
        while (heap[current] > heap[parent]) {
            int temp = heap[parent];
            heap[parent] = heap[current];
            heap[current] = temp;
            current = parent;
            parent = (current - 1) / 2;
            ++swaps;
        }
        return swaps;
    }

    /**
     * Used with the optimal method of building a heap, it will find the last parent node and reheapify downwards
     * and continue with every previous node before it.
     * @return number of swaps made for this reheapification.
     */
    private static int reheapOptimal() {
        int swaps = 0;
        int i = (heap.length - 1) / 2; // index of the last parent node
        for (; i >= 0; i--) {
            swaps += reheapDown(i);
        }
        return swaps;
    }

    /**
     * Used with either the optimal method or removal of elements from the heap, moves down the and performs swaps
     * based on comparisons between the parent node and its child nodes.
     * @param i index value of the node to reheapify
     * @return number of swaps made for the reheapification from index i and down.
     */
    private static int reheapDown(int i) {
        int swaps = 0;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int temp;
        int larger = i;
        if (left < heap.length && heap[left] > heap[larger]) {
            larger = left;
        }
        if (right < heap.length && heap[right] > heap[larger]) {
            larger = right;
        }
        if (larger != i) {
            temp = heap[i];
            heap[i] = heap[larger];
            heap[larger] = temp;
            swaps += 1 + reheapDown(larger);
        }
        return swaps;
    }

    /**
     * Inserts an element into the heap. It will check to make sure that the element being inserted doesn't already
     * exit in the heap (set).
     * @param element the integer value to be inserted into the heap.
     * @return whether the insertion was successful (element was not present in the heap) or unsuccessful (element
     * was already present in the heap).
     */
    private static boolean insert(int element) {
        int i;
        for (i = 0; i < heap.length; i++) {
            if (heap[i] == element) {
                return false;
            }
            if (heap[i] == 0) {
                break;
            }
        }
        heap[i] = element;
        lastIndex = i;
        return true;
    }
}