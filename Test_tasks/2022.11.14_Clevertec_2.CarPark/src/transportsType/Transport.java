package transportsType;

/**
 * Общие для любого транспорта методы
 */
public interface Transport {

    /**
     * Залить топливо в бак
     */
    public void getFuel();

    /**
     * Ремонт транспорта
     */
    public void repair();
}
