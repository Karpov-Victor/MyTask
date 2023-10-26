package root;

import java.util.Objects;

public class Goods {

    /**
     * Уникальный код товара. Равенство товаров обеспечивается только кодом товара.
     */
    private int goodsID;

    /**
     * Название товара. Товары с одним кодом, но разными товарами всё равно считаются одинаковыми
     */
    private String goodsName;

    /**
     * Временная переменная!
     * Стоимость товара.
     * В будущем стоимость товара должна браться из прихода товара (накладной)
     */
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Goods(int goodID) {
        this.goodsID = goodID;
    }

    public Goods(int goodsID, String goodsName) {
        this.goodsID = goodsID;
        this.goodsName = goodsName;
    }

    public Goods(int goodsID, String goodsName, double price) {
        this.goodsID = goodsID;
        this.goodsName = goodsName;
        this.price = price;
    }

    public int getGoodsID() {
        return goodsID;
    }

    public String getGoodsName() {return goodsName;}

    public void setGoodsName(String goodsName) {this.goodsName = goodsName;}

    @Override
    public String toString() {
        return "Goods{" +
                "goodsID=" + goodsID +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return goodsID == goods.goodsID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsID);
    }
}
