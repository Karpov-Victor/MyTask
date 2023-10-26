package root;

import root.billException.ParsingException;
import root.documents.Bill;
import root.documents.BillUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<Integer, Goods> goodsDirectory;

    public static void main(String[] args) throws ParsingException {
        Bill bill = Bill.createNewBill("$", "Petrova O. P");
        File file = BillUtils.getOutFileFromStingArr(args);
        goodsDirectory = Test.createTestGoodsDirectory();
        bill.setBillHead("CASH RECEIPT");
        bill.setAddress(new String[] {"\"BestShop\"", "Shop N3", "Gomel"});

        for (int i = 0; i < bill.getBillPositions().size(); i++) {
            bill.getBillPositions().get(i).refreshPrice(goodsDirectory);
            bill.getBillPositions().get(i).refreshDiscount(5, 10);
        }
        bill.deleteEmptyPositionBill();
        System.out.println(bill.printToFile(file, goodsDirectory));
    }
}
