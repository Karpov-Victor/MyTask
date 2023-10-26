package root;

import root.billException.ParsingException;
import root.documents.Bill;
import root.documents.BillUtils;
import root.documents.Document;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Test {

    private static Map<Integer, Goods> testGoodsDirectory;

    public static Map<Integer, Goods> createTestGoodsDirectory(){
        // Создаем справочник товаров
        Map<Integer, Goods> testGoodsDirectory = new HashMap<>();
        testGoodsDirectory = new HashMap<>();
        testGoodsDirectory.put(1001, new Goods(1001, "list", 1.01));
        testGoodsDirectory.put(1002, new Goods(1002, "Directory for paper", 2));
        testGoodsDirectory.put(1003, new Goods(1003, "Pen (red color)",0.5));
        testGoodsDirectory.put(1004, new Goods(1004, "Scissors",4));
        testGoodsDirectory.put(1005, new Goods(1005, "Hole puncher", 5));
        Test.testGoodsDirectory = testGoodsDirectory;
        return Test.testGoodsDirectory;
    }

    private static Bill newTestBillCreate(){
        Bill bill = Bill.createNewBill("$", "Petrova O. P.");


        bill.setBillHead("CASH RECEIPT");
        bill.setAddress(new String[] {"\"BestShop\"", "Shop N3", "Gomel"});
        return bill;
    }

    public static void main(String[] args) {

        System.out.println("=== Проверка является ли входная строка разбора входных данных с товарной позицией. Ожидается возврат товарной позиции ===");
        System.out.println(BillUtils.parseInputGoodData("123-5"));
        System.out.println();

        System.out.println("=== Проверка разбора входных данных с товарной позицией. Ожидается null ===");
        System.out.println(BillUtils.parseInputGoodData("1b23-5"));
        System.out.println();

        System.out.println("=== Проверка разбора входных данных с товарной позицией. Ожидается null ===");
        System.out.println(BillUtils.parseInputGoodData("123-"));
        System.out.println();

        System.out.println("=== Проверка разбора входных данных с скидочной картой. Ожидается возврат дисконтной карты ===");
        System.out.println(BillUtils.parseInputDiscountCardData("card-5312344"));
        System.out.println();

        System.out.println("=== Проверка разбора входных данных с скидочной картой. Ожидается возврат null ===");
        System.out.println(BillUtils.parseInputDiscountCardData("card-"));
        System.out.println();


        System.out.println("=== 1) Запуск успешного теста печати чека без карты лояльности. Ожидается true ===");
        try {
            Test.mainTest(new String[] {"1001-3","1002-2", "-outFile=d:\\Test1.txt"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 2) Запуск успешного теста печати чека с картой лояльности. Ожидается true и печать информации о скидочной карте ===");
        try {
            Test.mainTest(new String[] {"1001-3", "1002-2", "-outFile=d:\\Test2.txt"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 3) Печать чека. Ошибка разбора строки. Нет данных для разбора. Ожидается исключение ===");
        try {
            Test.mainTest(new String[] {"-outFile=d:\\Test3.txt"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 4) Печать чека. Ошибка разбора строки. Нет товара в справочнике товаров. Ожидается исключение ===");
        try {
            Test.mainTest(new String[] {"1-3", "5-2", "-outFile=d:\\Test4.txt"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 5) Запуск успешного теста печати чека по указанному пути. Ожидается true ===");
        try {
            Test.mainTest(new String[] {"1001-3", "1002-2", "card-1233", "-outFile=d:\\Test5.txt"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 6) Запуск теста печати чека по указанному пути (путь уже есть и это папка). Ожидается false ===");
        try {
            new File("d:\\Test6.txt").mkdir();
            Test.mainTest(new String[] {"1001-3", "1002-2", "card-1233", "-outFile=d:\\Test6.txt"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 7) Обработка данных из файла ===");
        try {
            Test.mainTest(new String[] {"-outFile=d:\\Test7.txt", "1001-3", "1002-2", "-inFile=d:\\inputdata.txt"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 8) Обработка данных из файла (рекурсивный обход) ===");
        try {
            Test.mainTest(new String[] {"-outFile=d:\\Test8.txt", "1001-3", "1002-2", "-inFile=d:\\inputdata.txt"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 9) Запуск успешного теста печати чека без карты лояльности, но со скидкой. " +
                "Ожидается true и скидка на товар ===");
        try {
            Test.mainTest(new String[] {"-outFile=d:\\Test9.txt", "1001-1", "1002-3", "1002-2"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 10) Обработка данных из файла (нулевые товары) ===");
        try {
            Test.mainTest(new String[] {"-outFile=d:\\Test10.txt", "1001-3", "1002-0", "1002-0"});
        } catch (ParsingException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            System.out.println(e.getMessage());
        }
        System.out.println();

    }

    /**
     * Тест успешного формирования чека
     * @param args
     * @throws ParsingException
     */
    public static void mainTest(String[] args) throws ParsingException {
        Bill bill = Test.newTestBillCreate();
        createTestGoodsDirectory();
        File file = BillUtils.getOutFileFromStingArr(args);
        BillUtils.parseBillPositionString(bill, args, Test.testGoodsDirectory);

        // Обновляем цены и скидки на позиции в чеке
        for (int i = 0; i < bill.getBillPositions().size(); i++) {
            bill.getBillPositions().get(i).refreshPrice(Test.testGoodsDirectory);
            bill.getBillPositions().get(i).refreshDiscount(5, 10);
        }
        bill.deleteEmptyPositionBill();
        System.out.println(bill.printToFile(file, Test.testGoodsDirectory));
    }



}
