package order;

import directories.Cargo;
import route.Point;
import transportsType.Vehicle;

public class Order {
    protected Point orderFrom;
    protected Point orderTo;
    protected Cargo cargo;
    private static int orderNumberCount = 0;
    protected int orderNumber;
    protected OrderState orderState;

    /**
     * Какой транспорт выполняет заказ
     */
    protected Vehicle vehiclePerforms = null;


    public Order(Point orderFrom, Point orderTo, Cargo cargo) {
        this.orderFrom = orderFrom;
        this.orderTo = orderTo;
        this.cargo = cargo;
        this.orderState = OrderState.READYTOGO;
        orderNumberCount++;
        orderNumber = orderNumberCount;
        System.out.print(String.format("Создан новый заказ №%s. Пункт отправления: %s, пункт назначения: %s. " +
                "Тип груза: %s", orderNumber, orderFrom.getPointName(), orderTo.getPointName(), cargo.toString()));
    }

    public Vehicle getVehiclePerforms() {
        return vehiclePerforms;
    }

    public void setVehiclePerforms(Vehicle vehiclePerforms) {
        this.vehiclePerforms = vehiclePerforms;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    /**
     * Изменение состояния заказа
     *
     * @param orderState
     * @return
     */
    public String setOrderState(OrderState orderState) {
        String result = "Измененение состояния: " + this.toString();
        this.orderState = orderState;
        switch (orderState) {
            case READYTOGO:
                result = result + " готов к исполнению";
                return result;
            case INPROGRESS:
                result = result + " исполняется";
                return result;
            case COMPLETED:
                result = result + " завершён";
                return result;
        }
        return result;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public Cargo getTypeOfCargo() {
        return cargo;
    }

    public static int getOrderNumberCount() {
        return orderNumberCount;
    }

    public Point getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(Point orderFrom) {
        this.orderFrom = orderFrom;
    }

    public Point getOrderTo() {
        return orderTo;
    }

    public void setOrderTo(Point orderTo) {
        this.orderTo = orderTo;
    }

    @Override
    public String toString() {
        return "Номер заказа=" + orderNumber +
                ", Состояние заказа = " + orderState +
                "Пункт отправления = " + orderFrom +
                ", Пункт назначения = " + orderTo +
                ", Тип груза = " + cargo
                ;
    }

    /**
     * Вывести состояние заказа на экран
     */
    public void printOrder() {
        System.out.println(this.toString());
    }
}
