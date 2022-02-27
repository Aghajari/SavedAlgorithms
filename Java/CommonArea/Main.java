
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Rect[] rects = new Rect[2];
        Scanner scanner = new Scanner(System.in).useDelimiter("[\\s,]");

        for (int i = 0; i < rects.length; i++) {
            Point[] points = new Point[4];

            for (int j = 0; j < 4; j++)
                points[j] = new Point(scanner.nextInt(), scanner.nextInt());

            rects[i] = new Rect(points);
        }

        Rect common = findCommonRect(rects[0], rects[1], false);
        System.out.println(common == null ? "False" : "True\n" + (int) common.getArea());
    }

    public static Rect findCommonRect(Rect rect1, Rect rect2, boolean checkForCollision) {
        Point[] points = new Point[4];
        int index = 0;

        for (int i = 0; i < 4; i++) {
            if (rect1.contains(rect2.getPoints()[i]))
                points[index++] = rect2.getPoints()[i];
        }

        if (checkForCollision && (index == 4 || index == 0))
            return null;

        for (int i = 0; i < 4; i++) {
            if (rect2.contains(rect1.getPoints()[i]) && !exists(points, rect1.getPoints()[i]))
                points[index++] = rect1.getPoints()[i];

            if (index == 4)
                break;
        }

        if (index == 0)
            return null;

        if (index != 4) {
            Line[] vertical1 = rect1.getVerticalLines(), vertical2 = rect2.getVerticalLines();
            Line[] horizontal1 = rect1.getHorizontalLines(), horizontal2 = rect2.getHorizontalLines();

            index = intersection(points, index, vertical1, horizontal2);
            if (index != 4)
                index = intersection(points, index, vertical2, horizontal1);
        }

        if (index != 4) // Only a point
            return new Rect(points[0], points[0], points[0], points[0]);

        return new Rect(points);
    }

    private static int intersection(Point[] points, int index, Line[] vertical, Line[] horizontal) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Point p = vertical[i].intersectionPoint(horizontal[j], true);
                if (p != null && !exists(points, p))
                    points[index++] = p;

                if (index == 4)
                    return index;
            }
        }
        return index;
    }

    private static boolean exists(Point[] points, Point point) {
        for (Point value : points) {
            if (value == null)
                break;

            if (point.equals(value))
                return true;
        }
        return false;
    }

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Point))
                return false;
            return ((Point) obj).x == x && ((Point) obj).y == y;
        }

        public double distance(Point point) {
            return distance(this, point);
        }

        public double angle(Point point) {
            return angle(this, point);
        }

        public static double distance(Point p1, Point p2) {
            return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        }

        public static double angle(Point center, Point point) {
            if (center.x == point.x && center.y == point.y)
                return 0;

            double a = Math.atan((double) (point.y - center.y) / (point.x - center.x));
            if (point.x < center.x)
                return Math.PI + a;
            if (a < 0)
                return a + 2 * Math.PI;
            return a;
        }

        public static void sortPoints(Point[] points) {
            Point point = points[0];
            for (int i = 1; i < points.length; ++i)
                if (points[i].y < point.y || (points[i].y == point.y && points[i].x < point.x))
                    point = points[i];

            for (int i = 0; i < points.length - 1; i++)
                for (int j = 0; j < points.length - 1; j++) {
                    double a1 = point.angle(points[j]);
                    double a2 = point.angle(points[j + 1]);
                    if (a1 > a2 || (a1 == a2 && point.distance(points[j]) > point.distance(points[j + 1]))) {
                        Point p = points[j + 1];

                        points[j + 1] = points[j];
                        points[j] = p;
                    }
                }
        }
    }

    public static class Line {
        public final Point[] points;

        public Line(Point point1, Point point2) {
            Point.sortPoints(this.points = new Point[]{point1, point2});
        }

        public double getSlope() {
            if (points[0].x == points[1].x)
                return Double.NaN;

            return (points[0].y - points[1].y) / ((points[0].x - points[1].x) * 1.0);
        }

        public double getIntercept() {
            double m = getSlope();
            if (Double.isNaN(m))
                return points[0].x;

            return points[0].y - m * points[0].x;
        }

        public boolean isOnThisSegment(Point point) {
            return isOnThisSegment(point.x, point.y);
        }

        public boolean isOnThisSegment(double x, double y) {
            return points[0].x <= x && points[1].x >= x && points[1].y >= y && points[0].y <= y;
        }

        public Point intersectionPoint(Line line, boolean segment) {
            double m1 = getSlope(), m2 = line.getSlope();
            double b1 = getIntercept(), b2 = line.getIntercept();
            double x, y;

            if (m1 == m2)
                return null;

            if (Double.isNaN(m1)) {
                x = b1;
                y = m2 * x + b2;
            } else if (Double.isNaN(m2)) {
                x = b2;
                y = m1 * x + b1;
            } else {
                x = (b2 - b1) / (m1 - m2);
                y = m1 * x + b1;
            }

            if (segment && (!isOnThisSegment(x, y) || !line.isOnThisSegment(x, y)))
                return null;

            return new Point((int) x, (int) y);
        }
    }

    // TO-DO: implement triangle and other shapes
    public static abstract class Shape {
        protected final Point[] points;

        public Shape(Point... points) {
            if (points.length < 3)
                throw new RuntimeException("A shape must contain at least 3 points");

            Point.sortPoints(this.points = points);
        }

        public double getArea() {
            double area = 0.0;
            // Shoelace formula
            for (int i = 0, j = points.length - 1; i < points.length; j = i, i++)
                area += (points[j].x + points[i].x) * (points[j].y - points[i].y);

            return Math.abs(area / 2.0);
        }

        public Point[] getPoints() {
            return points;
        }
    }

    public static class Rect extends Shape {

        public Rect(Point... points) {
            super(points);

            if (points.length != 4)
                throw new RuntimeException("Rect must contains 4 points!");

            if (getBottomLeft().x != getTopLeft().x || getTopRight().x != getBottomRight().x
                    || getTopLeft().y != getTopRight().y || getBottomLeft().y != getBottomRight().y)
                throw new RuntimeException("The rectangle is not parallel!");

            if (points[0].equals(points[1]))
                throw new RuntimeException("These points draw only a " + (points[1].equals(points[2]) ? "point" : "line") + "!");
        }

        public Point getTopLeft() {
            return points[3];
        }

        public Point getTopRight() {
            return points[2];
        }

        public Point getBottomRight() {
            return points[1];
        }

        public Point getBottomLeft() {
            return points[0];
        }

        @Override
        public String toString() {
            return Arrays.toString(points);
        }

        public int getRight() {
            return getBottomRight().x;
        }

        public int getLeft() {
            return getTopLeft().x;
        }

        public int getTop() {
            return getTopRight().y;
        }

        public int getBottom() {
            return getBottomLeft().y;
        }

        @Override
        public double getArea() {
            //return super.getArea();
            return (getRight() - getLeft()) * (getTop() - getBottom());
        }

        public boolean contains(Point point) {
            return point.x >= getLeft() && point.x <= getRight()
                    && point.y <= getTop() && point.y >= getBottom();
        }

        public Line[] getVerticalLines() {
            return new Line[]{
                    new Line(getBottomLeft(), getTopLeft()),
                    new Line(getBottomRight(), getTopRight())
            };
        }

        public Line[] getHorizontalLines() {
            return new Line[]{
                    new Line(getBottomLeft(), getBottomRight()),
                    new Line(getTopLeft(), getTopRight())
            };
        }
    }
}
