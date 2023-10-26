import java.io.*;
import java.util.*;

// https://ru.onlinemschool.com/math/library/vector/multiply/

public class Main {
    /**
     * Включает в себя все неотсортированные сегменты.
     * HashSet используется для того, что бы сразу удалять дублирующие себя сегменты
     */
    HashSet<Segment> hashSegments = new HashSet<>();

    /**
     * Включает в себя последовательность сегментов. В этой последовательности резак будет проходить сегменты
     */
    ArrayList<Segment> segments = new ArrayList<>();

    /**
     * Определяет, являются ли следующие входные данные, данными фигуры
     * @return
     */
    public static boolean isNextFigure(String line) {
        if (line.startsWith("Фигура")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Тесты
     */
    public static void mainTests() {
        List<Point> points = new ArrayList<>();
        ArrayList<Segment> testSegments = new ArrayList<>();
        Point point1, point2;
        Segment testSegment;

        // Проверка создания точек
        points.add(new Point(0, 0));
        points.add(new Point(0, 100));
        points.add(new Point(10, 500));
        points.add(new Point(20, 20));
        points.add(new Point(20, 20));

        for (Point point : points) System.out.println(point.toString());

        System.out.println("\n---Проверка равенства точек---");
        System.out.println("Результат сравнения точек " + points.get(0) + " и " + points.get(0) + " = " + points.get(0).equals(points.get(0)));
        System.out.println("Результат сравнения точек " + points.get(0) + " и " + points.get(1) + " = " + points.get(0).equals(points.get(1)));
        System.out.println("Результат сравнения точек " + points.get(2) + " и " + points.get(3) + " = " + points.get(2).equals(points.get(3)));
        System.out.println("Результат сравнения точек " + points.get(3) + " и " + points.get(3) + " = " + points.get(3).equals(points.get(3)));
        System.out.println("Результат сравнения точек " + points.get(3) + " и " + points.get(4) + " = " + points.get(3).equals(points.get(4)));

        System.out.println("\n---Проверка вычисления расстояния между двумя точками---");
        point1 = points.get(0);
        point2 = points.get(1);
        System.out.println("Расстояние между точками " + point1 + " и " + point2 + " = " + Point.getDistance(point1, point2));
        System.out.println("Расстояние между точками " + point2 + " и " + point1 + " = " + Point.getDistance(point2, point1));
        System.out.println("Расстояние между точками " + point1 + " и " + point1 + " = " + Point.getDistance(point1, point1));

        System.out.println("\n---Проверка создания сегментов---");
        testSegments.add(new Segment(points.get(0), points.get(1)));
        testSegments.add(new Segment(points.get(2), points.get(1)));
        testSegments.add(new Segment(points.get(2), points.get(3)));
        testSegments.add(new Segment(points.get(0), points.get(0)));
        for (Segment segment : testSegments) System.out.println(segment.toString());

        System.out.println("\n---Проверка является ли сегмент точкой---");
        testSegment = new Segment(new Point(10, 0), new Point(10, 0));
        System.out.println("Сегмент " + testSegment + " является точкой? Ответ: " + testSegment.isSegmentAsPoint());
        testSegment = new Segment(new Point(10, 0), new Point(11, 0));
        System.out.println("Сегмент " + testSegment + " является точкой? Ответ: " + testSegment.isSegmentAsPoint());

        // Проверка равенства сегментов
        System.out.println("\n======= Проверка равенства сегментов =======");
        System.out.println(new Segment(0, 0, 10, 10).equals(new Segment(0, 0, 10, 10)) + "\t expected TRUE");
        System.out.println(new Segment(0, 0, 10, 10).equals(new Segment(10, 10, 0, 0)) + "\t expected TRUE");
        System.out.println(new Segment(0, 0, 10, 10).equals(new Segment(2, 2, 10, 10)) + "\t expected FALSE");
        System.out.println(new Segment(10, 0, 10, 0).equals(new Segment(10, 0, 10, 0)) + "\t expected TRUE");
        System.out.println(new Segment(0, 0, 10, 0).equals(new Segment(10, 0, 5, 0)) + "\t expected FALSE");
        System.out.println("\n======= Проверка равенства hash-кодов =======");
        System.out.println(new Segment(0, 0, 10, 10).hashCode() + " - " + (new Segment(0, 0, 10, 10).hashCode()) + "\t expected equals");
        System.out.println(new Segment(0, 0, 10, 10).hashCode() + " - " + (new Segment(10, 10, 0, 0).hashCode()) + "\t expected equals");
        System.out.println(new Segment(0, 0, 10, 10).hashCode() + " - " + (new Segment(2, 2, 10, 10).hashCode()) + "\t expected not equals");
        System.out.println(new Segment(10, 0, 10, 0).hashCode() + " - " + (new Segment(10, 0, 10, 0).hashCode()) + "\t expected equals");
        System.out.println(new Segment(0, 0, 10, 0).hashCode() + " - " + (new Segment(10, 0, 5, 0).hashCode()) + "\t expected not equals");

        System.out.println("\n======= Создание фигуры и преобразование её сторон в отрезки  =======");
        Figure figure1 = new Figure(new Point[]
                {new Point(0, 0),
                        new Point(1500, 0),
                        new Point(1500, 1000),
                        new Point(0, 1485)},
                new Point(500, 15));

        Segment[] segments2 = Figure.getSegmentsFromFigure(figure1);
        for (int i = 0; i < segments2.length; i++) {
            System.out.println(segments2[i]);
        }

        System.out.println("=== Проверка вхождения одного отрезка в другой отрезок===");
        Segment segment1 = new Segment(10, 100, 10, 10);
        Segment segment2 = new Segment(10, 102, 10, 10);
        System.out.println(segment1.segmentIncludeSegment(segment2));
        System.out.println(segment2.segmentIncludeSegment(segment1));
    }

    public static void main(String[] args) {
        //mainTests();
        Main main = new Main();
        int segmentCount = 0;
        int figureCount = 0;
        String inputFileName;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(args[0])))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Segment segment = Segment.segmentFromStr(line);
                if (segment != null) {
                    main.hashSegments.add(segment);
                    segmentCount++;
                    continue;
                }
                if (isNextFigure(line)) {
                    Figure figure = Figure.getFigureFromReader(bufferedReader);
                    if (figure != null) {
                        figureCount++;
                        Segment[] figureSegments = Figure.getSegmentsFromFigure(figure);
                        for (int i = 0; i < figureSegments.length; i++) {
                            main.hashSegments.add(figureSegments[i]);
                            segmentCount++;
                        }
                    }
                }
            }
            System.out.println("Результат разбора файла:\n\tСегментов добавлено: " + segmentCount +
                    "\n\tИз них сегментов фигур добавлено: " + (figureCount * 4)
                    + "\n\tПосле удаления дубликатов сегментов осталось: " + main.hashSegments.size());
        } catch (Exception e) {
            System.out.println("Ошибка разбора файла. Проверьте корректность файла.");
            System.exit(1);
        }

        main.hashSegments = Segment.deleteIncludeSegments(main.hashSegments);

        main.hashSegments.add(new Segment(0, 0, 0, 0));
        main.segments = new ArrayList<>(main.hashSegments);

        int i = 0;
        while (i < main.segments.size() - 1) {
            Point pointEnd = main.segments.get(i).point2;
            int closestSegmentIndex = i + 1;
            for (int j = i + 1; j < main.segments.size(); j++) {
                if (Point.getDistance(pointEnd, main.segments.get(j).point1) >
                        Point.getDistance(pointEnd, main.segments.get(j).point2)) {
                    main.segments.set(j, main.segments.get(j).reverseSegment());
                }
                if (Point.getDistance(pointEnd, main.segments.get(j).point1) <
                        Point.getDistance(pointEnd, main.segments.get(closestSegmentIndex).point1)) {
                    closestSegmentIndex = j;
                }
            }
            Segment segmentTemp = main.segments.get(i + 1);
            main.segments.set(i + 1, main.segments.get(closestSegmentIndex));
            main.segments.set(closestSegmentIndex, segmentTemp);
            i++;
        }
        main.segments.remove(0);
        System.out.println("\nПолученны следующие данные:");
        for (Segment segment : main.segments) System.out.println(segment.toString());

        String outputFileName = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("\nВведите путь для вывода данных: ");
            outputFileName = bufferedReader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(outputFileName)))) {
            for (Segment segment : main.segments) {
                bufferedWriter.write(segment.point1.coordinateX + ", " +
                        segment.point1.coordinateY + ", " +
                        segment.point2.coordinateX + ", " +
                        segment.point2.coordinateY + "\n");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("\n Программа успешно завершила работу");
    }


}
