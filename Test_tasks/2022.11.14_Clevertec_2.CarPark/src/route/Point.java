package route;

public class Point {
    private String pointName;
    private int coordinateX;
    private int coordinateY;

    public Point(String pointName, int coordinateX, int coordinateY) {
        this.pointName = pointName;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    /**
     * Класс для теста (получаем расстояние между двумя точками)
     * В данном тесте по прямой
     *
     * @param point1
     * @param point2
     * @return
     */
    public static int getDistance(Point point1, Point point2) {
        System.out.print(String.format("Вычисляем расстояние между %s и %s", point1.pointName, point2.pointName));
        int result;
        result = (int) Math.sqrt(Math.pow((point1.coordinateX - point2.coordinateX), 2) + Math.pow((point1.coordinateY - point2.coordinateY), 2));
        return result;
    }

    public String getPointName() {
        return pointName;
    }
}
