import java.util.Objects;

public final class Point {
    private double xCoord;
    private double yCoord;

    Point(double xCoord, double yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    double getDistance(Point other) {
        double xDiff = Math.abs(xCoord - other.xCoord);
        double yDiff = Math.abs(yCoord - other.yCoord);
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.xCoord, xCoord) == 0 &&
                Double.compare(point.yCoord, yCoord) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoord, yCoord);
    }

    @Override
    public String toString() {
        return "Point{" +
                "xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }
}
