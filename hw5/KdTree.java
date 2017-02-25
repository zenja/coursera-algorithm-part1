import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxing on 2/23/17.
 */
public class KdTree {
    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("point is null");
        root = insert(root, p, true, 0.0, 0.0, 1.0, 1.0);
    }

    // helper method for insert(Point2D)
    private Node insert(Node node, Point2D p, boolean isVertical, double xmin, double ymin, double xmax, double ymax) {
        if (node == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null);
        }

        // if already contains the point, ignore
        if (node.p.equals(p)) return node;

        if (isVertical && p.x() < node.p.x()) {
            node.lb = insert(node.lb, p, !isVertical, xmin, ymin, node.p.x(), ymax);
        }
        if (isVertical && p.x() >= node.p.x()) {
            node.rt = insert(node.rt, p, !isVertical, node.p.x(), ymin, xmax, ymax);
        }
        if (!isVertical && p.y() < node.p.y()) {
            node.lb = insert(node.lb, p, !isVertical, xmin, ymin, xmax, node.p.y());
        }
        if (!isVertical && p.y() >= node.p.y()) {
            node.rt = insert(node.rt, p, !isVertical, xmin, node.p.y(), xmax, ymax);
        }

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("point is null");
        return contains(root, p);
    }

    // helper function for contains(Point2D)
    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;

        if (!node.rect.contains(p)) return false;

        if (node.p.equals(p)) return true;

        return contains(node.lb, p) || contains(node.rt, p);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    // helper method for draw
    private void draw(Node node, boolean isVertical) {
        if (node == null) return;

        // Set line color and weight
        StdDraw.setPenRadius();
        if (isVertical) StdDraw.setPenColor(StdDraw.RED);
        else StdDraw.setPenColor(StdDraw.BLUE);

        // Decide line start and end point
        double xMin = 0.0, yMin = 0.0, xMax = 0.0, yMax = 0.0;
        if (isVertical) {
            xMin = node.p.x();
            xMax = xMin;
            yMin = node.rect.ymin();
            yMax = node.rect.ymax();
        } else {
            xMin = node.rect.xmin();
            xMax = node.rect.xmax();
            yMin = node.p.y();
            yMax = yMin;
        }

        // Draw the line
        StdDraw.line(xMin, yMin, xMax, yMax);

        // Draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();

        // Draw left tree
        draw(node.lb, !isVertical);

        // Draw right tree
        draw(node.rt, !isVertical);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("rect is null");

        if (isEmpty()) return new ArrayList<Point2D>();

        List<Point2D> resultPoints = new ArrayList<Point2D>();
        range(rect, root, resultPoints);
        return resultPoints;
    }

    // helper method for range
    private void range(RectHV rect, Node node, List<Point2D> points) {
        if (rect.contains(node.p)) points.add(node.p);

        if (node.lb != null && rect.intersects(node.lb.rect)) {
            range(rect, node.lb, points);
        }

        if (node.rt != null && rect.intersects(node.rt.rect)) {
            range(rect, node.rt, points);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("point is null");

        if (isEmpty()) return null;

        return nearest(p, root, true, root.p.distanceSquaredTo(p), root.p);
    }

    private Point2D nearest(Point2D p, Node node, boolean isVertical, double minDistSquare, Point2D nearestSoFar) {
        Point2D newNearest = nearestSoFar;
        double newMinDistSquare = minDistSquare;
        double distSquare = p.distanceSquaredTo(node.p);
        if (distSquare < minDistSquare) {
            newMinDistSquare = distSquare;
            newNearest = node.p;
        }

        // decide which subtree to search first
        boolean lbFirst = false;
        if (isVertical) {
            if (p.x() <= node.p.x()) lbFirst = true;
        } else {
            if (p.y() <= node.p.y()) lbFirst = true;
        }

        if (lbFirst) {
            if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < minDistSquare) {
                newNearest = nearest(p, node.lb, !isVertical, newMinDistSquare, newNearest);
                newMinDistSquare = p.distanceSquaredTo(newNearest);
            }
            if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < minDistSquare) {
                newNearest = nearest(p, node.rt, !isVertical, newMinDistSquare, newNearest);
            }
        } else {
            if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < minDistSquare) {
                newNearest = nearest(p, node.rt, !isVertical, newMinDistSquare, newNearest);
                newMinDistSquare = p.distanceSquaredTo(newNearest);
            }
            if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < minDistSquare) {
                newNearest = nearest(p, node.lb, !isVertical, newMinDistSquare, newNearest);
            }
        }

        return newNearest;
    }

    private static class Node {
        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }

        private Point2D p;          // the point
        private RectHV rect;        // the axis-aligned rectangle corresponding to this node
        private Node lb;            // the left/bottom subtree
        private Node rt;            // the right/top subtree
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        // initialize the two data structures with point from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        kdtree.draw();
        System.out.println(kdtree.size());
    }
}
