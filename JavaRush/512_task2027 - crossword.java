/*
1. Дан двумерный массив, который содержит буквы английского алфавита в нижнем регистре.
2. Метод detectAllWords должен найти все слова из words в массиве crossword.
3. Элемент(startX, startY) должен соответствовать первой букве слова, элемент(endX, endY) - последней.
text - это само слово, располагается между начальным и конечным элементами
4. Все слова есть в массиве.
5. Слова могут быть расположены горизонтально, вертикально и по диагонали как в нормальном, так и в обратном порядке.
6. Метод main не участвует в тестировании.

Требования:
•	В классе Solution должен существовать метод detectAllWords.
•	В классе Solution должен существовать статический класс Word.
•	Класс Solution не должен содержать статические поля.
•	Метод detectAllWords должен быть статическим.
•	Метод detectAllWords должен быть публичным.
•	Метод detectAllWords должен возвращать список всех слов в кроссворде (согласно условию задачи).

======================= ЗАДАЧА ======================= */

package com.javarush.task.task20.task2027;

import java.util.ArrayList;
import java.util.List;

/* 
Кроссворд
*/

public class Solution {
    public static void main(String[] args) {
        int[][] crossword = new int[][]{
                {'f', 'd', 'e', 'r', 'l', 'k'}, // [СТРОКА] [СТОЛБЕЦ]
                {'u', 's', 'a', 'm', 'e', 'o'}, // crossword.length - высота столбца массива
                {'l', 'n', 'g', 'r', 'o', 'v'}, // crossword[0].length - длина строки массива
                {'m', 'l', 'p', 'r', 'r', 'h'},
                {'p', 'o', 'e', 'e', 'j', 'j'}
        };
        List<Word> list= new ArrayList<>();




        list = detectAllWords(crossword, "home", "same");

        if (list != null) {
            for (Word i:list) {
                System.out.println(i);
            }
        }

    }

    public static List<Word> detectAllWords(int[][] crossword, String... words) {
        List<Word> list = new ArrayList<>();
        for (int wordCounter = 0; wordCounter < words.length; wordCounter++) {
            String findWord = words[wordCounter];
            System.out.println(findWord);

            for (int i = 0; i < crossword.length; i++) {
                for (int j = 0; j < crossword[0].length; j++) {
                    //System.out.println("i="+i+" j="+j);
                    int arr[];
                    arr = detectOneWord(crossword,i,j,findWord, "LEFT");
                    if (arr != null) list.add(createNewWord(arr,findWord));
                    arr = detectOneWord(crossword,i,j,findWord, "RIGHT");
                    if (arr != null) list.add(createNewWord(arr,findWord));
                    arr = detectOneWord(crossword,i,j,findWord, "UP");
                    if (arr != null) list.add(createNewWord(arr,findWord));
                    arr = detectOneWord(crossword,i,j,findWord, "DOWN");
                    if (arr != null) list.add(createNewWord(arr,findWord));
                    arr = detectOneWord(crossword,i,j,findWord, "RIGHT-DOWN");
                    if (arr != null) list.add(createNewWord(arr,findWord));
                    arr = detectOneWord(crossword,i,j,findWord, "LEFT-DOWN");
                    if (arr != null) list.add(createNewWord(arr,findWord));
                    arr = detectOneWord(crossword,i,j,findWord, "RIGHT-UP");
                    if (arr != null) list.add(createNewWord(arr,findWord));
                    arr = detectOneWord(crossword,i,j,findWord, "LEFT-UP");
                    if (arr != null) list.add(createNewWord(arr,findWord));


                }
            }
        }
        return list;
    }

    public static Word createNewWord(int[] arr, String word) {
        Word newWord = new Word(word);
        newWord.setStartPoint(arr[0],arr[1]);
        newWord.setEndPoint(arr[2],arr[3]);
        return newWord;
    }

    public static int[] detectOneWord(int[][] crossword, int coorY, int coorX, String word, String vector) {
        // [СТРОКА] [СТОЛБЕЦ] int coorY, int coorX
        int[] coordinate = new int[4]; //startX, startY, endX, endY

        if (vector.equals("LEFT")) {
            String tempStr = "";
            int i = coorX;
            int symbolCount = 0;
            while ((i >= 0) && symbolCount < word.length()){
                tempStr = tempStr + (char) crossword[coorY][i];
                i--;
                symbolCount++;
            }
            //System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = i+1;
                coordinate[3] = coorY;
                return coordinate;
            } else {
                return null;
            }
        }

