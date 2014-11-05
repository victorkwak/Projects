import java.util.Scanner;

/**
 * Victor Kwak
 * CS241
 */
public class BST {
    private static Node root;

    public static void main(String[] args) {
        initialInsert();
        list();
        command();
    }

    /**
     * Asks the user for input and calls the appropriate method. Features input validation and the user can only
     * input commands in a certain format.
     */
    public static void command() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.print("Command? ");
            String input = scanner.nextLine();
            if (input.matches("[iIdDpPsS]\\s\\d+")) { // regex input validation accepts both lower and upper case
                char command = Character.toUpperCase(input.charAt(0));
                int data = Integer.parseInt(input.substring(1).trim());
                switch (command) {
                    case 'I':
                        insert(data);
                        System.out.print("In-order:\t");
                        inOrder();
                        System.out.println();
                        break;
                    case 'D':
                        delete(data);
                        System.out.print("In-order:\t");
                        inOrder();
                        System.out.println();
                        break;
                    case 'P':
                        predecessor(data);
                        break;
                    case 'S':
                        successor(data);
                        break;
                }
            } else if (input.matches("[eEhH]")) { // regex input validation accepts both lower and upper case
                char command = Character.toUpperCase(input.charAt(0));
                switch (command) {
                    case 'E':
                        exit = true;
                        System.out.println("Thank you for using!");
                        break;
                    case 'H':
                        menu();
                        break;
                }
            } else { // Invalid input
                System.out.println("Command not recognized. Use the format: \"I 50\"");
            }
        }
    }

    /**
     * Prints the the contents of the tree in pre-order, in-order, and post-order sequence.
     */
    public static void list() {
        System.out.print("Pre-order:\t");
        preOrder();
        System.out.println();
        System.out.print("In-order:\t");
        inOrder();
        System.out.println();
        System.out.print("Post-order:\t");
        postOrder();
        System.out.println();
    }

    /**
     * Prints the menu options.
     */
    public static void menu() {
        System.out.println("I   Insert a Value");
        System.out.println("D   Delete a Value");
        System.out.println("P   Find Predecessor");
        System.out.println("S   Find Successor");
        System.out.println("E   Exit the Program");
        System.out.println("H   Display This Message");
        System.out.println("");
    }

    /**
     * Asks the user for the initial set of data. Features input validation.
     */
    public static void initialInsert() {
        System.out.println("Please enter the initial sequence of values:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.matches("(\\d+)(\\s+\\d+\\s?)+")) {
            String[] data = input.split(" ");
            for (String e : data) {
                insert(Integer.parseInt(e));
            }
        } else {
            System.out.println("Invalid input. Exiting");
            System.exit(0);
        }
    }

    /**
     * Prints the contents of the tree in pre-order sequence.
     */
    public static void preOrder() {
        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }
        System.out.print(root + " ");
        if (root.getLeft() != null) {
            preOrder(root.getLeft());
        }
        if (root.getRight() != null) {
            preOrder(root.getRight());
        }
    }
    private static void preOrder(Node current) {
        System.out.print(current + " ");
        if (current.getLeft() != null) {
            preOrder(current.getLeft());
        }
        if (current.getRight() != null) {
            preOrder(current.getRight());
        }
    }

    /**
     * Prints the contents of the tree in in-order sequence.
     */
    public static void inOrder() {
        if (root == null) {
            System.out.println("Empty tree");
            return;
        }
        if (root.getLeft() != null) {
            inOrder(root.getLeft());
        }
        System.out.print(root + " ");
        if (root.getRight() != null) {
            inOrder(root.getRight());
        }
    }
    private static void inOrder(Node current) {
        if (current == null) {
            System.out.println("Empty tree");
            return;
        }
        if (current.getLeft() != null) {
            inOrder(current.getLeft());
        }
        System.out.print(current + " ");
        if (current.getRight() != null) {
            inOrder(current.getRight());
        }
    }

    /**
     * Prints the contents of the tree in post-order sequence.
     */
    private static void postOrder() {
        if (root == null) {
            System.out.println("Empty tree");
            return;
        }
        if (root.getLeft() != null) {
            postOrder(root.getLeft());
        }
        if (root.getRight() != null) {
            postOrder(root.getRight());
        }
        System.out.print(root + " ");
    }
    private static void postOrder(Node current) {
        if (current == null) {
            System.out.println("Empty tree");
            return;
        }
        if (current.getLeft() != null) {
            postOrder(current.getLeft());
        }
        if (current.getRight() != null) {
            postOrder(current.getRight());
        }
        System.out.print(current + " ");
    }

    /**
     * Searches the tree for a certain value.
     * @param data value to find
     * @return the node that contains the value searched.
     */
    public static Node search(int data) {
        if (root == null) { // Empty Tree
            return null;
        }
        if (root.getData() == data) {
            return root;
        }
        if (root.getLeft() != null && root.getData() > data) {
            return search(data, root.getLeft());
        }
        if (root.getRight() != null && root.getData() < data) {
            return search(data, root.getRight());
        }
        return null;
    }

    private static Node search(int data, Node current) {
        if (current.getData() == data) {
            return current;
        }
        if (current.getLeft() != null && current.getData() > data) {
            return search(data, current.getLeft());
        }
        if (current.getRight() != null && current.getData() < data) {
            return search(data, current.getRight());
        }
        return null;
    }

    /**
     * Deletes a node of the tree containing argument passed into the method.
     * @param data value to search for
     */
    public static void delete(int data) {
        if (root == null) { // Empty Tree
            System.out.println("Tree empty. Nothing to delete.");
            return;
        }

        if (!contains(data)) { // tree doesn't contain data to delete
            System.out.println(data + " doesn't exist!");
            return;
        }

        if (root.getData() == data) { // root contains the value to be deleted
            if (root.getRight() == null) {
                if (root.getLeft() == null) { // Both sides of root are empty
                    root = null;
                } else { // Right side of root is empty
                    root = root.getLeft();
                }
            } else if (root.getLeft() == null) { // Left side of the root is empty
                root = root.getRight(); //
            } else { // When both left and right child nodes are not null
                Node parent = root;
                Node newRoot = root.getLeft();
                while (newRoot.getRight() != null) {
                    parent = newRoot; // parent of the rightmost of the left
                    newRoot = newRoot.getRight(); // Rightmost of the left
                }
                root.setData(newRoot.getData());
                if (root.getLeft() != newRoot) { // When left child of root has a right subtree
                    parent.setRight(newRoot.getLeft());
                } else { // When left child of root doesn't have a right subtree
                    root.setLeft(newRoot.getLeft());
                }
            }
        } else { // root doesn't contain data to delete
            if (root.getLeft() != null && root.getData() > data) {
                delete(data, root.getLeft(), root);
            }
            if (root.getRight() != null && root.getData() < data) {
                delete(data, root.getRight(), root);
            }
        }
    }
    private static void delete(int data, Node current, Node parent) {
        if (current.getData() == data) {
            if (current.getRight() == null) {
                if (current.getLeft() == null) { // Both sides of current are empty
                    if (parent.getLeft() == current) {
                        parent.setLeft(null);
                    } else {
                        parent.setRight(null);
                    }
                } else { // Right side of current is empty
                    if (parent.getLeft() == current) { // current is left of parent node
                        parent.setLeft(current.getLeft());
                    } else { // current is right of parent node
                        parent.setRight(current.getLeft());
                    }
                    current.setLeft(null);
                }
            } else if (current.getLeft() == null) { // Left side of the current is empty
                if (parent.getLeft() == current) { // current is left of parent node
                    parent.setLeft(current.getRight());
                } else { // current is right of parent node
                    parent.setRight(current.getRight());
                }
                current.setRight(null);
            } else {// When both left and right child nodes are not null
                Node rParent = current;
                Node newRoot = current.getLeft();
                while (newRoot.getRight() != null) {
                    rParent = newRoot; // parent of the rightmost of the left
                    newRoot = newRoot.getRight(); // Rightmost of the left
                }
                current.setData(newRoot.getData());
                if (current.getLeft() != newRoot) { // When left child of root has a right subtree
                    rParent.setRight(newRoot.getLeft());
                } else { // When left child of root doesn't have a right subtree
                    current.setLeft(newRoot.getLeft());
                }
            }
        } else {
            if (current.getLeft() != null && current.getData() > data) {
                delete(data, current.getLeft(), current);
            }
            if (current.getRight() != null && current.getData() < data) {
                delete(data, current.getRight(), current);
            }
        }
    }

    /**
     * Inserts a new node containing the argument into the tree.
     * @param data
     */
    public static void insert(int data) {
        if (contains(data)) { // Already contains data
            System.out.println(data + " already exists, ignore.");
            return;
        }
        if (root == null) { // Tree is empty
            root = new Node(data, null, null);
        } else {
            if (data <= root.getData()) {
                if (root.getLeft() == null) {
                    root.setLeft(new Node(data, null, null));
                } else {
                    insert(data, root.getLeft());
                }
            } else {
                if (root.getRight() == null) {
                    root.setRight(new Node(data, null, null));
                } else {
                    insert(data, root.getRight());
                }
            }
        }
    }
    private static void insert(int data, Node current) {
        if (data <= current.getData()) {
            if (current.getLeft() == null) {
                current.setLeft(new Node(data, null, null));
            } else {
                insert(data, current.getLeft());
            }
        } else {
            if (current.getRight() == null) {
                current.setRight(new Node(data, null, null));
            } else {
                insert(data, current.getRight());
            }
        }
    }

    /**
     * Finds and prints the in-order predecessor of the argument passed.
     * @param data value to find the predecessor for
     */
    public static void predecessor(int data) {
        if (root == null) { // Tree is empty
            System.out.println("Tree empty");
            return;
        }
        if (!contains(data)) { // tree doesn't contain the value searched
            System.out.println(data + " doesn't exist!");
            return;
        }
        if (root.getData() == data) { // root's predecessor is always going to be the rightmost of the left child.
            if (root.getLeft() != null) {
                System.out.println(root.getLeft().getRightmostData());
            } else {
                System.out.println("No predecessor");
            }
            return;
        }
        if (data == root.getLeftmostData()) { // The leftmost of the root isn't going to have a predecessor
            System.out.println("No predecessor");
            return;
        }
        if (data < root.getData()) { // go to the left child
            predecessor(data, root.getLeft(), root);
        } else { // go to the right child
            predecessor(data, root.getRight(), root);
        }
    }
    private static void predecessor(int data, Node current, Node predecessor) {
        if (current.getData() == data) {
            if (current.getLeft() != null) { // has left subtree
                System.out.println(current.getLeft().getRightMost());

            } else { // doesn't have left subtree
                System.out.println(predecessor);
            }
            return;
        }
        if (data < current.getData()) { // go left
            predecessor(data, current.getLeft(), predecessor); // keeps the predecessor node the same
        } else { // go right
            predecessor(data, current.getRight(), current); // changes the predecessor node
        }
    }

    /**
     * Finds and prints the in-order successor of the argument passed.
     * @param data value to find the successor for
     */
    public static void successor(int data) {
        if (root == null) { // Tree is empty
            System.out.println("Tree empty");
            return;
        }
        if (!contains(data)) { // tree doesn't contain the value searched
            System.out.println(data + " doesn't exist!");
            return;
        }
        if (root.getData() == data) { // root's predecessor is always going to be the rightmost of the left child.
            if (root.getRight() != null) {
                System.out.println(root.getRight().getLeftmostData());
            } else {
                System.out.println("No successor");
            }
            return;
        }
        if (data == root.getRightmostData()) { // root's rightmost isn't going to have a successor
            System.out.println("No successor");
            return;
        }
        if (data < root.getData()) { // go left
            successor(data, root.getLeft(), root);
        } else { // go right
            successor(data, root.getRight(), root);
        }
    }
    private static void successor(int data, Node current, Node successor) {
        if (current.getData() == data) {
            if (current.getRight() != null) { // has right subtree
                System.out.println(current.getRight().getLeftmostData());
            } else { // doesn't have right subtree
                System.out.println(successor);
            }
            return;
        }
        if (data < current.getData()) { // go left
            successor(data, current.getLeft(), current); // changes the successor node
        } else { // go right
            successor(data, current.getRight(), successor); // keeps the successor node the same
        }
    }

    /**
     * @param data value to search for
     * @return whether the tree contains the value searched for or not
     */
    public static boolean contains(int data) {
        return search(data) != null;
    }
}