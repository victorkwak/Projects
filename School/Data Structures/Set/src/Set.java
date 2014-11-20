/**
 * Victor Kwak
 * CS240
 * Implement a Set ADT using a singly linked list
 * July 01, 2014
 */
public class Set <T>{

    private Node<T> head; //dummy head
    private int size;

    /**
     * Creates an empty set
     */
    public Set() {
        head = new Node<T>(null, null);
        size = 0;
    }

    /**
     * Takes an array of objects and creates a new set from those objects
     * @param initialSet initial set of objects
     */
    public Set(T[] initialSet) {
        head = new Node<T>(null, null);
        size = 0;
        for (int i = initialSet.length - 1; i >= 0; i--) {
            addElement(initialSet[i]);
        }
    }

    /**
     * @return true if the given object is contained in the set and false otherwise.
     */
    public boolean contain(T element) {
        if (head.getNext() != null) {
            Node<T> current = head.getNext();
            while (current != null) {
                if (current.getElement().equals(element)) {
                    return true;
                }
                current = current.getNext();
            }
        }
        return false;
    }

    /**
     * @return true if the node containing the object is removed from the set and false otherwise.
     */
    public boolean remove(T element) {
        if (head.getNext() != null) {
            Node<T> current = head.getNext();
            Node<T> previous = head;
            while (current != null) {
                if (current.getElement().equals(element)) {
                    previous.setNext(current.getNext());
                    current.setNext(null);
                    size--;
                    return true;
                }
                previous = previous.getNext();
                current = current.getNext();
            }
        }
        return false;
    }

    /**
     * @return false if element not added because it is already in the set and true if the element is added.
     */
    public boolean addElement(T element) {
        if (!contain(element)) {
            head.setNext(new Node<T>(element, head.getNext()));
            size++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return an integer equal to the number of distinct objects are in the Set.
     */
    public int size() {
        return size;
    }

    /**
     * @return true if every element in set A is in set B and false otherwise.
     */
    public boolean subsetOf(Set<T> set) {
        if (size == 0) { //
            return true;
        }

        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            current = current.getNext();
            if (!set.contain(current.getElement())) { // Not a subset if even one element is not present
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if both sets contain the same elements where order in either set does not count.
     */
    public boolean isEqual(Set<T> set) {
        if (size == set.size) {
            Node<T> currentNode = set.head.getNext();
            for (int i = 0; i < set.size(); i++) {
                if (!contain(currentNode.getElement())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * if A, B, C are sets, has the form C = A.union(B).
     *
     * @return a Set that contains all the elements in set A and B, but only list duplicates once.
     */
    public Set union(Set<T> set) {
        Set<T> union = new Set<T>();

        // Copy first set
        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            current = current.getNext();
            union.addElement(current.getElement());
        }

        // Add elements not present in the first set from the second
        current = set.head;
        for (int i = 0; i < set.size; i++) {
            current = current.getNext();
            if (!contain(current.getElement())) {
                union.addElement(current.getElement());
            }
        }
        return union;
    }

    /**
     * if A, B, C are sets, has form C = A.intersection(B)
     *
     * @return a set containing only elements that are common to both A and B, but no duplicates.
     */
    public Set intersection(Set<T> set) {
        Set<T> intersection = new Set<T>();

        Node<T> current = set.head;
        for (int i = 0; i < set.size; i++) {
            current = current.getNext();
            if (contain(current.getElement())) {
                intersection.addElement(current.getElement());
            }
        }
        return intersection;
    }

    /**
     * if A, B, C are sets, has form C = A.complement(B)
     *
     * @return a set containing only elements that are in A but not in B.
     */
    public Set complement(Set<T> set) {
        Set<T> complement = new Set<T>();

        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            current = current.getNext();
            if (!set.contain(current.getElement())) {
                complement.addElement(current.getElement());
            }
        }
        return complement;
    }

    /**
     * @return displays a message that indicates an object's state.
     * An object's state is the data that is stored in the object's fields at any giving moment.
     */
    public String toString() {
        if (size == 0) {
            return "{ }";
        } else {
            String returnString = "{";
            Node<T> current = head;
            for (int i = 0; i < size; i++) {
                current = current.getNext();
                if (i < size - 1) {
                    returnString += current.getElement().toString() + ", ";
                } else {
                    returnString += current.getElement().toString() + "}";
                }
            }
            return returnString;
        }
    }
}
