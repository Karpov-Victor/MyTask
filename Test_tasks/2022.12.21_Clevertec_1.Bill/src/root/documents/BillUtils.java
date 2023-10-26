package root.documents;

import root.billException.ParsingException;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import root.*;


public class BillUtils {
    /**
     * Парсинг входной строки с данными о товаре
     * @param inputStr
     * @return
     */
    public static BillPosition parseInputGoodData(String inputStr) {
        if (isDataGoods(inputStr)){
            BillPosition result = null;
            String[] data = inputStr.split("-");
            result = new BillPosition(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
            return result;
        } else {
            return null;
        }
    }

    /**
     * Парсинг входной строки о скидочной карте
     * @param inputStr
     * @return
     */
    public static DiscountCard parseInputDiscountCardData(String inputStr) {
        if (isDataDiscountCard(inputStr)) {
            String[] data = inputStr.split("-");
            return new DiscountCard(data[1]);
        } else {
            return null;
        }
    }

     /**
     * Являются ли входная строка данными о скидочной карте?
     * @param inputStr
     * @return
     */
    public static boolean isDataDiscountCard(String inputStr) {
        if (inputStr.matches("card-\\d+")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Являются ли строка данными о товаре?
     * @param inputStr
     * @return
     */
    public static boolean isDataGoods(String inputStr) {
        if (inputStr.matches("\\d+-\\d+")) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDate(Calendar calendar) {
        String result = String.format("DATE: %s/%s/%s", calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
        return result;
    }

    public static String getTime(Calendar calendar) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return "TIME: " + timeFormat.format(calendar.getTime());
    }

    public static void printBillToFile(Bill bill, String fileName, Map<Integer, Goods> goodsDirectory) {
        File file = new File(fileName);
        printBillToFile(bill, file, goodsDirectory);
    }

    /**
     * Возвращает строку, состоящую из переданного символа в ширину чека
     * @return
     */
    public static String getWidthLine(char ch) {
        StringBuilder result = new StringBuilder(Bill.getWidth());
        for (int i = 0; i < Bill.getWidth(); i++) {
            result.append(ch);
        }
        return result.toString();
    }

    /**
     * Добавляет в строку пробелы, или обрезает с конца до указанной ширины.
     * @param str
     * @param width
     * @return
     */
    public static String getStrWidth(String str, int width) {
        if (str.length() == width) return str;
        StringBuilder result = new StringBuilder(str);
        if (str.length() < width) {
            for (int i = str.length(); i < width; i++) {
                result.append(' ');
            }
        } else {
            result.delete(width, str.length());
        }
        return result.toString();
    }

    public static boolean printBillToFile(Bill bill, File file, Map<Integer, Goods> goodsDirectory) {
        String currencyTag = bill.getCurrencyTag();
        DecimalFormat dF = new DecimalFormat("0.00");
        try (FileWriter fileWriter = new FileWriter(file)){

            // Печать шапки чека
            fileWriter.write("\t\t" + bill.getBillHead() + "\n");

            // Печать адреса
            for (int i = 0; i < bill.getAddress().length; i++) {
                fileWriter.write("\t\t" + bill.getAddress()[i] + "\n");
            }
            fileWriter.write("\n");

            // Печать кассира и даты
            String cashier = "CASHIER: " + bill.getCashier();
            cashier = BillUtils.getStrWidth(cashier, Bill.getWidth() - 16);
            fileWriter.write(cashier + getDate(bill.getDate()) + "\n");
            fileWriter.write(BillUtils.getStrWidth(" ", Bill.getWidth() - getTime(bill.getDate()).length())
                    + getTime(bill.getDate()) + "\n");
            fileWriter.write("\n");

            // Печать заголовков
            fileWriter.write("QTY    DESCRIPTION                                                 PRICE   TOTAL" + "\n");
            fileWriter.write(BillUtils.getWidthLine('_') + "\n");

            // Печать твоарных позиций и скидок по ним
            for (BillPosition billPosition : bill.getBillPositions()) {
                String goodsName = goodsDirectory.get(billPosition.getGoodsID()).getGoodsName();
                fileWriter.write(Bill.createBillPositionString(billPosition, goodsName, "$") + "\n");
                if (billPosition.getDiscountSumm() != 0) {
                    String discountSumm = "-" + currencyTag + dF.format(billPosition.getDiscountSumm());
                    fileWriter.write(BillUtils.getStrWidth("                                       Discount summ: ",
                            Bill.getWidth() - discountSumm.length()) + discountSumm + "\n");
                }
            }
            fileWriter.write(BillUtils.getWidthLine('=') + "\n");
            double billSummDouble = bill.getTotalBillSumm();
            String billSummStr = currencyTag + dF.format(billSummDouble);
            fileWriter.write(BillUtils.getStrWidth("Bill summ: ",Bill.getWidth() - billSummStr.length())
                    + billSummStr + "\n");

            double billDiscountSummDouble = bill.getTotalDiscountSumm();
            if (bill.getTotalDiscountSumm() != 0) {

                String billDiscountSummStr = "-" + currencyTag + dF.format(billDiscountSummDouble);
                fileWriter.write(BillUtils.getStrWidth("Bill discount: ",Bill.getWidth()
                        - billDiscountSummStr.length()) + billDiscountSummStr + "\n");
            }

            double billFinishSummDouble = billSummDouble - billDiscountSummDouble;
            String billFinishSummStr = currencyTag + dF.format(billFinishSummDouble);
            fileWriter.write(BillUtils.getStrWidth("BILL TOTAL SUMM: ",Bill.getWidth()
                    - billFinishSummStr.length()) + billFinishSummStr + "\n");
            fileWriter.write(BillUtils.getWidthLine('-') + "\n");
            if (bill.getDiscountCard() != null) {
                fileWriter.write((BillUtils.getStrWidth("Discount card:" + bill.getDiscountCard(), Bill.getWidth())));
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //TODO Удалить, при необходимости
    /**
     * Представлен ли товар в справочнике товаров?
     * @param goodsID
     * @param set
     * @return
     */
    public static boolean isGoodsPresent(int goodsID, Set<Goods> set) {
        boolean result = false;
        for (Goods goods : set) {
            if (goods.getGoodsID() == goodsID) return true;
        }
        return false;
    }

    /**
     * Добавляет все товарные позиции в чек.
     * Конструкция для парсинга: "код_товара"-"кол-во_товара".
     * Пример: 15-2 (код товара 15, кол-во - 2 единицы).
     * Товары с одинаковым кодом складываются
     * Добавляет скидочную карту в чек. Если карт несколько, то добавляется последняя.
     * @param bill
     */
    public static void parseBillPositionString(Bill bill, String[] inputArr, Map<Integer, Goods> goodsDirectory) throws ParsingException{

        //TODO вынести переменную в ini-файл
        final String fileNametag = "-inFile=";

        if (inputArr.length == 0 || inputArr == null) {
            throw new ParsingException(1, "Parsing error. Parsing list is empty");
        }
        ArrayList<BillPosition> newBillPositionList = new ArrayList<>();

        for (int i = 0; i < inputArr.length; i++) {
            if (inputArr[i].startsWith(fileNametag)) {
                String fileName=inputArr[i].substring(fileNametag.length());
                File file = new File(fileName);
                if (!file.exists() || !file.isDirectory()) {
                    String[] strings = readDataFromFile(file);
                    parseBillPositionString(bill, strings, goodsDirectory);
                }

            }
        }


        DiscountCard newDiscountCard = null;

        for (int i = 0; i < inputArr.length; i++) {
            if (BillUtils.isDataGoods(inputArr[i])) {
                BillPosition newBillPosition = BillUtils.parseInputGoodData(inputArr[i]);
                if (!goodsDirectory.containsKey(newBillPosition.getGoodsID()))
                    throw new ParsingException(4, "Parsing error. root.Goods directory isn't contains good with id = " + newBillPosition.getGoodsID());
                boolean isNeedAdd = true;
                for (int j = 0; j < newBillPositionList.size(); j++) {
                    if (newBillPositionList.get(j).getGoodsID() == newBillPosition.getGoodsID()) {
                        newBillPositionList.get(j).setCount(newBillPositionList.get(j).getCount() + newBillPosition.getCount());
                        isNeedAdd = false;
                    }
                }
                if (isNeedAdd) newBillPositionList.add(newBillPosition);
            } else {
                if (BillUtils.isDataDiscountCard(inputArr[i])) newDiscountCard = BillUtils.parseInputDiscountCardData(inputArr[i]);
                bill.setDiscountCard(newDiscountCard);
            }
        }
        if (newBillPositionList.isEmpty()) throw new ParsingException(2, "Parsing error. There aren't input good position for add");
        for (BillPosition billPosition : newBillPositionList) {
            bill.getBillPositions().add(billPosition);
        }
    }

    /**
     * Возвращает массив входных данных вида код_товара-кол-во_товара, прочитанного из файла
     * @param file
     * @return
     * @throws ParsingException
     */
    public static String[] readDataFromFile(File file) throws ParsingException {
        if (file.isDirectory() || !file.exists()) return null;
        ArrayList<String> result = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while (fileReader.ready()) {
                String str = fileReader.readLine();
                String[] strArr = str.split(" ");
                for (int i = 0; i < strArr.length; i++) {
                    result.add(strArr[i]);
                }
            }
        } catch (FileNotFoundException e) {
            throw new ParsingException(5, e.getMessage());
        } catch (IOException e) {
            throw new ParsingException(5, e.getMessage());
        }
        String[] str = new String[result.size()];
        return result.toArray(str);
    }

    /**
     * Анализирует массив строк и возвращает файл, в который будет проводиться запись.
     * Берется первая запись, дальше список не проверяется
     */
    public static File getOutFileFromStingArr(String[] inputArr) {
        //TODO вынести переменную в ini-файл
        final String fileNametag = "-outFile=";
        for (int i = 0; i < inputArr.length; i++) {
            if (inputArr[i].startsWith(fileNametag)) {
                String fileName=inputArr[i].substring(fileNametag.length());
                return new File(fileName);
            }
        }
        return null;
    }
}
