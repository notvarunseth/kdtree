import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.Iterator;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p) {
            this.p = p;
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
        this.mySize++;
        if (root == null) {
            root = new Node(p);
            root.rect = new RectHV(p.x(), 0, p.x(), 1);
            return;
        }

        Node pointer = root;
        while (true) {
            if (pointer.rect.xmin() == pointer.rect.xmax()) {
                // vertical partition
                // compare only x
                double compared = (p.x() - pointer.p.x());
                if (compared <= 0) {
                    if (pointer.lb == null) {
                        pointer.lb = new Node(p);
                        pointer.lb.rect = new RectHV(0, p.y(), pointer.rect.xmin(), p.y());
                        break;
                    }
                    else {
                        pointer = pointer.lb;
                    }
                }
                else {
                    if (pointer.rt == null) {
                        pointer.rt = new Node(p);
                        pointer.rt.rect = new RectHV(pointer.rect.xmin(), p.y(), 1, p.y());
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
                        pointer.lb.rect = new RectHV(p.x(), 0, p.x(), pointer.rect.ymax());
                        break;
                    }
                    else {
                        pointer = pointer.lb;
                    }
                }
                else {
                    if (pointer.rt == null) {
                        pointer.rt = new Node(p);
                        pointer.rt.rect = new RectHV(p.x(), pointer.rect.ymin(), p.x(), 1);
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
        node.p.draw();
        node.rect.draw();
        drawDFS(node.lb);
        drawDFS(node.rt);
    }

    public Point2D nearest(Point2D p) {
        return null;
    }

    public Iterable<Point2D> range(RectHV rect) {
        return new Iterable<Point2D>() {
            public Iterator<Point2D> iterator() {
                return Collections.emptyIterator();
            }
        };

    }


    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.draw();

        tree.insert(new Point2D(0.5, 0.4));
        tree.draw();
        tree.insert(new Point2D(0.2, 0.3));
        tree.draw();
        tree.insert(new Point2D(0.4, 0.7));
        tree.draw();
        tree.insert(new Point2D(0.9, 0.6));
        tree.draw();

        KdTree ps1 = new KdTree();
        StdOut.println("isEmpty" + ps1.isEmpty());
        ps1.insert(new Point2D(0.10, 0.10));
        ps1.insert(new Point2D(0.10, 0.15));
        ps1.insert(new Point2D(0.15, 0.10));
        ps1.insert(new Point2D(0.15, 0.15));
        StdOut.println("isEmpty" + ps1.isEmpty());

        // ps1.draw();

        // ps1.draw();

        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.11, 0.11)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.11, 0.14)));
        StdOut.println("Nearest: " + ps1.nearest(new Point2D(0.15, 0.12)));

        // check rect.
        RectHV rect = new RectHV(0.10, 0.10, 0.12, 0.12);
        for (Point2D p : ps1.range(rect)) {
            StdOut.println("range" + p);
        }
        // rect.draw();
        // ps1.draw();

    }
}
