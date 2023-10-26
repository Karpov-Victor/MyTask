package route;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Машрут следования.
 */

public class Itinerary {
    private LinkedList<Point> Itinerary = new LinkedList<>();

    public LinkedList<Point> getItinerary() {
        return Itinerary;
    }

    public void setItinerary(LinkedList<Point> itinerary) {
        Itinerary = itinerary;
    }
}
