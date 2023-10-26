import java.io.BufferedReader;
import java.io.IOException;

public class Figure {

    /**
     * Массив точек фигуры. Точка с индексом 0 считается нулевой.
     */
    protected Point[] points = new Point[4];

    /**
     * Положение фигуры на листе (совпадает с точкой c индексом 0 в массиве точек фигуры)
     */
    protected Point nullPoint;

    public Figure(Point[] points, Point nullPoint) {
        this.points = points;
        this.nullPoint = nullPoint;
    }

    /**
     * Возвращает массив из четырёх сегментов (сторон фигуры).
     * Перемещает фигуру на плоскости листа в соответствии с нулевой точкой
     * @param figure
     * @return
     */
    public static Segment[] getSegmentsFromFigure(Figure figure) {
        Segment[] segments = new Segment[4];
        for (int i = 0; i < 4; i++) {
            Point point1 = figure.points[i];
            Point point2;
            if (i == 3) {
                point2 = figure.points[0];
            } else {
                point2 = figure.points[i + 1];
            }
            point1.coordinateX = point1.coordinateX + figure.nullPoint.coordinateX;
            point1.coordinateY = point1.coordinateY + figure.nullPoint.coordinateY;
            segments[i] = new Segment(point1, point2);
        }
        return segments;
    }

    /**
     * Парсинг данных фигуры из файла
     */
    public static Figure getFigureFromReader(BufferedReader bufferedReader) throws IOException {
        String line;
        line = bufferedReader.readLine();
        if (!line.startsWith("4 точки фигуры:")) return null;
        try {
            Point[] points = new Point[4];
            for (int i = 0; i < 4; i++) {
                line = bufferedReader.readLine();
                points[i] = Point.pointFromStr(line);
            }
            line = bufferedReader.readLine();
            if (!line.startsWith("Положение фигуры в СКЛ:")) return null;
            line = bufferedReader.readLine();
            Point nullPoint = Point.pointFromStr(line);
            return new Figure(points, nullPoint);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }
}
