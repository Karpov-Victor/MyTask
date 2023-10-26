package transportsType;

import directories.FuelType;
import order.Order;
import order.PassengersOrder;

import java.util.*;

/**
 * Автобус, может перевозить только пассажировг (груз c типом Passenger)
 */
public class Bus extends Vehicle implements PassengerTransport {
    private int maxPassengers;
    private int passengersCount;

    public Bus(String brand, String model, Calendar manufactureDate, FuelType fuelType, float consumption, int fuelTankVolume, int maxPassenger) {
        super(brand, model, manufactureDate, fuelType, consumption, fuelTankVolume);
        this.maxPassengers = maxPassenger;
    }

    /**
     * Создание автобуса со случайными параметрами
     *
     * @return
     */
    public static Bus createRandomBus() {
        int fuelTypeNumber = new Random().nextInt(3) + 1;
        FuelType fuelType = null;
        switch (fuelTypeNumber) {
            case 1:
                fuelType = FuelType.DIESEL;
                break;
            case 2:
                fuelType = FuelType.GASOLINE;
                break;
            case 3:
                fuelType = FuelType.LPG;
                break;
        }
        int fuelTankVolume = new Random().nextInt(41) + 50;
        int maxPassenger = new Random().nextInt(91) + 10;
        float consumption = (float) Math.random() * 10f;
        String brand = "RandomBrand" + (fuelTankVolume / 10 + 5);
        String model = "RandomModel" + (fuelTankVolume / 20 + 2);
        Calendar manufactureDate = new GregorianCalendar();
        Bus resultBus = new Bus(brand, model, manufactureDate, fuelType, consumption, fuelTankVolume, maxPassenger);

        return resultBus;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    @Override
    public String disinfect() {
        return String.format("Дезинфекция салона %s проведена\n", this.getShortName());
    }

    @Override
    public String cleanUp() {
        return String.format("Дезинфекция салона %s проведена\n", this.getShortName());
    }

    @Override
    public int getFreeSeats() {
        return maxPassengers - passengersCount;
    }

    @Override
    public String toString() {
        return super.toString() + "\n Всего мест = " + maxPassengers + " чел. " +
                "Занято мест: " + passengersCount + " чел.";
    }

    @Override
    public String takeOrder(Order order) {
        if ((order == null) || (order.getVehiclePerforms() != null))
            return "Ошибка. Нулевой заказ или заказ уже у другого транспорта.\n";
        if (order.getOrderFrom() != location[0]) return "Ошибка. Невозможно взять заказ, он в другом месте\n";
        if (!(order instanceof PassengersOrder)) return "Ошибка. Передача заказа непоходящему транспорту\n";
        PassengersOrder passengersOrder = (PassengersOrder) order;
        if (passengersOrder.getAmountPassengers() > this.maxPassengers - this.passengersCount) {
            return "Ошибка. Недостаточно свободных мест\n";
        }
        order.setVehiclePerforms(this);
        this.addOrderToOrdersMap(order);
        refreshPassengersCount();
        return " " + this.getShortName() + " успешно взял " + passengersOrder + "\n";
    }

    public void refreshPassengersCount() {
        HashMap<Integer, Order> vehicleOrders = super.getVehicleOrders();
        passengersCount = 0;
        for (Map.Entry<Integer, Order> entry : vehicleOrders.entrySet()) {
            passengersCount += ((PassengersOrder) entry.getValue()).getAmountPassengers();
        }
    }
}
