/*
stepic.otg Java. Базовый курс
Реализуйте метод, сливающий два отсортированных по неубыванию массива чисел в один отсортированный в том же порядке массив. Массивы могут быть любой длины, в том числе нулевой.

Предполагается, что вы реализуете алгоритм слияния, имеющий линейную сложность: он будет идти по двум исходным массивам и сразу формировать отсортированный результирующий массив. Так, чтобы сортировка полученного массива при помощи Arrays.sort() уже не требовалась. К сожалению, автоматически это не проверить, так что это остается на вашей совести :)

Воспользуйтесь предоставленным шаблоном. Декларацию класса, метод main и обработку ввода-вывода добавит проверяющая система.

Пример

Если на вход подаются массивы {0, 2, 2} и {1, 3}, то на выходе должен получиться массив {0, 1, 2, 2, 3}
*/

package com;

import java.math.BigInteger;

public class Solution {
    public static void main(String[] args) {

        // Тесты
        // int[] a1 = {};
        // int[] a2 = {};
        // int[] a1 = new int[] {0, 2, 2, 5, 7, 8};
        // int[] a2 = new int[] {1, 3, 6, 10};
        // int[] a1 = {6}, a2 = {1, 3, 5}; // [1, 3, 5, 6]
        // int[] a1 =  {0, 2, 2}, a2={1, 3}; // [0, 1, 2, 2, 3]
        // int[] a1 =  {0, 2, 2, 6}, a2={1, 3}; // [0, 1, 2, 2, 3, 6]
        // int[] a1 = {0, 2, 2, 10, 10, 20}, a2 = {1, 3, 5, 7, 8, 10, 10};
        // int[] a1 = {-3, -2, -1, 0}, a2 =  {-1, 1, 1}; // [-3, -2, -1, -1, 0, 1, 1]
        // int[] a1 = {}, a2 =  {-1, 5, 6, 11}; // [-1, 5, 6, 11]
        // int[] a1 = {-3, -2, -1, 0}, a2 = {}; // [-3, -2, -1, 0]
        
        int[] a1 = {-30, -2, -1, 1}, a2 =  {-15, -4, 7, 10}; // [-30, -15, -4, -2, -1, 1, 7, 10]

        int[] resultArray = mergeArrays(a1, a2);

        if (resultArray != null) {
            for (int i = 0; i < resultArray.length; i++) {
                System.out.println(resultArray[i] + " ");
            }
        }
    }

    public static int[] mergeArrays(int[] a1, int[] a2) {
        if ( (a1.length == 0) && (a2.length == 0) ) {
            //System.out.println("Нулевые массивы");
            int[] resultArray = {};
            return resultArray;
        }

        int[] resultArray = new int[a1.length+ a2.length]; // Описываем новый массив
        int a1i = 0; // Описываем индексы для перебора первого массива
        int a2i = 0; // Описываем индексы для перебора второго массива

        for (int i = 0; i < resultArray.length; i++) { //Начинаем цикл внесения в результирующий массив данных
            // Если первый массив нулевой
            if (a1.length == 0) {
                resultArray[i] = a2[i];
                if (a2.length == i+1) break;
                continue;
            }
            // Если второй массив нулевой
            if (a2.length == 0) {
                resultArray[i] = a1[i];
                if (a1.length == i+1) break;
                continue;
            }


            if (a1[a1i] < a2[a2i]) {
                resultArray[i] =a1[a1i];
                a1i++;
                if (a1i > a1.length - 1) {
                    for (; a2i < a2.length; a2i++) {
                        i++;
                        resultArray[i] =a2[a2i];
                    }
                    break;
                }
            } else {
                resultArray[i] =a2[a2i];
                a2i++;
                if (a2i > a2.length - 1) {
                    for (; a1i < a1.length; a1i++) {
                        i++;
                        resultArray[i] =a1[a1i];
                    }
                    break;
                }
            }
        }
        return resultArray;
    }

}
