package transportsType;

import directories.FuelType;
import order.Order;
import order.OrderState;
import route.Point;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Транспорт - обобщенный класс транспотра. Является суперклассом для любого транспорта
 */
public class Vehicle implements Transport, WorkingWithOrder {

    private String brand;
    private String model;
    private Calendar manufactureDate;
    private FuelType fuelType;
    private float consumption;
    private int fuelTankVolume;

    private static int numberOfVehicleCount = 0;
    private int numberOfVehicle;

    /**
     * Местоположение автомобиля между двумя точками.
     * в тестовом задании на отрезке, соединяющем точки
     */
    protected Point[] location = new Point[2];

    /**
     * Список заказов автомобиля. Один автомобиль может иметь несколько разных заказов,
     * отличающихся местоназначением, типом груза и т.д.
     */
    private HashMap<Integer, Order> vehicleOrders = new HashMap<>();

    /**
     * сокращённая информация о транспорте
     *
     * @return
     */
    public String getShortName() {
        String type = "Тип не определён";
        if (this instanceof Bus) type = "Автобус";
        if (this instanceof CargoPassenger) type = "Грузо-пассажирский транспорт";
        if (this instanceof Truck) type = "Грузовик";
        if (this instanceof RefrigeratorTruck) type = "Грузовик с морозильной камерой";
        if (this instanceof TankTruck) type = "Автоцистерна";
        if (this instanceof TentTruck) type = "Тентованный грузовик";
        return "Транспорт № " + numberOfVehicle +
                " " + type;
    }

    public Vehicle(String brand, String model, Calendar manufactureDate, FuelType fuelType, float consumption, int fuelTankVolume) {
        this.brand = brand;
        this.model = model;
        this.manufactureDate = manufactureDate;
        this.fuelType = fuelType;
        this.consumption = consumption;
        this.fuelTankVolume = fuelTankVolume;
        numberOfVehicleCount++;
        this.numberOfVehicle = numberOfVehicleCount;
    }

    /**
     * Установить позицию транспорта между двумя точками (местами).
     * Если точки одинаковы, считается, что автотранспорт в этой точке
     *
     * @param point1
     * @param point2
     * @return
     */
    public String setLocation(Point point1, Point point2) {
        if (point1 == null || point2 == null) {
            return "ОШИБКА. Не удаётся установить местоположение транспорта №" + this.numberOfVehicle + ". Ни одно значение не может быть нулевым.\n";
        }
        location[0] = point1;
        location[1] = point2;
        if (vehicleOrders.size() > 0) {
            for (Map.Entry<Integer, Order> entry : vehicleOrders.entrySet()) {
                entry.getValue().setOrderState(OrderState.INPROGRESS);
            }
        }
        return "Для транспорта №" + this.numberOfVehicle + " установлено " + getLocation() + "\n";
    }

    /**
     * Получить информацию о местоположении транспорта
     *
     * @return
     */
    public String getLocation() {
        if (location[0] == null || location[1] == null) return "Местоположение не определено.";
        String result;
        if (location[0] == location[1]) {
            return "местонахождение: " + location[0].getPointName();
        } else {
            return "местонахождение между точками " + location[0].getPointName() + " и " + location[1].getPointName();
        }
    }

    /**
     * Добавить заказ в список заказа автомобиля
     *
     * @param order
     * @return
     */
    public boolean addOrder(Order order) {
        if (vehicleOrders.containsKey(order.getOrderNumber())) return false;
        vehicleOrders.put(order.getOrderNumber(), order);
        return true;
    }

    /**
     * Удалить заказ из транспортного средства
     *
     * @param order
     * @return
     */
    public boolean removeOrder(Order order) {
        return vehicleOrders.remove(order.getOrderNumber(), order);
    }

    public HashMap<Integer, Order> getVehicleOrders() {
        return vehicleOrders;
    }

    public static int getNumberOfVehicleCount() {
        return numberOfVehicleCount;
    }

    public int getNumberOfVehicle() {
        return numberOfVehicle;
    }

