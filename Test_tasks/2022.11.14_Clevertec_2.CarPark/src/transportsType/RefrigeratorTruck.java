package transportsType;

import directories.FuelType;
import transportsType.Truck;

import java.util.Calendar;

public class RefrigeratorTruck extends Truck {

    private int temperature;

    public RefrigeratorTruck(String brand, String model, Calendar manufactureDate, FuelType fuelType, float consumption, int fuelTankVolume, int maxCargoWeight, int temperature) {
        super(brand, model, manufactureDate, fuelType, consumption, fuelTankVolume, maxCargoWeight);
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }
}
