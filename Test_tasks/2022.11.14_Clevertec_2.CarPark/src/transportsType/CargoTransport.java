package transportsType;

public interface CargoTransport extends Transport {
    public int maxCargoWeight = 0;

    /**
     * пломбровка
     */

    public void selUp();

    /**
     * Загрузка
     */
    public void loading();

    /**
     * Выгрузка
     */
    public void unloading();

    /**
     * Узнать свободное место
     */
    public int getFreePlace();
}
