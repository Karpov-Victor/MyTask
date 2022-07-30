/*
Считать с консоли 2 пути к файлам - file1, file2.
Файлы содержат строки. Так как file2 является обновленной версией file1, то часть строк совпадает.
Нужно создать объединенную версию строк из обоих файлов и записать эти строки в список lines.
Правила объединения:

Если строка в обоих файлах совпадает, то в результат она попадает с операцией (приставкой) SAME.
Например, SAME строка1.
Если строка есть в file1, но ее нет в file2, то считаем, что строку удалили и в результат она попадает с операцией (приставкой) REMOVED.
Например, REMOVED строка2.
Если строки нет в file1, но она есть в file2, то считаем, что строку добавили и в результат она попадает с операцией (приставкой) ADDED.
Например, ADDED строка0.
Операции ADDED и REMOVED не могут идти подряд, они всегда разделены SAME.
Пустые строки даны в примере для наглядности и означают, что этой строки нет в определенном файле.
В оригинальном и редактируемом файлах пустых строк нет!
Пример 1:
содержимое оригинального файл (file1):
строка1
строка2
строка3
строка4
строка5
строка1
строка2
строка3
строка5
строка0

содержимое "редактированного" файла (file2):
строка1
строка3
строка5
строка0
строка1
строка3
строка4
строка5

результат объединения:
оригинальный    редактированный    общий
file1:          file2:             результат:(lines)
 
строка1         строка1            SAME строка1
строка2                            REMOVED строка2
строка3         строка3            SAME строка3
строка4                            REMOVED строка4
строка5         строка5            SAME строка5
                строка0            ADDED строка0
строка1         строка1            SAME строка1
строка2                            REMOVED строка2
строка3         строка3            SAME строка3
                строка4            ADDED строка4
строка5         строка5            SAME строка5
строка0                            REMOVED строка0

Пример 2:
содержимое оригинального файла (file1):
строка1

содержимое "редактированного" файла (file2):
строка1
строка0

результат объединения:
оригинальный    редактированный    общий
file1:          file2:             результат:(lines)
 
строка1         строка1            SAME строка1
                строка0            ADDED строка0

Требования:
•	Класс Solution должен содержать класс LineItem.
•	Класс Solution должен содержать enum Type.
•	Класс Solution должен содержать публичное статическое поле lines типа List<LineItem>, которое сразу проинициализировано.
•	В методе main(String[] args) программа должна считывать пути к файлам с консоли (используй BufferedReader).
•	В методе main(String[] args) BufferedReader для считывания данных с консоли должен быть закрыт.
•	Программа должна считывать содержимое первого и второго файла (используй FileReader).
•	Потоки чтения из файлов (FileReader) должны быть закрыты.
•	Список lines должен содержать объединенную версию строк из файлов, где для каждой строки указана одна из операций ADDED, REMOVED, SAME.

*/

// ==========================  РЕШЕНИЕ ========================================
package com.javarush.task.task19.task1916;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* 
Отслеживаем изменения
*/

public class Solution {
    public static List<LineItem> lines = new ArrayList<LineItem>();

    public static void main(String[] args) {
        String fileName1 = "d:\\1";
        String fileName2 = "d:\\2";

        // Считываем имя файла с консоли
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in)) ) {
            fileName1 = bufferedReader.readLine();
            fileName2 = bufferedReader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ArrayList<String> arrayList1 = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();


        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName1))) {
            while (bufferedReader.ready()) {
                arrayList1.add(bufferedReader.readLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName2))) {
            while (bufferedReader.ready()) {
                arrayList2.add(bufferedReader.readLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }



        while (true) {
            int i = 0;
            int j = 0;
            if ((arrayList1.size() > 0) && (arrayList2.size() == 0)) {
                lines.add(new LineItem(Type.REMOVED, arrayList1.get(i)));
                arrayList1.remove(i);
                continue;
            }
            if ((arrayList1.size() == 0) && (arrayList2.size() > 0)) {
                lines.add(new LineItem(Type.ADDED, arrayList2.get(j)));
                arrayList2.remove(j);
                continue;
            }
            if ((arrayList1.size() == 0) && (arrayList2.size() == 0)) {
                break;
            }

            if (arrayList1.get(0).equals(arrayList2.get(0))) {
                lines.add(new LineItem(Type.SAME, arrayList1.get(0)));
                arrayList1.remove(0);
                arrayList2.remove(0);
                continue;
            } else {
                if (arrayList1.get(0).equals(arrayList2.get(1))) {
                    lines.add(new LineItem(Type.ADDED, arrayList2.get(0)));
                    arrayList2.remove(0);
                    continue;
                }

                if (arrayList1.get(1).equals(arrayList2.get(0))) {
                    lines.add(new LineItem(Type.REMOVED, arrayList1.get(0)));
                    arrayList1.remove(0);
                    continue;
                }
            }
        }


        for (LineItem a : lines) {
            System.out.println(a);
        }


    }


    public static enum Type {
        ADDED,        //добавлена новая строка
        REMOVED,      //удалена строка
        SAME          //без изменений
    }

    public static class LineItem {
        public Type type;
        public String line;

        public LineItem(Type type, String line) {
            this.type = type;
            this.line = line;
        }

        @Override
        public String toString() {
            return (this.type + " " + this.line);
        }
    }
}
