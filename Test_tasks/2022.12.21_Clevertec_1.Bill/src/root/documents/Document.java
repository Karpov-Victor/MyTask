package root.documents;

/**
 * Абстрактный класс, описывающий некий документ: продажи, возврата и т.д.
 */
public abstract class Document{

    /**
     * Счетчик созданных документов
     */
    protected static int idCounter = 0;

    /** Уникальный номер документа
     *
     */
    protected int id;

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Document() {
        idCounter++;
        this.id = idCounter;
    }

    protected Document(int id) {
        idCounter++;
        this.id = id;
    }
}
