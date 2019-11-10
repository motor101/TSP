import java.util.Arrays;
import java.util.Random;

class Path {
    static Random randomGenerator = new Random();
    private Point[] arr;
    private double length = 0;
    private boolean lengthIsUpToDate = false;

    Path(int size) {
        arr = new Point[size];
        outerloop:
        for (int i = 0; i < arr.length; i++) {
            double xCoord = randomGenerator.nextDouble() * 100;
            double yCoord = randomGenerator.nextDouble() * 100;
            Point newPoint = new Point(xCoord, yCoord);

            // check if the new Point doesn't equal some of the old points
            for (int j = 0; j < i; j++) {
                if (newPoint.equals(arr[j])) {
                    --i;
                    continue outerloop;
                }
            }
            arr[i] = newPoint;
        }
    }

    Path(Path other) {
        arr = Arrays.copyOf(other.arr, other.arr.length);
        if (other.lengthIsUpToDate) {
            length = other.length;
            lengthIsUpToDate = true;
        }
    }

    void setPoint(int index, Point value) {
        lengthIsUpToDate = false;
        arr[index] = value;
    }

    Point getPoint(int index) {
        return arr[index];
    }

    void mutate() {
        lengthIsUpToDate = false;

        int index1 = randomGenerator.nextInt(arr.length);
        int index2 = randomGenerator.nextInt(arr.length);

        Point tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
    }

    double getLength() {
        if (!lengthIsUpToDate) {
            length = 0;
            for (int i = 0; i < arr.length; i++) {
                length += arr[i].getDistance(arr[(i + 1) % arr.length]);
            }
            lengthIsUpToDate = true;
        }
        return length;
    }
}
