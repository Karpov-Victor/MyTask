package transportsType;

import directories.FuelType;
import order.CargoOrder;
import order.Order;
import order.OrderState;
import order.PassengersOrder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Грузо-пассажирский транспорт. Может перевозить пассажиров и грузы
 */
public class CargoPassenger extends Vehicle implements CargoTransport, PassengerTransport {
    private int maxPassengers;
    private int passengersCount;
    private int maxCargoWeight;

    public CargoPassenger(String brand, String model, Calendar manufactureDate, FuelType fuelType, float consumption, int fuelTankVolume, int maxPassengers, int maxCargoWeight) {
        super(brand, model, manufactureDate, fuelType, consumption, fuelTankVolume);
        this.maxPassengers = maxPassengers;
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
        if (order instanceof PassengersOrder) {
            PassengersOrder passengersOrder = (PassengersOrder) order;
            if (passengersOrder.getAmountPassengers() > this.maxPassengers - this.passengersCount) {
                return "Ошибка. Недостаточно свободных мест\n";
            }
            order.setVehiclePerforms(this);
            this.addOrderToOrdersMap(order);
            refreshPassengersCount();
            return " " + this.getShortName() + " успешно взял " + passengersOrder + "\n";
        }
        return "Ошибка. Заказ не обработан";
    }

    public void refreshPassengersCount() {
        HashMap<Integer, Order> vehicleOrders = super.getVehicleOrders();
        passengersCount = 0;
        for (Map.Entry<Integer, Order> entry : vehicleOrders.entrySet()) {
            passengersCount += ((PassengersOrder) entry.getValue()).getAmountPassengers();
        }
    }


}
