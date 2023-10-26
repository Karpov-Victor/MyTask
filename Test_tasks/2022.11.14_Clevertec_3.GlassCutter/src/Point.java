import java.util.Comparator;
import java.util.Objects;

public class Point {
    protected int coordinateX;
    protected int coordinateY;

    public Point(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    /**
     * Возвращает кратчайшее расстояние между двумя точками.
     * @param point1
     * @param point2
     * @return
     */
    public static double getDistance(Point point1, Point point2) {
        double result = Math.sqrt(Math.pow((point1.coordinateX - point2.coordinateX), 2) + Math.pow((point1.coordinateY - point2.coordinateY), 2));
        return result;
    }

    /**
     * Определяет какая из точек, указынных в входных параметрах, ближе к точке-объекту
     * @param point1
     * @param point2
     * @return
     */
    public int compareDistance(Point point1, Point point2) {
        double distance1 = getDistance(this, point1);
        double distance2 = getDistance(this, point2);
        return (distance1 < distance2) ? -1 : ((distance1 == distance2) ? 0 : 1);
        // return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return coordinateX == point.coordinateX && coordinateY == point.coordinateY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinateX, coordinateY);
    }

    @Override
    public String toString() {
        return "Point {" +
                coordinateX +
                ", " + coordinateY +
                '}';
    }

    /**
     * Преобразует строку вида "0, 15" в точку
     * @param inputLine
     * @return
     */
    public static Point pointFromStr(String inputLine) {
        try {
            int[] arr = new int[2];
            String[] strArr = inputLine.split(", ");
            return new Point(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }


}
