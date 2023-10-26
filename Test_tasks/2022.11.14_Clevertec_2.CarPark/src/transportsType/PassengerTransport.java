package transportsType;

public interface PassengerTransport extends Transport{

    /**
     * Максимальное количество пассажиров
     */
    public int maxPassenger = 0;

    /**
     * Дезинфекция салона
     * @return
     */
    public String disinfect();

    /**
     * Чистка салона
     * @return
     */
    public String cleanUp();

    /**
     * Узнать кол-во свободных мест
     * @return
     */
    public int getFreeSeats();
}