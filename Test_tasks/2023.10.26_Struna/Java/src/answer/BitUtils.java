package answer;

/**
 * Утилитный класс для работы с битами.
 * Выделение бита, разложения числа в массив битов и т.д.
 */
public class BitUtils {

    /**
     * Возвращает численное значение бита (0 или 1)
     * @param value значение
     * @param bit номер бита
     * @return
     */
    public static int getBitIntValue(int value, int bit) {
        return ((value & (1 << bit)) >> bit);
    }

    /**
     * Возвращает булевое значение бита (false если 0 или true если 1)
     * @param value значение
     * @param bit номер бита
     * @return
     */
    public static boolean getBitBooleanValue(int value, int bit) {
        if (getBitIntValue(value, bit) == 1) return true;
        return false;
    }

}