    public void setVehicleOrders(HashMap<Integer, Order> vehicleOrders) {
        this.vehicleOrders = vehicleOrders;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Calendar getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Calendar manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public float getConsumption() {
        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }

    public int getFuelTankVolume() {
        return fuelTankVolume;
    }

    public void setFuelTankVolume(int fuelTankVolume) {
        this.fuelTankVolume = fuelTankVolume;
    }

    @Override
    public void getFuel() {
        System.out.println("Заправка транспорта топливом проведена");
    }

    @Override
    public void repair() {
        System.out.println("Ремонт транспорта проведен");
    }

    @Override
    public String toString() {
        String type = "Тип не определён";
        if (this instanceof Bus) type = "Автобус";
        if (this instanceof CargoPassenger) type = "Грузо-пассажирский транспорт";
        if (this instanceof Truck) type = "Грузовик";
        if (this instanceof RefrigeratorTruck) type = "Грузовик с морозильной камерой";
        if (this instanceof TankTruck) type = "Автоцистерна";
        if (this instanceof TentTruck) type = "Тентованный грузовик";


//        return "Транспорт № " + numberOfVehicle +
//                " " + type + ". "
        return getShortName() + ", " + getLocation() + "\n" +
                " Марка = '" + brand + '\'' +
                ", Модель = '" + model + '\'' +
                ", Дата производства = " + manufactureDate.get(Calendar.YEAR) +
                ", Тип топлива = " + fuelType +
                ", расход топлива = " + String.format("%.2f", consumption) + "л. на 100км" +
                ", Объём бака = " + fuelTankVolume + "л.";
        //" Заказы =" + ordersMap;
    }


    @Override
    public String takeOrder(Order order) {
        if ((order == null) || (order.getVehiclePerforms() != null)) return null;
        order.setVehiclePerforms(this);
        return this.getShortName() + " взял заказ № " + order.toString();
    }

    /**
     * Завершить выполнение заказа.
     *
     * @param order
     * @return
     */
    @Override
    public String completeOrder(Order order) {
        if (order.getOrderTo() != this.location[1])
            return String.format("Заказ №%s не в пункте назначения. Завершение невозможно.\n", order.getOrderNumber());
        order.setOrderState(OrderState.COMPLETED);
        vehicleOrders.remove(order.getOrderNumber());
        if (this instanceof Bus) {
            ((Bus) this).refreshPassengersCount();
        }
        if (this instanceof CargoTransport) {
            ((CargoPassenger) this).refreshPassengersCount();
        }
        return String.format("Заказ №%s завершён\n", order.getOrderNumber());
    }


    /**
     * Отменить заказ
     *
     * @param order
     * @return
     */
    @Override
    public String cancelVehicleForOrder(Order order) {
        if (order.getOrderState() == OrderState.INPROGRESS)
            return String.format("Заказ №%s уже в работе. Отмена невозможна.\n", order.getOrderNumber());
        order.setOrderState(OrderState.READYTOGO);
        order.setVehiclePerforms(null);
        return String.format("Заказ №%s у %s успешно отменён.\n", order.getOrderNumber(), this.getShortName());
    }

    public void addOrderToOrdersMap(Order order) {
        vehicleOrders.put(order.getOrderNumber(), order);
    }

    /**
     * Просмотреть все текущие заказы транспорта
     *
     * @return
     */
    public String lookAllVehicleOrders() {
        if (this.vehicleOrders.size() == 0) return String.format("У %s нет активных заказов", this.getShortName());
        StringBuilder result = new StringBuilder(String.format("У %s есть следующие заказы:\n", this.getShortName()));
        for (Map.Entry<Integer, Order> entry : vehicleOrders.entrySet()) {
            result.append("\t" + entry.getValue().toString() + "\n");
        }
        return result.toString();
    }

    /**
     * Посмотреть все заказы транспорта, которые можно завершить в текущем местоположении транспорта
     *
     * @return
     */
    public String lookActiveOrdersForFinish() {
        if (this.vehicleOrders.size() == 0)
            return String.format("У %s нет активных заказов для завершения", this.getShortName());
        StringBuilder result = new StringBuilder(String.format("У %s для завершения есть следующие заказы:\n", this.getShortName()));
        boolean readyForFinish = false;
        for (Map.Entry<Integer, Order> entry : vehicleOrders.entrySet()) {
            if (entry.getValue().getOrderTo() == this.location[1]) {
                readyForFinish = true;
                result.append("\t" + entry.getValue().toString() + "\n");
            }
        }
        if (readyForFinish) {
            return result.toString();
        } else {
            return String.format("\tУ %s нет заказов для завершения в пункте назначения %s.\n", this.getShortName(), this.location[1].getPointName());
        }

    }


}

