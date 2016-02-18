import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    /**
     * array of items
     */
    private Item[] a;
    /**
     * number of elements on stack
     */
    private int n;

    /**
     * construct an empty randomized queue
     */
    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        a = (Item[]) new Object[8];
        n = 0;
    }

    /**
     * is the queue empty?
     *
     * @return true if is empty
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * return the number of items on the queue
     *
     * @return number of items
     */
    public int size() {
        return n;
    }

    /**
     * add the item
     *
     * @param item item to be added in random place
     */
    public void enqueue(Item item) {
        validate(item);

        if (n >= (a.length * 2 / 3)) { //if we are 2/3 full resize to double
            resize(2 * a.length);
        }

        a[getInsertPosition()] = item;
        n++;
    }


    /**
     * remove and return a random item
     *
     * @return a random item
     */
    public Item dequeue() {
        checkEmpty();

        int index = StdRandom.uniform(a.length);
        Item item = a[index];
        while (item == null) {
            index = StdRandom.uniform(a.length);
            item = a[index];
        }
        a[index] = null; //remove from array
        n--;

        // shrink size of array if necessary
        if (n > 0 && n == a.length / 4) resize(a.length / 2);

        return item;
    }

    /**
     * return (but do not remove) a random item
     *
     * @return a random item
     */
    public Item sample() {
        checkEmpty();

        int samplePosition = StdRandom.uniform(a.length);
        Item item = a[samplePosition];
        while (item == null) {
            item = a[StdRandom.uniform(a.length)];
        }

        return item;
    }

    /**
     * return an independent iterator over items in random order
     *
     * @return a different iterator each time with a unique random order
     */
    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        private int index;
        private Item[] shuffled;

        @SuppressWarnings("unchecked")
        public ReverseArrayIterator() {
            index = n - 1;
            shuffled = (Item[]) new Object[n];

            int i = 0; //to look for elements in the old array
            int j = 0; //to assign them in the new array

            while (j < n) {
                Item aux = a[i++];
                if (aux != null) {
                    shuffled[j++] = aux;
                }
            }
            StdRandom.shuffle(shuffled);
        }

        public boolean hasNext() {
            return index >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return shuffled[index--];
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
     * Returns always a non occupied position in the array. It re-sizes the array if necessary
     *
     * @return a non used position in the array
     */
    private int getInsertPosition() {
        //attempts to inserts it means it's kind of full, so resize it and compact it
        int numberOfAttempts = 0;
        int maxNumberAttempts = getMaxNumberAttempts();
        int insertPosition = StdRandom.uniform(a.length);

        //Do the max number of attempts, this should meet not quite often
        while (a[insertPosition] != null && numberOfAttempts < maxNumberAttempts) {
            insertPosition = StdRandom.uniform(a.length);
            numberOfAttempts++;
        }

        //make the array bigger and scramble it, so is more difficult to do collision
        if (a[insertPosition] != null && numberOfAttempts >= maxNumberAttempts) {
            resize(2 * a.length);
            do {
                insertPosition = StdRandom.uniform(a.length);
            } while (a[insertPosition] != null);
        }

        return insertPosition;
    }

    /**
     * Copies all elements to a bigger array, scrambling elements in the process
     *
     * @param capacity the new size of the internal array
     */
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        int i = 0; //to look for elements in the old array
        int j = 0; //to assign them in the new array


        while (j < n) {
            Item aux = a[i++];
            if (aux != null) {
                int samplePosition = StdRandom.uniform(temp.length);
                while (temp[samplePosition] != null) {
                    samplePosition = StdRandom.uniform(temp.length);
                }
                j++;
                temp[samplePosition] = aux;
            }
        }

        a = temp;
    }

    private int getMaxNumberAttempts() {
        return a.length - n;
    }

    private void show() {
        for (Item item : a) {
            if (item != null) {
                StdOut.print(item);
            } else {
                StdOut.print("_");
            }
            StdOut.print(" ");
        }
        StdOut.println();
    }

    /**
     * unit testing
     *
     * @param args command line args not expected
     */
    public static void main(String[] args) {
        //Test adding
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        assert rq.isEmpty();
        assert rq.size() == 0;
        rq.enqueue(0);
        rq.show();
        assert !rq.isEmpty();
        assert rq.size() == 1;
        rq.enqueue(1);
        rq.show();
        assert rq.size() == 2;
        rq.enqueue(2);
        rq.show();
        rq.enqueue(3);
        rq.show();
        rq.enqueue(4);
        rq.show();
        rq.enqueue(5);
        rq.show();
        rq.enqueue(6);
        rq.show();
        rq.enqueue(7);
        rq.show();
        rq.enqueue(8);
        rq.show();
        rq.enqueue(9);
        rq.show();
        assert rq.size() == 10;

        rq.sample();
        rq.sample();
        rq.sample();
        assert rq.size() == 10;

        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        rq.dequeue();
        rq.show();
        assert rq.size() == 0;
        assert rq.isEmpty();


        rq.enqueue(0);
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        assert rq.size() == 5;
        Iterator<Integer> it = rq.iterator();

        while (it.hasNext()) {
            Integer next = it.next();
            assert next != null;
        }

        //Test exceptions
        RandomizedQueue<Integer> rq2 = new RandomizedQueue<>();
        try {
            rq2.enqueue(null);
            assert false : "Can't add null elements";
        } catch (NullPointerException e) {
            StdOut.print();
        }
        try {
            rq2.sample();
            assert false : "Can't sample from empty queue";
        } catch (NoSuchElementException e) {
            StdOut.print();
        }
        try {
            rq2.dequeue();
            assert false : "Can't remove from empty queue";
        } catch (NoSuchElementException e) {
            StdOut.print();
        }

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
    }

}