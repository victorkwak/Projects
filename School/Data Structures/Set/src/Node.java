/**
 * Victor Kwak
 * June 24, 2014
 */


/**
 * Node of a singly linked list of strings.
 */
public class Node <T>{
    private T element;
    private Node<T> next;

    /**
     * Creates a node with the given element and next node.
     */
    public Node(T s, Node<T> n) {
        element = s;
        next = n;
    }

    /**
     * Returns the element of this node.
     */
    public T getElement() {
        return element;
    }

    /**
     * Returns the next node of this node.
     */
    public Node<T> getNext() {
        return next;
    }
    // Modifier methods:

    /**
     * Sets the element of this node.
     */
    public void setElement(T newElem) {
        element = newElem;
    }

    /**
     * Sets the next node of this node.
     */
    public void setNext(Node<T> newNext) {
        next = newNext;
    }
}
