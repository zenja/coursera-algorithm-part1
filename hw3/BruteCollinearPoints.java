import java.util.ArrayList;

/**
 * Created by wangxing on 2/11/17.
 */
public class BruteCollinearPoints {
    private ArrayList<LineSegment> segs = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

        Point aux[] = new Point[4];
        for (int i1 = 0; i1 < points.length; i1++) {
            Point p1 = points[i1];
            for (int i2 = 0; i2 < i1; i2++) {
                Point p2 = points[i2];
                for (int i3 = 0; i3 < i2; i3++) {
                    Point p3 = points[i3];
                    for (int i4 = 0; i4 < i3; i4++) {
                        Point p4 = points[i4];
                        double s12 = p1.slopeTo(p2);
                        double s13 = p1.slopeTo(p3);
                        double s14 = p1.slopeTo(p4);
                        if (s12 == s13 && s13 == s14) {
                            // Find max and min point
                            aux[0] = p1;
                            aux[1] = p2;
                            aux[2] = p3;
                            aux[3] = p4;
                            Point maxP = p1, minP = p1;
                            for (int i = 1; i < aux.length; i++) {
                                if (maxP.compareTo(aux[i]) < 0) maxP = aux[i];
                                if (minP.compareTo(aux[i]) > 0) minP = aux[i];
                            }
                            segs.add(new LineSegment(maxP, minP));
                        }
                    }
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
