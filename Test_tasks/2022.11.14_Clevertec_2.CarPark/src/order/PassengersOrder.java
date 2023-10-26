package order;

import directories.Cargo;
import route.Point;

public class PassengersOrder extends Order {
    private int amountPassengers;


    public PassengersOrder(Point orderFrom, Point orderTo, Cargo cargo, int amountPassengers) {
        super(orderFrom, orderTo, cargo);
        this.amountPassengers = amountPassengers;
        System.out.println(". Кол-во человек для перевозки: " + amountPassengers);
    }


    public int getAmountPassengers() {
        return amountPassengers;
    }

    public void setAmountPassengers(int amountPassengers) {
        this.amountPassengers = amountPassengers;
    }

    @Override
    public String toString() {
        return "Заказ № " + orderNumber +

                " из " + orderFrom.getPointName() +
                ", в " + orderTo.getPointName() +
                " на " + amountPassengers + " чел."
                ;
    }
}
