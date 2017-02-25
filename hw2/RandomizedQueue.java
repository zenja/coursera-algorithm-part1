import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int sz = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the queue
    public int size() {
        return sz;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException("Cannot add null item");

        if (sz == items.length) resize(2 * sz);

        int randomIdx = StdRandom.uniform(sz + 1);
        items[sz++] = items[randomIdx];
        items[randomIdx] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Cannot dequeue from an empty queue");

        Item result = items[sz - 1];
        items[sz - 1] = null; // avoid loitering
        sz--;
        if (sz < items.length / 4) {
            resize(items.length / 2);
        }
        return result;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Cannot sample from an empty queue");
        return items[StdRandom.uniform(sz)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        int currentIdx = 0;
        Item[] arr;

        public RandomizedQueueIterator() {
            arr = (Item[]) new Object[sz];
            for (int i = 0; i < sz; i++) {
                arr[i] = items[i];
            }
            StdRandom.shuffle(arr);
        }

        @Override
        public boolean hasNext() {
            return currentIdx < arr.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Calling next() on an empty iterator");
            }

            return arr[currentIdx++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove in RandomizedQueue iterator");
        }
    }

    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        for (int i = 0; i < sz; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    // unit testing (optional)
    public static void main(String[] args) {
    }
}
