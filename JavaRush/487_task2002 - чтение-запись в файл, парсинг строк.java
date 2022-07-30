/*
�������� ������ ������ � ���� � ������ �� ����� ��� ������ JavaRush.
������ ����� � ������� User ���� �� �����. ���� � ����� ������ ������� � ������� long.
����� main ���������� ������ ��� ���� � �� ��������� � ������������.

����������:
�	������ ������/������ ������������� � ������� save/load ������ �������� ��������� � ������, ���� ������ users ������.
�	������ ������/������ ������������� � ������� save/load ������ �������� ��������� � ������, ���� ������ users �� ������.
�	����� Solution.JavaRush �� ������ ������������ ��������� Serializable.
�	����� Solution.JavaRush ������ ���� ���������.
�	����� Solution.JavaRush �� ������ ������������ ��������� Externalizable.

*/

// ==================== ������� =======================================
package com.javarush.task.task20.task2002;

import java.io.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/* 
������ � ����� � ����: JavaRush
*/

public class Solution {
    public static void main(String[] args) {
        //you can find your_file_name.tmp in your TMP directory or adjust outputStream/inputStream according to your file's actual location
        //�� ������ ����� your_file_name.tmp � ����� TMP ��� ��������� outputStream/inputStream � ������������ � ����� � ������ ��������� �����
        try {
            //File yourFile = File.createTempFile("your_file_name", null);
            File yourFile = new File("d:\\1");

            OutputStream outputStream = new FileOutputStream(yourFile);
            InputStream inputStream = new FileInputStream(yourFile);

            JavaRush javaRush = new JavaRush();
            //initialize users field for the javaRush object here - ��������������� ���� users ��� ������� javaRush ���
            javaRush = new JavaRush();


            User user = new User();
            user.setFirstName("Mike");
            user.setLastName("Askel");
            //user.setBirthDate(new Date(Date.UTC(1980, Calendar.NOVEMBER, 15, 18, 15, 12)));
            LocalDateTime localDateTime = LocalDateTime.of(1989, Month.FEBRUARY, 15, 18, 51, 33, 458);
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            user.setBirthDate(date);
            user.setMale(true);
            user.setCountry(User.Country.OTHER);
            javaRush.users.add(user);
            // System.out.println("������� ������������: " + user.toString());

            User user2 = new User();
            user2.setFirstName("Vasya");
            user2.setLastName("Pupkin");
            localDateTime = LocalDateTime.of(1960, Month.FEBRUARY, 01, 18, 51, 33, 330);
            date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            user2.setBirthDate(date);
            user2.setMale(true);
            user2.setCountry(User.Country.UKRAINE);
            //javaRush.users.add(user2);



            javaRush.save(outputStream);
            outputStream.flush();

            JavaRush loadedObject = new JavaRush();
            loadedObject.load(inputStream);
            // here check that the javaRush object is equal to the loadedObject object - ��������� ���, ��� javaRush � loadedObject �����
            // System.out.println("������ ������������ = " + loadedObject.users.get(0).toString());

            if (javaRush.equals(loadedObject)) {
                 System.out.println("������������ �����");
            } else {
                 System.out.println("������������ �� �����");
            }

            outputStream.close();
            inputStream.close();


        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Oops, something is wrong with my file");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Oops, something is wrong with the save/load method");
        }
    }

    public static class JavaRush {
        public List<User> users = new ArrayList<>();

        public void save(OutputStream outputStream) throws Exception {
            //implement this method - ���������� ���� �����
            if (users.size() == 0) return;
            PrintWriter printWriter = new PrintWriter(outputStream);
            for (int i = 0; i < users.size(); i++) {
                printWriter.println(users.get(i).getFirstName());
                printWriter.println(users.get(i).getLastName());
                long dateBirthday = users.get(i).getBirthDate().getTime();
                printWriter.println(dateBirthday);
                printWriter.println(users.get(i).isMale());
                printWriter.println(users.get(i).getCountry());
                printWriter.flush();
                System.out.println("������� ������������ : " + users.get(i).toString());
            }

            printWriter.close();


        }

        public void load(InputStream inputStream) throws Exception {
            //implement this method - ���������� ���� �����
            String firstName;
            String lastName;
            Date birthDate = new Date();
            boolean isMale;
            User.Country country = User.Country.OTHER;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            firstName = bufferedReader.readLine();
            while (firstName != null) {
                {
                lastName = bufferedReader.readLine();
                //String a = bufferedReader.readLine();
                //birthDate.setTime(Long.parseLong(a));
                    birthDate = new Date(Long.parseLong(bufferedReader.readLine()));


                if (bufferedReader.readLine().equals("true")) {
                    isMale = true;
                } else {
                    isMale = false;
                }
                String str = bufferedReader.readLine();
                switch (str) {
                    case "UKRAINE": country = User.Country.UKRAINE; break;
                    case "RUSSIA": country = User.Country.RUSSIA; break;
                    case "OTHER": country = User.Country.OTHER; break;
                }
                    User user = new User();
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setBirthDate(birthDate);
                    user.setMale(isMale);
                    user.setCountry(country);
                    //System.out.println();
                    System.out.println("�������� ������������: " + user.toString());
                    this.users.add(user);
                    firstName = null;
                }
                if (bufferedReader.ready()) { firstName = bufferedReader.readLine();}
            }
            bufferedReader.close();

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JavaRush javaRush = (JavaRush) o;

            return users != null ? users.equals(javaRush.users) : javaRush.users == null;

        }

        @Override
        public int hashCode() {
            return users != null ? users.hashCode() : 0;
        }
    }
}