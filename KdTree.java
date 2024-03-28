import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private RectHV container;
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p) {
            if (p == null) {
                throw new IllegalArgumentException("No");
            }
            this.p = p;
        }

        private boolean isVertical() {
            return this.rect.xmin() == this.rect.xmax();
        }

        // private Point2D lbRect() {
        //     if (this.lb == null) {
        //         return null;
        //     }
        //     if (this.isVertical()) {
        //         //
        //     }
        // }
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
            root.rect = new RectHV(p.x(), 0, p.x(), 1);
            root.container = new RectHV(0, 0, 1, 1);
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
                        pointer.lb.rect = new RectHV(pointer.container.xmin(), p.y(),
                                                     pointer.rect.xmin(), p.y());
                        pointer.lb.container = new RectHV(
                                pointer.container.xmin(), pointer.container.ymin(),
                                pointer.rect.xmax(), pointer.container.ymax()
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
                        pointer.rt.rect = new RectHV(pointer.rect.xmin(), p.y(),
                                                     pointer.container.xmax(), p.y());
                        pointer.rt.container = new RectHV(
                                pointer.rect.xmin(), pointer.container.ymin(),
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
                        pointer.lb.rect = new RectHV(p.x(), pointer.container.ymin(), p.x(),
                                                     pointer.rect.ymax());
                        pointer.lb.container = new RectHV(
                                pointer.container.xmin(), pointer.container.ymin(),
                                pointer.container.xmax(), pointer.rect.ymax()
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
                        pointer.rt.rect = new RectHV(p.x(), pointer.rect.ymin(), p.x(),
                                                     pointer.container.ymax());
                        pointer.rt.container = new RectHV(
                                pointer.container.xmin(), pointer.rect.ymin(),
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
        node.p.draw();
        node.rect.draw();
        drawDFS(node.lb);
        drawDFS(node.rt);
    }

    // private class SearchResult {
    //     public SearchResult (Node node, ) {
    //
    //     }
    //
    // }

    private Point2D searchHelper(Point2D p, Node node, Point2D pSoFar) {
        if (node == null || p == null || node.p == null) {
            return pSoFar;
        }

        if (p.compareTo(node.p) == 0) {
            return node.p;
        }
        if (pSoFar == null || p.distanceSquaredTo(node.p) < pSoFar.distanceSquaredTo(p)) {
            pSoFar = node.p;
        }

        Node firstGo;
        Node secondGo;


        if ((node.rect.xmin() == node.rect.xmax() && p.x() <= node.rect.xmin()) || (
                node.rect.ymin() == node.rect.ymax() && p.y() <= node.rect.ymin())) {
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
            Point2D pNew = searchHelper(p, firstGo, pSoFar);
            if (pNew != null && pNew.distanceSquaredTo(p) < pSoFar.distanceSquaredTo(p)) {
                pSoFar = pNew;
            }
        }

        if (secondGo != null && node.rect.distanceSquaredTo(p) < pSoFar.distanceSquaredTo(p)) {
            //
            Point2D pNew = searchHelper(p, secondGo, pSoFar);
            if (pNew != null && pNew.distanceSquaredTo(p) < pSoFar.distanceSquaredTo(p)) {
                pSoFar = pNew;
            }
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
        RectHV rect = new RectHV(0.09, 0.09, 0.12, 0.12);
        for (Point2D p : ps1.range(rect)) {
            StdOut.println("range" + p);
        }
        // rect.draw();
        // ps1.draw();

        StdOut.println("Contains1? " + ps1.contains(new Point2D(0.1, 0.1)));
        StdOut.println("Contains2? " + ps1.contains(new Point2D(0.1, 0.2)));
    }
}
