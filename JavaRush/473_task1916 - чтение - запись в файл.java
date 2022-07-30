/*
������� � ������� 2 ���� � ������ - file1, file2.
����� �������� ������. ��� ��� file2 �������� ����������� ������� file1, �� ����� ����� ���������.
����� ������� ������������ ������ ����� �� ����� ������ � �������� ��� ������ � ������ lines.
������� �����������:

���� ������ � ����� ������ ���������, �� � ��������� ��� �������� � ��������� (����������) SAME.
��������, SAME ������1.
���� ������ ���� � file1, �� �� ��� � file2, �� �������, ��� ������ ������� � � ��������� ��� �������� � ��������� (����������) REMOVED.
��������, REMOVED ������2.
���� ������ ��� � file1, �� ��� ���� � file2, �� �������, ��� ������ �������� � � ��������� ��� �������� � ��������� (����������) ADDED.
��������, ADDED ������0.
�������� ADDED � REMOVED �� ����� ���� ������, ��� ������ ��������� SAME.
������ ������ ���� � ������� ��� ����������� � ��������, ��� ���� ������ ��� � ������������ �����.
� ������������ � ������������� ������ ������ ����� ���!
������ 1:
���������� ������������� ���� (file1):
������1
������2
������3
������4
������5
������1
������2
������3
������5
������0

���������� "����������������" ����� (file2):
������1
������3
������5
������0
������1
������3
������4
������5

��������� �����������:
������������    ���������������    �����
file1:          file2:             ���������:(lines)
 
������1         ������1            SAME ������1
������2                            REMOVED ������2
������3         ������3            SAME ������3
������4                            REMOVED ������4
������5         ������5            SAME ������5
                ������0            ADDED ������0
������1         ������1            SAME ������1
������2                            REMOVED ������2
������3         ������3            SAME ������3
                ������4            ADDED ������4
������5         ������5            SAME ������5
������0                            REMOVED ������0

������ 2:
���������� ������������� ����� (file1):
������1

���������� "����������������" ����� (file2):
������1
������0

��������� �����������:
������������    ���������������    �����
file1:          file2:             ���������:(lines)
 
������1         ������1            SAME ������1
                ������0            ADDED ������0

����������:
�	����� Solution ������ ��������� ����� LineItem.
�	����� Solution ������ ��������� enum Type.
�	����� Solution ������ ��������� ��������� ����������� ���� lines ���� List<LineItem>, ������� ����� �������������������.
�	� ������ main(String[] args) ��������� ������ ��������� ���� � ������ � ������� (��������� BufferedReader).
�	� ������ main(String[] args) BufferedReader ��� ���������� ������ � ������� ������ ���� ������.
�	��������� ������ ��������� ���������� ������� � ������� ����� (��������� FileReader).
�	������ ������ �� ������ (FileReader) ������ ���� �������.
�	������ lines ������ ��������� ������������ ������ ����� �� ������, ��� ��� ������ ������ ������� ���� �� �������� ADDED, REMOVED, SAME.

*/

// ==========================  ������� ========================================
package com.javarush.task.task19.task1916;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* 
����������� ���������
*/

public class Solution {
    public static List<LineItem> lines = new ArrayList<LineItem>();

    public static void main(String[] args) {
        String fileName1 = "d:\\1";
        String fileName2 = "d:\\2";

        // ��������� ��� ����� � �������
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
        ADDED,        //��������� ����� ������
        REMOVED,      //������� ������
        SAME          //��� ���������
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
