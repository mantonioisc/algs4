import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    /**
     * number of elements on queue
     */
    private int size;
    /**
     * beginning of queue
     */
    private Node first;
    /**
     * end of queue
     */
    private Node last;

    /**
     * helper linked list class
     */
    private class Node {
        private Item item;
        private Node front;
        private Node back;
    }

    /**
     * construct an empty deque
     */
    public Deque() {

    }

    /**
     * is the deque empty?
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return first == null || last == null;
    }

    /**
     * return the number of items on the deque
     *
     * @return number of items
     */
    public int size() {
        return size;
    }

    /**
     * add the item to the front
     *
     * @param item to be added
     */
    public void addFirst(Item item) {
        validate(item);

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.front = null; //make clear that no node is in front
        size++;

        if (isEmpty()) {
            last = first;
        } else {
            oldFirst.front = first;
            first.back = oldFirst;
        }
    }

    /**
     * add the item to the end
     *
     * @param item to be added
     */
    public void addLast(Item item) {
        validate(item);

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.back = null; //make clear that no node is in the back
        size++;

        if (isEmpty()) {
            first = last;
        } else {
            oldLast.back = last;
            last.front = oldLast;
        }
    }

    /**
     * remove and return the item from the front
     *
     * @return an item
     */
    public Item removeFirst() {
        checkEmpty();

        Item item = first.item;

        Node newFirst = first.back;
        if (newFirst != null) { //make sure there is more than one element
            newFirst.front = null;
        }
        first.back = null;
        first = newFirst;

        size--;

        if (isEmpty()) {
            last = null; //to avoid loitering
        }

        return item;
    }

    /**
     * remove and return the item from the end
     *
     * @return an item
     */
    public Item removeLast() {
        checkEmpty();

        Item item = last.item;

        Node newLast = last.front;
        if (newLast != null) { //make sure there is more than one element
            newLast.back = null;
        }
        last.front = null;
        last = newLast;

        size--;

        if (isEmpty()) {
            first = null;
        }


        return item;
    }

    /**
     * return an iterator over items in order from front to end
     *
     * @return an new iterator each time with the same order
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.back;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    //private methods
    private void validate(Item item) {
        if (item == null) {
            throw new NullPointerException("Can't insert null item in deque");
        }
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("No item in deque");
        }
    }

    /**
     * unit testing
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        //front and back
        assert deque.isEmpty();
        deque.addFirst(1);
        assert !deque.isEmpty();
        deque.removeFirst();
        assert deque.isEmpty();
        deque.addLast(2);
        assert !deque.isEmpty();
        deque.removeLast();
        assert deque.isEmpty();

        //mixed
        deque.addFirst(1);
        assert !deque.isEmpty();
        deque.removeLast();
        assert deque.isEmpty();
        deque.addLast(2);
        assert !deque.isEmpty();
        deque.removeFirst();
        assert deque.isEmpty();

        //many elements
        deque.addFirst(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addFirst(0);

        //iterator
        Iterator<Integer> it = deque.iterator();
        assert it.hasNext();
        assert it.next() == 0;
        assert it.next() == 1;
        assert it.next() == 2;
        assert it.next() == 3;
        assert !it.hasNext();

        //assert on elements
        assert deque.size == 4;
        assert !deque.isEmpty();
        if (deque.removeFirst() != 0) throw new AssertionError();
        if (deque.removeLast() != 3) throw new AssertionError();
        if (deque.removeFirst() != 1) throw new AssertionError();
        if (deque.removeLast() != 2) throw new AssertionError();
        assert deque.isEmpty();

        //assert on exceptions
        try {
            it.next();
            assert false : "no more elements to iterate";
        } catch (NoSuchElementException e) {
            StdOut.print();
        }
        try {
            it.remove();
            assert false : "should not happen";
        } catch (UnsupportedOperationException e) {
            StdOut.print();
        }

        try {
            deque.removeFirst();
            assert false : "Can't remove from empty deque";
        } catch (NoSuchElementException e) {
            StdOut.print();
        }
        try {
            deque.removeLast();
            assert false : "Can't remove from empty deque";
        } catch (NoSuchElementException e) {
            StdOut.print();
        }
        try {
            deque.addFirst(null);
            assert false : "Can't add null elements";
        } catch (NullPointerException e) {
            StdOut.print();
        }
        try {
            deque.addLast(null);
            assert false : "Can't add null elements";
        } catch (NullPointerException e) {
            StdOut.print();
        }
    }
}