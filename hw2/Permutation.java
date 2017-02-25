import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        try {
            while (true) {
                rq.enqueue(StdIn.readString());
            }
        } catch (NoSuchElementException ex) {
            // do nothing
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
