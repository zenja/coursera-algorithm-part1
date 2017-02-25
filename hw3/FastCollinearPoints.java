import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wangxing on 2/11/17.
 */
public class FastCollinearPoints {
    private ArrayList<LineSegment> segs = new ArrayList<LineSegment>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException("points is null");

        // Check if there is null points
        for (Point p : points) {
            if (p == null) throw new NullPointerException("there is null point");
        }

        // Check if there is repeated points
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (j != i && points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("there are repeated points");
                }
            }
        }

        // Check if there is less than 4 points
        if (points.length < 4) return;

        Point aux[] = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            aux[i] = points[i];
        }

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            // sort by slope to point p
            Arrays.sort(aux, p.slopeOrder());

            // fixme debug
//            double slopes[] = new double[points.length];
//            for (int m = 0; m < points.length; m++) {
//                slopes[m] = p.slopeTo(aux[m]);
//            }
//            System.out.println();
//            System.out.println(p);
//            System.out.println(Arrays.toString(aux));
//            System.out.println(Arrays.toString(slopes));

            // look for range of same slopes
            int start = 0;
            while (start < aux.length) {
                if (start + 2 >= aux.length) break;
                double slopeToStart = p.slopeTo(aux[start]);
                if (slopeToStart == p.slopeTo(aux[start + 2])) {
                    int end = start + 3;
                    while (end < aux.length && p.slopeTo(aux[end]) == slopeToStart) end++;
                    // aux[start:end] (end is exclusive) forms a line, find out their min/max and add to segs
                    Point maxP = p, minP = p;
                    for (int x = start; x < end; x++) {
                        if (maxP.compareTo(aux[x]) < 0) maxP = aux[x];
                        if (minP.compareTo(aux[x]) > 0) minP = aux[x];
                    }
                    LineSegment seg = new LineSegment(minP, maxP);
                    if (maxP == p) {
                        segs.add(seg);
//                        System.out.println("Added seg: " + seg.toString());
                    }

                    start = end;
                } else {
                    start++;
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segs.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] results = new LineSegment[segs.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = segs.get(i);
        }
        return results;
    }
}
