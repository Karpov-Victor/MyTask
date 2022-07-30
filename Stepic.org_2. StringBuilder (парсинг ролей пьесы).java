// https://metanit.com/java/tutorial/7.3.php
// https://javarush.ru/groups/posts/2351-znakomstvo-so-string-stringbuffer-i-stringbuilder-v-java
// https://stepik.org/lesson/12762/step/10?unit=3110

/*
        Вам дан список ролей и сценарий пьесы в виде массива строчек.

        Каждая строчка сценария пьесы дана в следующем виде:
        Роль: текст

        Текст может содержать любые символы.

        Напишите метод, который будет группировать строчки по ролям, пронумеровывать их и возвращать результат в виде готового текста (см. пример). Каждая группа распечатывается в следующем виде:

        Роль:
        i) текст
        j) текст2
        ...
        ==перевод строки==

        i и j -- номера строк в сценарии. Индексация строчек начинается с единицы, выводить группы следует в соответствии с порядком ролей. Переводы строк между группами обязательны, переводы строк в конце текста не учитываются.

        Заметим, что вам предстоит обработка огромной пьесы в 50 000 строк для 10 ролей – соответственно, неправильная сборка результирующей строчки может выйти за ограничение по времени.

        Обратите внимание еще на несколько нюансов:

        имя персонажа может встречаться в строке более одного раза, в том числе с двоеточием;
        название одной роли может быть префиксом названия другой роли (например, "Лука" и "Лука Лукич");
        роль, у которой нет реплик, тоже должна присутствовать в выходном файле;
        в качестве перевода строки надо использовать символ '\n' (перевод строки в стиле UNIX);
        будьте внимательны, не добавляйте лишних пробелов в конце строк.
        Sample Input:

        roles:
        Городничий
        Аммос Федорович
        Артемий Филиппович
        Лука Лукич
        textLines:
        Городничий: Я пригласил вас, господа, с тем, чтобы сообщить вам пренеприятное известие: к нам едет ревизор.
        Аммос Федорович: Как ревизор?
        Артемий Филиппович: Как ревизор?
        Городничий: Ревизор из Петербурга, инкогнито. И еще с секретным предписаньем.
        Аммос Федорович: Вот те на!
        Артемий Филиппович: Вот не было заботы, так подай!
        Лука Лукич: Господи боже! еще и с секретным предписаньем!
        Sample Output:

        Городничий:
        1) Я пригласил вас, господа, с тем, чтобы сообщить вам пренеприятное известие: к нам едет ревизор.
        4) Ревизор из Петербурга, инкогнито. И еще с секретным предписаньем.

        Аммос Федорович:
        2) Как ревизор?
        5) Вот те на!

        Артемий Филиппович:
        3) Как ревизор?
        6) Вот не было заботы, так подай!

        Лука Лукич:
        7) Господи боже! еще и с секретным предписаньем!

// ================= РЕШЕНИЕ ========================== */
package com;

import java.math.BigInteger;

public class Solution {
    public static void main(String[] args) {
        String[] roles = {"Городничий", "Аммос Федорович", "Артемий Филиппович", "Лука Лукич"};
        String[] textLines = {"Городничий: Я пригласил вас, господа, с тем, чтобы сообщить вам пренеприятное известие: к нам едет ревизор.",
                "Аммос Федорович: Как ревизор?",
                "Артемий Филиппович: Как ревизор?",
                "Городничий: Ревизор из Петербурга, инкогнито. И еще с секретным предписаньем.",
                "Аммос Федорович: Вот те на!",
                "Артемий Филиппович: Вот не было заботы, так подай!",
                "Лука Лукич: Господи боже! еще и с секретным предписаньем!"};

        String resulString = printTextPerRole(roles, textLines);
        System.out.println(resulString);

        resulString = printTextPerRole2(roles, textLines);
        System.out.println(resulString);

    }

    public static String printTextPerRole(String[] roles, String[] textLines) {
        // Решение ЧЕРЕЗ СТРОКИ
        String resultString = "";
        for (int i = 0; i < roles.length; i++) {
            if (resultString != "") resultString = resultString + "\n";
            String personeName = roles[i]; // Определяем имя персоны
            boolean newRole = true;
            for (int j = 0; j < textLines.length; j++) {

                if (textLines[j].indexOf(personeName + ':', 0) == 0) {
                    // парсинг строки, оставляем только текст
                    String personeVoice = textLines[j].substring(personeName.length() + 1);

                    //System.out.println(personeVoice);
                    if (newRole) { // Если роль новая, то прописываем её, если нет, то продолжаем без указания роли
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
        // РЕШЕНИЕ ЧЕРЕЗ STRINGBUILDER
        StringBuilder resultString = new StringBuilder(50000);

        for (int i = 0; i < roles.length; i++) {

            if (resultString.toString() != "") resultString.append("\n");

            String personeName = roles[i]; // Определяем имя персоны
            boolean newRole = true;
            for (int j = 0; j < textLines.length; j++) {

                if (textLines[j].indexOf(personeName + ':', 0) == 0) {
                    // парсинг строки, оставляем только текст
                    String personeVoice = textLines[j].substring(personeName.length() + 1);

                    //System.out.println(personeVoice);
                    if (newRole) { // Если роль новая, то прописываем её, если нет, то продолжаем без указания роли

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

