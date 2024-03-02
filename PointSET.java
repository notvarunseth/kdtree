import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class PointSET {

    private SET<Point2D> innerSet = new SET<>();

    public PointSET() {

    }                              // construct an empty set of points

    public boolean isEmpty() {
        return innerSet.isEmpty();
    }                      // is the set empty?

    public int size() {
        return innerSet.size();
    }                        // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        innerSet.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        return innerSet.contains(p);
    }            // does the set contain point p?

    public void draw() {
        for (Point2D p : innerSet) {
            p.draw();
        }
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        // TODO
        return new Iterable<Point2D>() {
            public Iterator<Point2D> iterator() {
                return null;
            }
        };
    }             // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        return null;
    }            // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        PointSET ps1 = new PointSET();
        StdOut.println("isEmpty" + ps1.isEmpty());
        ps1.insert(new Point2D(10, 10));
        ps1.insert(new Point2D(10, 15));
        ps1.insert(new Point2D(15, 10));
        ps1.insert(new Point2D(15, 15));
        StdOut.println("isEmpty" + ps1.isEmpty());

        ps1.draw();

        
    }                  // unit testing of the methods (optional)
}
