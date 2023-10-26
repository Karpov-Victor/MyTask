package transportsType;

import directories.FuelType;
import directories.Cargo;
import order.Order;

import java.util.Calendar;

/**
 * Грузовик - обобщённый класс грузовика
 */
public class Truck extends Vehicle implements CargoTransport {
    protected int maxCargoWeight;

    public Truck(String brand, String model, Calendar manufactureDate, FuelType fuelType, float consumption, int fuelTankVolume, int maxCargoWeight) {
        super(brand, model, manufactureDate, fuelType, consumption, fuelTankVolume);
        this.maxCargoWeight = maxCargoWeight;
    }

    @Override
    public void selUp() {
        System.out.println("Пломбировка проведена");
    }

    @Override
    public void loading() {
        System.out.println("Поргузка проведена");
    }

    @Override
    public void unloading() {
        System.out.println("Выгрузка проведена");
    }

    @Override
    public int getFreePlace() {
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + " Грузоподъёмность = " + maxCargoWeight + "кг";
    }

}
