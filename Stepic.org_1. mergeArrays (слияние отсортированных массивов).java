/*
stepic.otg Java. ������� ����
���������� �����, ��������� ��� ��������������� �� ���������� ������� ����� � ���� ��������������� � ��� �� ������� ������. ������� ����� ���� ����� �����, � ��� ����� �������.

��������������, ��� �� ���������� �������� �������, ������� �������� ���������: �� ����� ���� �� ���� �������� �������� � ����� ����������� ��������������� �������������� ������. ���, ����� ���������� ����������� ������� ��� ������ Arrays.sort() ��� �� �����������. � ���������, ������������� ��� �� ���������, ��� ��� ��� �������� �� ����� ������� :)

�������������� ��������������� ��������. ���������� ������, ����� main � ��������� �����-������ ������� ����������� �������.

������

���� �� ���� �������� ������� {0, 2, 2} � {1, 3}, �� �� ������ ������ ���������� ������ {0, 1, 2, 2, 3}
*/

package com;

import java.math.BigInteger;

public class Solution {
    public static void main(String[] args) {

        // �����
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
            //System.out.println("������� �������");
            int[] resultArray = {};
            return resultArray;
        }

        int[] resultArray = new int[a1.length+ a2.length]; // ��������� ����� ������
        int a1i = 0; // ��������� ������� ��� �������� ������� �������
        int a2i = 0; // ��������� ������� ��� �������� ������� �������

        for (int i = 0; i < resultArray.length; i++) { //�������� ���� �������� � �������������� ������ ������
            // ���� ������ ������ �������
            if (a1.length == 0) {
                resultArray[i] = a2[i];
                if (a2.length == i+1) break;
                continue;
            }
            // ���� ������ ������ �������
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
