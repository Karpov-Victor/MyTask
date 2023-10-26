import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

/**
 * Описываются отрезки для движения резца.
 * Резец по умолчанию поднят.
 */
public class Segment {

    /**
     * Начальная точка отрезка (движение от неё) в СКЛ
     */
    Point point1;

    /**
     * Конечная точка отрезка (движение к ней) в СКЛ
     */
    Point point2;

    public Segment(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Segment(int coordinatePoint1X, int coordinatePoint1Y, int coordinatePoint2X, int coordinatePoint2Y) {
        this.point1 = new Point(coordinatePoint1X, coordinatePoint1Y);
        this.point2 = new Point(coordinatePoint2X, coordinatePoint2Y);
    }

    /**
     * Преобразует строку вида "0, 15, 6000, 15" в отрезок
     *
     * @param inputLine
     * @return
     */
    public static Segment segmentFromStr(String inputLine) {
        try {
            int[] arr = new int[4];
            String[] strArr = inputLine.split(", ");
            Point point1 = new Point(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]));
            Point point2 = new Point(Integer.parseInt(strArr[2]), Integer.parseInt(strArr[3]));
            return new Segment(point1, point2);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Включает ли текущий отрезок в себя другой отрезок (полностью)
     *
     * @param segment
     * @return
     */
    public boolean segmentIncludeSegment(Segment segment) {
        int cx = this.point1.coordinateX;
        if ((cx == this.point2.coordinateX) && (cx == segment.point1.coordinateX) && (cx == segment.point2.coordinateX)) {
            int minY;
            int maxY;
            if (this.point1.coordinateY < this.point2.coordinateY) {
                minY = this.point1.coordinateY;
                maxY = this.point2.coordinateY;
            } else {
                minY = this.point2.coordinateY;
                maxY = this.point1.coordinateY;
            }
            if (((minY <= segment.point1.coordinateY) && (minY <= segment.point2.coordinateY)) &&
                    (maxY >= segment.point1.coordinateY) && (maxY >= segment.point2.coordinateY))
                return true;
        }
        int cy = this.point1.coordinateY;
        if ((cy == this.point2.coordinateY) && (cy == segment.point1.coordinateY) && (cy == segment.point2.coordinateY)) {
            int minY;
            int maxY;
            if (this.point1.coordinateX < this.point2.coordinateX) {
                minY = this.point1.coordinateX;
                maxY = this.point2.coordinateX;
            } else {
                minY = this.point2.coordinateX;
                maxY = this.point1.coordinateX;
            }
            if (((minY <= segment.point1.coordinateX) && (minY <= segment.point2.coordinateX)) &&
                    ((maxY >= segment.point1.coordinateX) && (maxY >= segment.point2.coordinateX)))
                return true;
        }
        return false;
    }

    /**
     * Удаляет отрезки, которые уже содержатся в других отрезках
     *
     * @return
     */
    public static HashSet<Segment> deleteIncludeSegments(HashSet<Segment> incomeHashSet) {
        HashSet<Segment> result = new HashSet<>();
        ArrayList<Segment> arrayList = new ArrayList<>(incomeHashSet);
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = i; j < arrayList.size(); j++) {
                if (arrayList.get(i) == arrayList.get(j)) continue;
                if (arrayList.get(i) == null || arrayList.get(j) == null) continue;
                if (arrayList.get(i).segmentIncludeSegment(arrayList.get(j))) {
                    arrayList.set(j, null);
                    continue;
                }
                if (arrayList.get(j).segmentIncludeSegment(arrayList.get(i))) {
                    arrayList.set(i, null);
                }
            }
        }
        for (Segment segment : arrayList) {
            if (segment != null) result.add(segment);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return (this.point1.equals(segment.point1) && this.point2.equals(segment.point2))
                || (this.point1.equals(segment.point2) && this.point2.equals(segment.point1));
    }


    @Override
    public int hashCode() {
        return Objects.hash(point1) + Objects.hash(point2);
    }

    @Override
    public String toString() {
        return "Segment{" +
                point1.coordinateX + ", " + point1.coordinateY +
                "; " + point2.coordinateX + ", " + point2.coordinateY +
                '}';
    }

    /**
     * Изменение направление отрезка
     */
    public Segment reverseSegment() {
        return new Segment(this.point2, this.point1);
    }

    /**
     * Определяет является ли отрезок точкой?
     * Когда координаты начала и конца совпадают
     *
     * @return
     */
    public boolean isSegmentAsPoint() {
        return this.point1.equals(point2);
    }


}
