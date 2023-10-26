package root.documents;

import root.*;

import java.util.Map;

/**
 * Товарная позиция в чеке. Определяет код товара, его кол-во и цену за единицу.
 */
public class BillPosition {

    /**
     * Код товара
     */
    private int goodsID;

    /**
     * Количество товара
     */
    private int count;

    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    /**
     * Цена товара за единицу товара
     */
    private double price;

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Общая сумма по товарной позиции
     */
    private double positionSumm;
    public double getPositionSumm() {
        positionSumm = count * price;
        return positionSumm;
    }

    public void setPositionSumm(double positionSumm) {
        this.positionSumm = positionSumm;
    }

    /**
    * Сумма скидки по товарной позиции
    */
    private double discountSumm;

    public void setDiscountSumm(double discountSumm) {
        this.discountSumm = discountSumm;
    }
    public double getDiscountSumm() {
        return discountSumm;
    }


    public double getPrice() {
        return price;
    }





    public BillPosition(int goodsId, int count) {
        this.goodsID = goodsId;
        this.count = count;
    }

    public int getGoodsID() {
        return goodsID;
    }

    @Override
    public String toString() {
        return "BillPosition{" +
                "id=" + goodsID +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

    public void refreshPrice(Map<Integer, Goods> goodsDirectory) {
        this.price = goodsDirectory.get(this.getGoodsID()).getPrice();
    }

    /**
     * Скидка на товар.
     * @param count
     * @param discountPercent
     */
    public void refreshDiscount(int count, int discountPercent) {
        if (this.getCount() >= count) {
            if (discountPercent >= 100) {
                this.setDiscountSumm(0);
                return;
            }
            this.setDiscountSumm((this.getPositionSumm() / 100) * discountPercent);
        }
    }

}
