package transportsType;

import order.Order;

public interface WorkingWithOrder {
    public String takeOrder(Order order);
    public String completeOrder(Order order);
    public String cancelVehicleForOrder(Order order);
}
