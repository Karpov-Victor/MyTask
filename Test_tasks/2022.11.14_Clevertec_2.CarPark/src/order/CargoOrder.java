package order;

import directories.Cargo;
import route.Point;

public class CargoOrder extends Order {
    private int volume;
    private int weight;

    public CargoOrder(Point orderFrom, Point orderTo, Cargo cargo, int volume, int weight) {
        super(orderFrom, orderTo, cargo);
        this.volume = volume;
        this.weight = weight;
        System.out.println(" Объём груза: " + volume + " м3. Масса груза: " + weight + " кг.");
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Заказ № " + orderNumber +

                " из " + orderFrom.getPointName() +
                ", в " + orderTo.getPointName() +
                " на перевозку груза " + this.cargo +
                " (" + weight + "кг, " +
                volume + "м3)"
                ;
    }
}
