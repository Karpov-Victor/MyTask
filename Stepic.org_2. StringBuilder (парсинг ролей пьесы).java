// https://metanit.com/java/tutorial/7.3.php
// https://javarush.ru/groups/posts/2351-znakomstvo-so-string-stringbuffer-i-stringbuilder-v-java
// https://stepik.org/lesson/12762/step/10?unit=3110

/*
        ��� ��� ������ ����� � �������� ����� � ���� ������� �������.

        ������ ������� �������� ����� ���� � ��������� ����:
        ����: �����

        ����� ����� ��������� ����� �������.

        �������� �����, ������� ����� ������������ ������� �� �����, ��������������� �� � ���������� ��������� � ���� �������� ������ (��. ������). ������ ������ ��������������� � ��������� ����:

        ����:
        i) �����
        j) �����2
        ...
        ==������� ������==

        i � j -- ������ ����� � ��������. ���������� ������� ���������� � �������, �������� ������ ������� � ������������ � �������� �����. �������� ����� ����� �������� �����������, �������� ����� � ����� ������ �� �����������.

        �������, ��� ��� ��������� ��������� �������� ����� � 50 000 ����� ��� 10 ����� � ��������������, ������������ ������ �������������� ������� ����� ����� �� ����������� �� �������.

        �������� �������� ��� �� ��������� �������:

        ��� ��������� ����� ����������� � ������ ����� ������ ����, � ��� ����� � ����������;
        �������� ����� ���� ����� ���� ��������� �������� ������ ���� (��������, "����" � "���� �����");
        ����, � ������� ��� ������, ���� ������ �������������� � �������� �����;
        � �������� �������� ������ ���� ������������ ������ '\n' (������� ������ � ����� UNIX);
        ������ �����������, �� ���������� ������ �������� � ����� �����.
        Sample Input:

        roles:
        ����������
        ����� ���������
        ������� ����������
        ���� �����
        textLines:
        ����������: � ��������� ���, �������, � ���, ����� �������� ��� ������������� ��������: � ��� ���� �������.
        ����� ���������: ��� �������?
        ������� ����������: ��� �������?
        ����������: ������� �� ����������, ���������. � ��� � ��������� ������������.
        ����� ���������: ��� �� ��!
        ������� ����������: ��� �� ���� ������, ��� �����!
        ���� �����: ������� ����! ��� � � ��������� ������������!
        Sample Output:

        ����������:
        1) � ��������� ���, �������, � ���, ����� �������� ��� ������������� ��������: � ��� ���� �������.
        4) ������� �� ����������, ���������. � ��� � ��������� ������������.

        ����� ���������:
        2) ��� �������?
        5) ��� �� ��!

        ������� ����������:
        3) ��� �������?
        6) ��� �� ���� ������, ��� �����!

        ���� �����:
        7) ������� ����! ��� � � ��������� ������������!

// ================= ������� ========================== */
package com;

import java.math.BigInteger;

public class Solution {
    public static void main(String[] args) {
        String[] roles = {"����������", "����� ���������", "������� ����������", "���� �����"};
        String[] textLines = {"����������: � ��������� ���, �������, � ���, ����� �������� ��� ������������� ��������: � ��� ���� �������.",
                "����� ���������: ��� �������?",
                "������� ����������: ��� �������?",
                "����������: ������� �� ����������, ���������. � ��� � ��������� ������������.",
                "����� ���������: ��� �� ��!",
                "������� ����������: ��� �� ���� ������, ��� �����!",
                "���� �����: ������� ����! ��� � � ��������� ������������!"};

        String resulString = printTextPerRole(roles, textLines);
        System.out.println(resulString);

        resulString = printTextPerRole2(roles, textLines);
        System.out.println(resulString);

    }

    public static String printTextPerRole(String[] roles, String[] textLines) {
        // ������� ����� ������
        String resultString = "";
        for (int i = 0; i < roles.length; i++) {
            if (resultString != "") resultString = resultString + "\n";
            String personeName = roles[i]; // ���������� ��� �������
            boolean newRole = true;
            for (int j = 0; j < textLines.length; j++) {

                if (textLines[j].indexOf(personeName + ':', 0) == 0) {
                    // ������� ������, ��������� ������ �����
                    String personeVoice = textLines[j].substring(personeName.length() + 1);

                    //System.out.println(personeVoice);
                    if (newRole) { // ���� ���� �����, �� ����������� �, ���� ���, �� ���������� ��� �������� ����
                        resultString = resultString + personeName + ":\n";
                        newRole = false;
                    }
                    resultString = resultString + Integer.toString(j + 1) + ')' + personeVoice + "\n";
                }
            }

        }
        return resultString;

    }



    public static String printTextPerRole2(String[] roles, String[] textLines) {
        // ������� ����� STRINGBUILDER
        StringBuilder resultString = new StringBuilder(50000);

        for (int i = 0; i < roles.length; i++) {

            if (resultString.toString() != "") resultString.append("\n");

            String personeName = roles[i]; // ���������� ��� �������
            boolean newRole = true;
            for (int j = 0; j < textLines.length; j++) {

                if (textLines[j].indexOf(personeName + ':', 0) == 0) {
                    // ������� ������, ��������� ������ �����
                    String personeVoice = textLines[j].substring(personeName.length() + 1);

                    //System.out.println(personeVoice);
                    if (newRole) { // ���� ���� �����, �� ����������� �, ���� ���, �� ���������� ��� �������� ����

                        resultString.append(personeName + ":\n");
                        newRole = false;
                    }
                    resultString.append(Integer.toString(j + 1) + ')' + personeVoice + "\n");
                }
            }

        }
        return resultString.toString();
    }
}

