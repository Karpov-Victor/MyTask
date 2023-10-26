package root.billException;

/**
 * исключения, которые могут возникнуть при работе с чеком
 */
public class ParsingException extends Exception{


    /**
     * Код ошибки:
     * 1 - Неизвестная ошибка
     * 2 - Отсутствует данные для разбора строки.
     * 3 - Разбор строки прошел успешно, но нет товаров для добавления в чек.
     * 4 - Отсутствует товар в справочнике товаров
     * 5 - Ошибка при разборе файла с входными данными
     * 6 - Ошибка при разборе входных данных
     * 7 - Ошибка при обращении к файлу
     * 8 - Не указан файл для вывода
     */
    int errorCode = 0;
    public int getErrorCode() {return errorCode;}

    public ParsingException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
