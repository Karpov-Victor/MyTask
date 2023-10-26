package root;

public class DiscountCard {

    /**
     * Номер карты лояльности
     */
    private String cardNumber;

    /**
     * Данные о владельце карты
     */
    private String ownerCardName;

    public DiscountCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getOwnerCardName() {
        return ownerCardName;
    }

    public void setOwnerCardName(String ownerCardName) {
        this.ownerCardName = ownerCardName;
    }

    @Override
    public String toString() {
        if (ownerCardName != null) {
            return cardNumber +" " + ownerCardName;
        }
        return cardNumber;
    }
}
