import directories.FuelType;
import directories.Cargo;
import order.CargoOrder;
import order.Order;
import order.PassengersOrder;
import route.Point;
import transportsType.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CarPark {
    private HashMap<Integer, Vehicle> transports = new HashMap<>();
    private HashMap<Integer, Order> parkOrders = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        CarPark myCarPark = new CarPark();

        System.out.println();
        // Создание новых точек маршрута
        Point base = new Point("База", 0, 0);
        Point nevada = new Point("Nevada", 80, 10);
        Point las = new Point("Los Angeles", 10, 20);
        Point chicago = new Point("Chicago", 50, 50);
        Point points[] = {base, nevada, las, chicago};
        System.out.println(String.format("Имеются следующие точки маршрута: %s, %s, %s, %s", base.getPointName(), nevada.getPointName(), las.getPointName(), chicago.getPointName()));
        System.out.println(" Расстояние равно " + Point.getDistance(base, las) + "км.");
        System.out.println();


        System.out.println("КОММЕНТАРИЙ: СОЗДАНИЕ И ДОБАВЛЕНИЕ В ПАРК НОВЫХ ТРАНСПОРТНЫХ СРЕДСТВ");
        // Создание и добавление автобуса (1)
        Vehicle vehicle = new Bus("Volvo", "R500", new GregorianCalendar(1998, 10, 1), FuelType.DIESEL, 17.6f, 450, 70);
        // Устанавливаем местоположене
        System.out.print(vehicle.setLocation(null, null));
        System.out.print(vehicle.setLocation(base, null));
        System.out.print(vehicle.setLocation(base, base));
        myCarPark.transports.put(vehicle.getNumberOfVehicle(), vehicle);

        // Создание и добавление грузопассажирского транспорта (2)
        vehicle.setLocation(base, base);
        vehicle = new CargoPassenger("MAN", "T100", new GregorianCalendar(2015, 8,
                1), FuelType.DIESEL, 20.4f, 610, 16, 8600);
        myCarPark.transports.put(vehicle.getNumberOfVehicle(), vehicle);

        // Создание тентованного грузовика (3)
        vehicle.setLocation(base, las);
        vehicle = new TentTruck("MAN", "superCargo", new GregorianCalendar(2000, 8,
                1), FuelType.GASOLINE, 12.4f, 210, 3400);
        myCarPark.transports.put(vehicle.getNumberOfVehicle(), vehicle);

        // Создание автоцистерны (4)
        vehicle.setLocation(chicago, chicago);
        vehicle = new TankTruck("MAN", "superTank", new GregorianCalendar(2017, 8,
                1), FuelType.DIESEL, 25.1f, 510, 12000);
        myCarPark.transports.put(vehicle.getNumberOfVehicle(), vehicle);

        // Создание автохолодильника (5)
        vehicle = new RefrigeratorTruck("Ford", "FrezyFord", new GregorianCalendar(2015,
                2, 1), FuelType.LPG, 25.1f, 510, 5000, -30);
        myCarPark.transports.put(vehicle.getNumberOfVehicle(), vehicle);

        // В парке содержится следующий транспорт:
        for (Map.Entry<Integer, Vehicle> entry : myCarPark.transports.entrySet()) {
            System.out.println(entry.getValue().toString());
        }
        System.out.println();


        System.out.println("КОММЕНТАРИЙ: СОЗДАНИЕ И ДОБАВЛЕНИЕ НОВЫХ ЗАКАЗОВ");
        // Создание нового заказа на перевозку 20 людей
        Order order = new PassengersOrder(base, nevada, Cargo.PASSENGERS, 40);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        order = new PassengersOrder(base, nevada, Cargo.PASSENGERS, 10);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        order = new PassengersOrder(base, nevada, Cargo.PASSENGERS, 5);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        order = new PassengersOrder(base, las, Cargo.PASSENGERS, 50);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        order = new CargoOrder(base, nevada, Cargo.LOOSE, 4000, 4500);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        order = new CargoOrder(base, nevada, Cargo.FROZEN_PRODUCTS, 2000, 1900);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        order = new CargoOrder(base, nevada, Cargo.FLUID, 7000, 6500);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        order = new CargoOrder(base, nevada, Cargo.GOODS, 3000, 1000);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        order = new CargoOrder(base, nevada, Cargo.PRODUCTS, 5000, 5000);
        myCarPark.parkOrders.put(order.getOrderNumber(), order);
        System.out.println();

        System.out.println("КОММЕНТАРИЙ. ПРОСТАЯ РАБОТА МЕТОДОВ ГРУЗОВИКОВ (БЕЗ ПРОВЕРОК)");
        System.out.println(myCarPark.transports.get(3).takeOrder(myCarPark.parkOrders.get(9)));
        System.out.println(myCarPark.transports.get(4).takeOrder(myCarPark.parkOrders.get(7)));
        System.out.println(myCarPark.transports.get(5).takeOrder(myCarPark.parkOrders.get(6)));
        System.out.println();

        System.out.println("КОММЕНТАРИЙ: ПЕРЕДАЧА ЗАКАЗОВ ТРАНСПОРТНЫМ СРЕДСТВАМ (ПАССАЖИРЫ)");
        System.out.printf("Попытка передачи заказа №%s на выполнение %s. (Превышено кол-во мест). Результат = %s",
                myCarPark.parkOrders.get(1).getOrderNumber(), myCarPark.transports.get(2).getShortName(),
                myCarPark.transports.get(2).takeOrder(myCarPark.parkOrders.get(1)));
        System.out.printf("Попытка передачи заказа №%s на выполнение %s. Результат = %s",
                myCarPark.parkOrders.get(2).getOrderNumber(), myCarPark.transports.get(2).getShortName(),
                myCarPark.transports.get(2).takeOrder(myCarPark.parkOrders.get(2)));
        System.out.printf("Попытка передачи заказа №%s на выполнение %s. Результат = %s",
                myCarPark.parkOrders.get(3).getOrderNumber(), myCarPark.transports.get(2).getShortName(),
                myCarPark.transports.get(2).takeOrder(myCarPark.parkOrders.get(3)));
        System.out.printf("Попытка передачи заказа №%s на выполнение %s. (Уже взятый заказ берет другое ТС). Результат = %s",
                myCarPark.parkOrders.get(2).getOrderNumber(), myCarPark.transports.get(1).getShortName(),
                myCarPark.transports.get(1).takeOrder(myCarPark.parkOrders.get(2)));
        System.out.printf("Попытка отмены заказа №%s на выполнение. Результат = %s",
                myCarPark.parkOrders.get(2).getOrderNumber(),
                myCarPark.parkOrders.get(2).getVehiclePerforms().cancelVehicleForOrder(myCarPark.parkOrders.get(2)));
        System.out.printf("Попытка передачи заказа №%s на выполнение %s. (Попытка взять после отмены заказа). Результат = %s",
                myCarPark.parkOrders.get(2).getOrderNumber(), myCarPark.transports.get(1).getShortName(),
                myCarPark.transports.get(1).takeOrder(myCarPark.parkOrders.get(2)));
        System.out.printf("Попытка передачи заказа №%s на выполнение %s. (Попытка взять после отмены заказа). Результат = %s",
                myCarPark.parkOrders.get(4).getOrderNumber(), myCarPark.transports.get(1).getShortName(),
                myCarPark.transports.get(1).takeOrder(myCarPark.parkOrders.get(4)));

        System.out.println();
        System.out.printf("Количество свободных мест в %s равно %s\n", myCarPark.transports.get(1).getShortName(), ((PassengerTransport) myCarPark.transports.get(1)).getFreeSeats());
        System.out.printf("Количество свободных мест в %s равно %s\n", myCarPark.transports.get(2).getShortName(), ((PassengerTransport) myCarPark.transports.get(2)).getFreeSeats());
        System.out.println("Просмотр заказов транспорта:\n" + myCarPark.transports.get(2).lookAllVehicleOrders());

        System.out.println("\n\nКОММЕНТАРИЙ: ИЗМЕНЕНИЕ МЕСТОПОЛОЖЕНИЯ ТРАНСПОРТА И РАБОТА С ЗАКАЗОМ");
        System.out.printf("Установка нового местоположения №%s (Присутствует нулевая точка). Результат = %s",
                myCarPark.transports.get(1).getShortName(), myCarPark.transports.get(1).setLocation(base, null));
        System.out.printf("Установка нового местоположения №%s (Присутствует другая нулевая точка). Результат = %s",
                myCarPark.transports.get(1).getShortName(), myCarPark.transports.get(1).setLocation(null, base));
        System.out.printf("Установка нового местоположения №%s (Обе точки нулевые). Результат = %s",
                myCarPark.transports.get(1).getShortName(), myCarPark.transports.get(1).setLocation(null, null));
        System.out.printf("Установка нового местоположения №%s (Корректные данные). Результат = %s",
                myCarPark.transports.get(1).getShortName(), myCarPark.transports.get(1).setLocation(base, nevada));
        System.out.printf("Попытка отмены заказа №%s на выполнение (Заказ уже выполняется). Результат = %s",
                myCarPark.parkOrders.get(2).getOrderNumber(),
                myCarPark.parkOrders.get(2).getVehiclePerforms().cancelVehicleForOrder(myCarPark.parkOrders.get(2)));
        System.out.printf("Установка нового местоположения №%s (Корректные данные). Результат = %s",
                myCarPark.transports.get(1).getShortName(), myCarPark.transports.get(1).setLocation(nevada, nevada));

        System.out.println();
        System.out.println("КОММЕНТАРИЙ: ОКОНЧАНИЕ РАБОТЫ С ЗАКАЗОМ");
        System.out.println("Просмотр заказов транспорта:\n" + myCarPark.transports.get(1).lookAllVehicleOrders());
        System.out.println("Просмотр заказов транспорта, готовых к завершению:\n" + myCarPark.transports.get(1).lookActiveOrdersForFinish());

        System.out.println("КОММЕНТАРИЙ: ВЫПОЛНЕНИЕ ЗАКАЗА");
        System.out.println("Попытка завершения заказа: " + myCarPark.transports.get(1).completeOrder(myCarPark.parkOrders.get(2)));
        System.out.println("Просмотр заказов транспорта:\n" + myCarPark.transports.get(1).lookAllVehicleOrders());
        System.out.println("Просмотр заказов транспорта, готовых к завершению:\n" + myCarPark.transports.get(1).lookActiveOrdersForFinish());
        System.out.printf("Установка нового местоположения №%s (Корректные данные). Результат = %s",
                myCarPark.transports.get(1).getShortName(), myCarPark.transports.get(1).setLocation(nevada, las));
        System.out.printf("Установка нового местоположения №%s (Корректные данные). Результат = %s",
                myCarPark.transports.get(1).getShortName(), myCarPark.transports.get(1).setLocation(las, las));
        System.out.println("Просмотр заказов транспорта:\n" + myCarPark.transports.get(1).lookAllVehicleOrders());
        System.out.println("Просмотр заказов транспорта, готовых к завершению:\n" + myCarPark.transports.get(1).lookActiveOrdersForFinish());
        System.out.printf("Количество свободных мест в %s равно %s\n", myCarPark.transports.get(1).getShortName(), ((PassengerTransport) myCarPark.transports.get(1)).getFreeSeats());
        System.out.println("Попытка завершения заказа: " + myCarPark.transports.get(1).completeOrder(myCarPark.parkOrders.get(4)));
        System.out.printf("Количество свободных мест в %s равно %s\n", myCarPark.transports.get(1).getShortName(), ((PassengerTransport) myCarPark.transports.get(1)).getFreeSeats());
        System.out.printf("Проведение дезинфекции: %s", ((PassengerTransport) myCarPark.transports.get(1)).disinfect());
        System.out.printf("Проведение уборки: %s", ((PassengerTransport) myCarPark.transports.get(1)).cleanUp());

        System.out.println();
        System.out.println();
        System.out.println("АВТОМАТИЗИРОВАННЫЙ АВТОБУСНЫЙ ПАРК. Работает автоматически в цикле.");
        System.out.print("Для запуска цикла наберите \"GO\": ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //String str = "go";
        String str = reader.readLine();
        if (str.equalsIgnoreCase("GO")) {
            while (true) {
                System.out.println("\n========== новый цикл ================\n\n\n");
                Thread.sleep(3000);
                System.out.println("Создаётся случайный транспорт");
                Thread.sleep(100);
                Vehicle randomBus = Bus.createRandomBus();
                System.out.println("Создан транспорт:\n" + randomBus);
                int point1number = new Random().nextInt(3);
                randomBus.setLocation(points[point1number], points[point1number]);
                System.out.println(randomBus.getShortName() + " " + randomBus.getLocation());

                System.out.println("Создаём случайный заказ №1 (используем уже готовые путевые точки)");
                Thread.sleep(100);
                point1number = new Random().nextInt(2);
                int point2number = 2 + new Random().nextInt(2);
                int randPass = new Random().nextInt(50);
                Order randomOrder1 = new PassengersOrder(points[point1number], points[point2number], Cargo.PASSENGERS, randPass);
                System.out.println(randomOrder1.toString());

                System.out.println("Создаём случайный заказ №2 (используем уже готовые путевые точки)");
                Thread.sleep(100);
                point1number = new Random().nextInt(2);
                point2number = 2 + new Random().nextInt(2);
                randPass = new Random().nextInt(5);
                Order randomOrder2 = new PassengersOrder(points[point1number], points[point2number], Cargo.PASSENGERS, randPass);
                System.out.println(randomOrder1.toString());

                System.out.println("Попытка взять заказ №1 транспортом: " + randomBus.getShortName());
                System.out.println(randomBus.takeOrder(randomOrder1));

                System.out.println("Попытка взять заказ №2 транспортом: " + randomBus.getShortName());
                System.out.println(randomBus.takeOrder(randomOrder2));

                System.out.println("Все заказы " + randomBus.getShortName() + " " + randomBus.lookAllVehicleOrders());
                if (randomBus.getVehicleOrders().size() == 0) {
                    System.out.println("НЕ УДАЛОСЬ ВЗЯТЬ НИКАКИХ ЗАКАЗОВ. НАЧИНАЕМ НОВЫЙ ЦИКЛ.");
                    continue;
                }
                System.out.println("Двигаемся в сторону выполнения заказа");
                System.out.println(randomBus.setLocation(randomOrder1.getOrderFrom(), randomOrder1.getOrderTo()));
                System.out.println("Пробуем завершить заказ в пути: " + randomBus.completeOrder(randomOrder1));
                System.out.println("Прибыли в точку назначения");
                System.out.println(randomBus.setLocation(randomOrder1.getOrderTo(), randomOrder1.getOrderTo()));

                System.out.println("Всего заказов: " + randomBus.lookAllVehicleOrders());
                System.out.println("Всего заказов доступных для завершения: " + randomBus.lookActiveOrdersForFinish());
                System.out.println("Завершаем заказ: " + randomBus.completeOrder(randomOrder1));
                System.out.println("Всего заказов: " + randomBus.lookAllVehicleOrders());
                System.out.println("Всего заказов доступных для завершения: " + randomBus.lookActiveOrdersForFinish());

                System.out.println("Проезжаем все точки, что бы избавиться от заказов. Завершаем всё, что есть");
                for (int i = 0; i < points.length; i++) {
                    System.out.println(randomBus.setLocation(points[i], points[i]));
                    System.out.println("Пробуем завершить заказы");
                    for (Map.Entry<Integer, Order> entry : randomBus.getVehicleOrders().entrySet()) {
                        System.out.println("Пытаемся завершить заказ: " + randomBus.completeOrder(entry.getValue()));
                    }
                }
                System.out.println("Всего заказов: " + randomBus.lookAllVehicleOrders());
            }
        }
    }
}
