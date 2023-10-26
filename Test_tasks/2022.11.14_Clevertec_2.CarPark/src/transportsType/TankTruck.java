package transportsType;

import directories.FuelType;

import java.util.Calendar;

/**
 * Автоцистерна - частный случай грузовика
 */
public class TankTruck extends Truck {
    public TankTruck(String brand, String model, Calendar manufactureDate, FuelType fuelType, float consumption, int fuelTankVolume, int maxCargoWeight) {
        super(brand, model, manufactureDate, fuelType, consumption, fuelTankVolume, maxCargoWeight);
    }

}
