import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV container;
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean vertical;

        private RectHV getRect() {
            if (vertical) {
                return new RectHV(p.x(), this.container.ymin(), p.x(), this.container.ymax());
            }
            else {
                return new RectHV(this.container.xmin(), p.y(), this.container.xmax(), p.y());
            }
        }

        public Node(Point2D p) {
            if (p == null) {
                throw new IllegalArgumentException("No");
            }
            this.p = p;
        }

        private boolean isVertical() {
            return this.vertical;
        }

    }

    private Node root;
    private int mySize = 0;

    public KdTree() {

    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        if (contains(p)) {
            return;
        }
        this.mySize++;
        if (root == null) {
            root = new Node(p);
            root.vertical = true;
            root.container = new RectHV(0, 0, 1, 1);
            return;
        }

        Node pointer = root;
        while (true) {
            if (pointer.isVertical()) {
                // vertical partition
                // compare only x
                double compared = (p.x() - pointer.p.x());
                if (compared <= 0) {
                    if (pointer.lb == null) {
                        pointer.lb = new Node(p);
                        // pointer.lb.rect = new RectHV(pointer.container.xmin(), p.y(),
                        //                              pointer.rect.xmax(), p.y());
                        pointer.lb.vertical = false;
                        pointer.lb.container = new RectHV(
                                pointer.container.xmin(), pointer.container.ymin(),
                                pointer.p.x(), pointer.container.ymax()
                        );
                        break;
                    }
                    else {
                        pointer = pointer.lb;
                    }
                }
                else {
                    if (pointer.rt == null) {
                        pointer.rt = new Node(p);
                        // pointer.rt.rect = new RectHV(pointer.rect.xmin(), p.y(),
                        //                              pointer.container.xmax(), p.y());
                        pointer.rt.vertical = false;
                        pointer.rt.container = new RectHV(
                                pointer.p.x(), pointer.container.ymin(),
                                pointer.container.xmax(), pointer.container.ymax()
                        );
                        break;
                    }
                    else {
                        pointer = pointer.rt;
                    }
                }
            }
            else {
                // horizontal partition
                // compare only y
                double compared = (p.y() - pointer.p.y());
                if (compared <= 0) {
                    if (pointer.lb == null) {
                        pointer.lb = new Node(p);
                        // pointer.lb.rect = new RectHV(p.x(), pointer.container.ymin(), p.x(),
                        //                              pointer.rect.ymax());
                        pointer.lb.vertical = true;
                        pointer.lb.container = new RectHV(
                                pointer.container.xmin(), pointer.container.ymin(),
                                pointer.container.xmax(), pointer.p.y()
                        );
                        break;
                    }
                    else {
                        pointer = pointer.lb;
                    }
                }
                else {
                    if (pointer.rt == null) {
                        pointer.rt = new Node(p);
                        // pointer.rt.rect = new RectHV(p.x(), pointer.rect.ymin(), p.x(),
                        //                              pointer.container.ymax());
                        pointer.rt.vertical = true;
                        pointer.rt.container = new RectHV(
                                pointer.container.xmin(), pointer.p.y(),
                                pointer.container.xmax(), pointer.container.ymax()
                        );
                        break;
                    }
                    else {
                        pointer = pointer.rt;
                    }
                }
            }

        }

    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        return mySize;
    }

    public void draw() {
        drawDFS(root);
    }

    private void drawDFS(Node node) {
        if (node == null) {
            return;
        }


        // StdDraw.setPenRadius(0.02);
        // StdDraw.setPenColor(StdDraw.YELLOW);
        // node.container.draw();


        drawDFS(node.lb);
        drawDFS(node.rt);


        StdDraw.setPenRadius(0.005);
        if (node.isVertical()) {
            StdDraw.setPenColor(StdDraw.RED);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
        }
        node.getRect().draw();


        StdDraw.setPenColor(StdDraw.BLACK);

        StdDraw.setPenRadius(0.01);
        node.p.draw();
    }

    private Point2D searchHelper(Point2D p, Node node, Point2D pSoFar) {
        if (node == null || p == null || node.p == null) {
            return pSoFar;
        }

        if (p.compareTo(node.p) == 0) {
            return node.p;
        }
        if (pSoFar == null || node.p.distanceSquaredTo(p) < pSoFar.distanceSquaredTo(p)) {
            pSoFar = node.p;
        }

        Node firstGo;
        Node secondGo;


        if ((node.isVertical() && p.x() <= node.p.x()) || (
                !node.isVertical() && p.y() <= node.p.y())) {
            // go left.
            firstGo = node.lb;
            secondGo = node.rt;
        }
        else {
            // go right
            firstGo = node.rt;
            secondGo = node.lb;
        }
        // now check firstGo
        if (firstGo != null) {
            pSoFar = searchHelper(p, firstGo, pSoFar);
        }

        if (secondGo != null && secondGo.container.distanceSquaredTo(p) < pSoFar.distanceSquaredTo(
                p)) {
            pSoFar = searchHelper(p, secondGo, pSoFar);
        }
        return pSoFar;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return searchHelper(p, this.root, null);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearestPoint = this.nearest(p);
        return p.equals(nearestPoint);
    }            // does the set contain point p?


    private void rectSearchHelper(Node node, RectHV rect, List<Point2D> results) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.p)) {
            results.add(node.p);
        }

        if (node.lb != null && node.lb.container.intersects(rect)) {
            rectSearchHelper(node.lb, rect, results);
        }
        if (node.rt != null && node.rt.container.intersects(rect)) {
            rectSearchHelper(node.rt, rect, results);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> results = new ArrayList<>();

        rectSearchHelper(root, rect, results);

        return results;

    }


    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        // tree.draw();

        tree.insert(new Point2D(0.5, 0.4));
        // tree.draw();
        tree.insert(new Point2D(0.2, 0.3));
        // tree.draw();
        tree.insert(new Point2D(0.4, 0.7));
        // tree.draw();
        tree.insert(new Point2D(0.9, 0.6));
        // tree.draw();

        KdTree ps1 = new KdTree();
        StdOut.println("isEmpty" + ps1.isEmpty());
        ps1.insert(new Point2D(0.0, 0.10));
        ps1.insert(new Point2D(0.001, 0.10));
        ps1.insert(new Point2D(0.001, 0.1001));
        ps1.insert(new Point2D(0.0, 0.1001));
        ps1.insert(new Point2D(0.375, 0.0));
        ps1.insert(new Point2D(0.375, 0.0625));
        ps1.insert(new Point2D(0.10, 0.10));
        ps1.insert(new Point2D(0.10, 0.15));
        ps1.insert(new Point2D(0.15, 0.10));
        ps1.insert(new Point2D(0.15, 0.15));
        ps1.insert(new Point2D(0.1223, 0.1439));
        ps1.insert(new Point2D(0.09123123, 0.12323));
        ps1.insert(new Point2D(0.1123423, 0.12234234));
        ps1.insert(new Point2D(0.13132123, 0.10123123));
        ps1.insert(new Point2D(0.102342, 0.10123123123));
        ps1.insert(new Point2D(0.8359375, 0.0));
        ps1.insert(new Point2D(0.84375, 0.0078125));
        ps1.insert(new Point2D(0.0, 0.2421875));
        ps1.insert(new Point2D(0.0, 0.25));

        StdOut.println("isEmpty" + ps1.isEmpty());

        // ps1.draw();

        // ps1.draw();

        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.8359375, 0.0)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.375, 0.0)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.0, 0.10)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.11, 0.11)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.11, 0.14)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.15, 0.12)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.0, 0.25)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.0, 0.2421875)));

        // check rect.
        RectHV rect = new RectHV(0.09, 0.09, 0.12, 0.12);
        for (Point2D p : ps1.range(rect)) {
            StdOut.println("range: " + p);
        }
        // rect.draw();
        // ps1.draw();

        StdOut.println("Contains1? " + ps1.contains(new Point2D(0.1, 0.1)));
        StdOut.println("Contains2? " + ps1.contains(new Point2D(0.1, 0.2)));


    }
}
