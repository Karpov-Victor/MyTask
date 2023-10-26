package root.documents;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import root.*;
import root.billException.ParsingException;

public class Bill extends Document implements PrintToFile{

    private Bill() {
        super();
    }

    private Bill(int id) {
        super(id);
    }

    public static Bill createNewBill(String currencyTag, String cashier) {
        Bill bill = new Bill();
        bill.billPositions = new ArrayList<>();
        bill.date = new GregorianCalendar();
        bill.currencyTag = currencyTag;
        bill.cashier = cashier;
        return bill;
    }

    /**
     * Валюта расчёта
     */
    protected String currencyTag;

    public String getCurrencyTag() {
        return currencyTag;
    }

    public void setCurrencyTag(String currencyTag) {
        this.currencyTag = currencyTag;
    }


    /**
     * Список товарных позиций в чеке
     */
    private ArrayList<BillPosition> billPositions;


    /**
     * Скидочная карта в чеке. При парсинге добавляется последняя обработанная.
     */
    private DiscountCard discountCard = null;

    /**
     * Заголовок чека
     */
    private String billHead;


    /**
     * Адрес торгового объекта
     */
    private String[] address;

    /**
     * или id кассира
     */
    private String cashier;

    /**
     * Дата и время создания чека
     */
    private Calendar date;

    /**
     * Ширина чека. По умолчанию - 80 символов
     */
    private static int width = 80;

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Bill.width = width;
    }

    public ArrayList<BillPosition> getBillPositions() {
        return billPositions;
    }

    public void setBillPositions(ArrayList<BillPosition> billPositions) {
        this.billPositions = billPositions;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    public String getBillHead() {
        return billHead;
    }

    public void setBillHead(String billHead) {this.billHead = billHead;}

    public String[] getAddress() {
        return address;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public Calendar getDate() {
        return date;
    }

    public void printBillPositions() {
        System.out.println("Товарные позиции в чеке №" + this.id);
        for (int i = 0; i < this.billPositions.size(); i++) {
            System.out.println(this.billPositions.get(i).toString());
        }
    }

    protected static String createBillPositionString(BillPosition billPosition, String goodsName, String currencyTag) {
        int width = Bill.getWidth();
        int qty = billPosition.getCount();
        double price = billPosition.getPrice();
        double positionSumm = billPosition.getPositionSumm();

        String qtyStr = Integer.toString(qty);
        DecimalFormat dF = new DecimalFormat("0.00");
        String priceStr = currencyTag + dF.format(price);
        String priceTotalStr = currencyTag + dF.format(positionSumm);

        String name = BillUtils.getStrWidth(goodsName, width - qtyStr.length() - priceStr.length() - priceTotalStr.length()-4);

        return String.format("%s %s %s  %s", qty, name, priceStr, priceTotalStr);
    }

    public double getTotalBillSumm() {
        double result = 0;
        for (BillPosition billPosition : this.getBillPositions()) {
            result += billPosition.getPositionSumm();
        }
        return result;
    }

    public double getTotalDiscountSumm() {
        double result = 0;
        for (BillPosition billPosition : this.getBillPositions()) {
            result += billPosition.getDiscountSumm();
        }
        return result;
    }

    /**
     * Удаляет пустые товарные позиции из чека
     * @return
     */
    public boolean deleteEmptyPositionBill() {
        boolean result = false;
        ListIterator<BillPosition> iterator = this.billPositions.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getCount() == 0) {
                iterator.remove();
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean printToFile(File file, Map<Integer, Goods> goodsDirectory) throws ParsingException {
        if (file == null) throw new ParsingException(7, "Не указан файл для вывода");
        return BillUtils.printBillToFile(this, file, goodsDirectory);
    }


}