        if (vector.equals("RIGHT")) {
            String tempStr = "";
            int i = coorX;
            int symbolCount = 0;
            while ((i < crossword[0].length ) && symbolCount < word.length()){
                tempStr = tempStr + (char) crossword[coorY][i];
                i++;
                symbolCount++;
            }
            //System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = i-1;
                coordinate[3] = coorY;
                return coordinate;
            } else {
                return null;
            }
        }

        if (vector.equals("UP")) {
            String tempStr = "";
            int i = coorY;
            int symbolCount = 0;
            while ((i >= 0 ) && symbolCount < word.length()){
                tempStr = tempStr + (char) crossword[i][coorX];
                i--;
                symbolCount++;
            }
            //System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = coorX;
                coordinate[3] = i+1;
                return coordinate;
            } else {
                return null;
            }
        }

        if (vector.equals("DOWN")) {
            String tempStr = "";
            int i = coorY;
            int symbolCount = 0;
            while ((i < crossword.length ) && symbolCount < word.length()){
                tempStr = tempStr + (char) crossword[i][coorX];
                i++;
                symbolCount++;
            }
            //System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = coorX;
                coordinate[3] = i-1;
                return coordinate;
            } else {
                return null;
            }
        }

        // [СТРОКА] [СТОЛБЕЦ] int coorY, int coorX
        if (vector.equals("RIGHT-DOWN")) {
            String tempStr = "";
            int i = coorY;
            int j = coorX;
            int symbolCount = 0;
            while ((i < crossword.length) && (j < crossword[coorY].length) && symbolCount < word.length()) {
                tempStr = tempStr + (char) crossword[i][j];
                i++;
                j++;
                symbolCount++;
            }
            //System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = j-1;
                coordinate[3] = i-1;
                return coordinate;
            } else {
                return null;
            }
        }

        // [СТРОКА] [СТОЛБЕЦ] int coorY, int coorX
        if (vector.equals("LEFT-DOWN")) {
            String tempStr = "";
            int i = coorY; // Строка
            int j = coorX; // Столбец
            int symbolCount = 0;
            while ((i < crossword.length) && (j >=0) && symbolCount < word.length()) {
                tempStr = tempStr + (char) crossword[i][j];
                i++;
                j--;
                symbolCount++;
            }
            //System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = j+1;
                coordinate[3] = i-1;
                return coordinate;
            } else {
                return null;
            }
        }

        // [СТРОКА] [СТОЛБЕЦ] int coorY, int coorX
        if (vector.equals("RIGHT-UP")) {
            String tempStr = "";
            int i = coorY; // Строка
            int j = coorX; // Столбец
            int symbolCount = 0;
            while ((i >=0) && (j < crossword[coorY].length) && symbolCount < word.length()) {
                tempStr = tempStr + (char) crossword[i][j];
                i--;
                j++;
                symbolCount++;
            }
            //System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = j-1;
                coordinate[3] = i+1;
                return coordinate;
            } else {
                return null;
            }
        }

        // [СТРОКА] [СТОЛБЕЦ] int coorY, int coorX
        if (vector.equals("LEFT-UP")) {
            String tempStr = "";
            int i = coorY; // Строка
            int j = coorX; // Столбец
            int symbolCount = 0;
            while ((i >=0) && (j >=0) && symbolCount < word.length()) {
                tempStr = tempStr + (char) crossword[i][j];
                i--;
                j--;
                symbolCount++;
            }
            //System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = j+1;
                coordinate[3] = i+1;
                return coordinate;
            } else {
                return null;
            }
        }

        return null;
    }

    public static int[] test(int[][] crossword, int coorY, int coorX, String word, String vector){
        int[] coordinate = new int[4];
        if (vector.equals("RIGHT")) {
            String tempStr = "";
            int i = coorX;
            int symbolCount = 0;
            System.out.println("crossword[coorX].length = " + crossword[0].length);
            while ((i < crossword[0].length ) && symbolCount < word.length()){
                tempStr = tempStr + (char) crossword[coorY][i];
                i++;
                symbolCount++;
            }
            System.out.println(tempStr);
            if (tempStr.equals(word)) {
                coordinate[0] = coorX;
                coordinate[1] = coorY;
                coordinate[2] = i-1;
                coordinate[3] = coorY;
                return coordinate;
            } else {
                return null;
            }
        }
        return null;
    }

    public static class Word {
        private String text;
        private int startX;
        private int startY;
        private int endX;
        private int endY;

        public Word(String text) {
            this.text = text;
        }

        public void setStartPoint(int i, int j) {
            startX = i;
            startY = j;
        }

        public void setEndPoint(int i, int j) {
            endX = i;
            endY = j;
        }

        @Override
        public String toString() {
            return String.format("%s - (%d, %d) - (%d, %d)", text, startX, startY, endX, endY);
        }
    }
}
