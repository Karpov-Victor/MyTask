package transportsType;

import directories.FuelType;

import java.util.Calendar;
import java.util.jar.JarOutputStream;

/**
 * Тентованный грузовик - частный случай грузовика
 */
public class TentTruck extends Truck {
    public TentTruck(String brand, String model, Calendar manufactureDate, FuelType fuelType, float consumption, int fuelTankVolume, int maxCargoWeight) {
        super(brand, model, manufactureDate, fuelType, consumption, fuelTankVolume, maxCargoWeight);
    }


}
