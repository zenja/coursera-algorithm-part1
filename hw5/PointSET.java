import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by wangxing on 2/23/17.
 */
public class PointSET {
    private TreeSet<Point2D> tree = new TreeSet<Point2D>();

    // construct an empty set of points
    public PointSET() {}

    // is the set empty?
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return tree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("point is null");
        tree.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("point is null");
        return tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : tree) {
            p.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("rect is null");
        List<Point2D> pointsInRect = new ArrayList<Point2D>();
        for (Point2D p : tree) {
            if (rect.contains(p)) pointsInRect.add(p);
        }
        return pointsInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("point is null");

        if (isEmpty()) return null;

        double minDistance = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D pInTree : tree) {
            double dist = pInTree.distanceTo(p);
            if (dist < minDistance) {
                minDistance = dist;
                nearestPoint = pInTree;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
