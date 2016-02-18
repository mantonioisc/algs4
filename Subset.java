import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Subset {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        String[] strings = StdIn.readAllStrings();
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        for (String string : strings) {
            queue.enqueue(string);
        }
        Iterator<String> it = queue.iterator();
        for (int i = 0; i < n && it.hasNext(); i++) {
            StdOut.println(it.next());
        }
    }
}
