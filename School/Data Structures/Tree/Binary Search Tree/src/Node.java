/**
 * Victor Kwak
 * CS241
 * September 30, 2014
 */
public class Node {
    private int data;
    private Node left;
    private Node right;

    public Node(int initialData, Node initialLeft, Node initialRight) {
        data = initialData;
        left = initialLeft;
        right = initialRight;
    }

    public int getData() {
        return data;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setData(int newData) {
        data = newData;
    }

    public void setLeft(Node newLeft) {
        left = newLeft;
    }

    public void setRight(Node newRight) {
        right = newRight;
    }


    public int getLeftmostData() {
        Node current = this;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current.getData();
    } 

    public Node getRightMost() {
        Node current = this;
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current;
    }

    public int getRightmostData() {
        Node current = this;
        while (current.getRight() != null) {
            current = current.getRight();
        }
        return current.data;
    }

    public String toString() {
        return String.valueOf(this.getData());
    }
}