import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Deque
 *
 * Corner cases.
 * 1. Throw a java.lang.NullPointerException if the client attempts to add a null item;
 * 2. Throw a java.util.NoSuchElementException
 *    if the client attempts to remove an item from an empty deque;
 * 3. Throw a java.lang.UnsupportedOperationException
 *    if the client calls the remove() method in the iterator;
 * 4. Throw a java.util.NoSuchElementException
 *    if the client calls the next() method in the iterator and there are no more items to return.
 *
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int sz = 0;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return sz;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException("Cannot add null item");

        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.next = first;
        if (first != null) {
            first.previous = newFirst;
        } else {
            last = newFirst;
        }
        first = newFirst;

        sz++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException("Cannot add null item");

        Node newLast = new Node();
        newLast.item = item;
        newLast.previous = last;
        if (last != null) {
            last.next = newLast;
        } else {
            first = newLast;
        }
        last = newLast;

        sz++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove from an empty deque");

        Node oldFirst = first;
        if (first.next != null) {
            first.next.previous = null;
        } else {
            last = null;
        }
        first = first.next;

        sz--;

        return oldFirst.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Cannot remove from an empty deque");

        Node oldLast = last;
        if (last.previous != null) {
            last.previous.next = null;
        } else {
            first = null;
        }
        last = last.previous;

        sz--;

        return oldLast.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("Calling next() on an empty iterator");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove in deque iterator");
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        StdOut.println(deque.isEmpty());
        deque.addLast(2);
        deque.addFirst(0);
        for (Integer i : deque) {
            StdOut.println(i);
        }
        StdOut.println("Size: " + deque.size());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.isEmpty());
    }
}
