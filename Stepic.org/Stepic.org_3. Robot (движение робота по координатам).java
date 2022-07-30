/*
На игровом поле находится робот. Позиция робота на поле описывается двумя целочисленным координатами: X и Y. Ось X смотрит слева направо, ось Y — снизу вверх. (Помните, как рисовали графики функций в школе?)
В начальный момент робот находится в некоторой позиции на поле. Также известно, куда робот смотрит: вверх, вниз, направо или налево. Ваша задача — привести робота в заданную точку игрового поля.
Робот описывается классом Robot. Вы можете пользоваться следующими его методами (реализация вам неизвестна):

public class Robot {

    public Direction getDirection() {
        // текущее направление взгляда
    }
    public int getX() {
        // текущая координата X
    }
    public int getY() {
        // текущая координата Y
    }
    public void turnLeft() {
        // повернуться на 90 градусов против часовой стрелки
    }
    public void turnRight() {
        // повернуться на 90 градусов по часовой стрелке
    }
    public void stepForward() {
        // шаг в направлении взгляда
        // за один шаг робот изменяет одну свою координату на единицу
    }
}
Direction, направление взгляда робота,  — это перечисление:
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

Как это  выглядит:

Пример:

В метод передано: toX == 3, toY == 0; начальное состояние робота такое: robot.getX() == 0, robot.getY() == 0, robot.getDirection() == Direction.UP

Чтобы привести робота в указанную точку (3, 0), метод должен вызвать у робота следующие методы:

robot.turnRight();
robot.stepForward();
robot.stepForward();
robot.stepForward();


P.S. Если вам понравилось это задание, то вам может прийтись по душе игра Robocode, в которой надо написать алгоритм управления танком. Алгоритмы, написанные разными людьми, соревнуются между собой.

*/

// ======== РЕШЕНИЕ =============

import java.math.*;
import java.util.Arrays;

public class module2 {
    static int placeX = -40;
    static int placeY = -50;
    public static void main(String[] args) {

        Robot robot = new Robot(-10,-10, Direction.UP);
        System.out.println("Робот находится здесь:  X = " + robot.getX() + " Y = " + robot.getY() + " Direction = " + robot.getDirection());
        moveRobot(robot, placeX, placeY);

        System.out.println("Надо было прийти в placeX = " + placeX + " placeY = " + placeY);
        System.out.println("Робот находится здесь:  X = " + robot.getX() + " Y = " + robot.getY() + " Direction = " + robot.getDirection());

    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public static class Robot {
        int x;
        int y;
        Direction dir;

        public Robot (int x, int y, Direction dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        public Direction getDirection() {return dir;}

        public int getX() {return x;}

        public int getY() {return y;}

        public void turnLeft() {
            if      (dir == Direction.UP)    {dir = Direction.LEFT;}
            else if (dir == Direction.DOWN)  {dir = Direction.RIGHT;}
            else if (dir == Direction.LEFT)  {dir = Direction.DOWN;}
            else if (dir == Direction.RIGHT) {dir = Direction.UP;}
        }

        public void turnRight() {
            if      (dir == Direction.UP)    {dir = Direction.RIGHT;}
            else if (dir == Direction.DOWN)  {dir = Direction.LEFT;}
            else if (dir == Direction.LEFT)  {dir = Direction.UP;}
            else if (dir == Direction.RIGHT) {dir = Direction.DOWN;}
        }

        public void stepForward() {
            if (dir == Direction.UP)    {y++;}
            if (dir == Direction.DOWN)  {y--;}
            if (dir == Direction.LEFT)  {x--;}
            if (dir == Direction.RIGHT) {x++;}
        }
    }

    public static void moveRobot(Robot robot, int toX, int toY) {
        int robotX = robot.getX();
        int robotY = robot.getY();

        while (!((robotX == toX) && (robotY == toY))) {

            if (robot.getDirection() == Direction.UP) {
                if (robotY <= toY) {
                    while (robotY != toY) {
                        robot.stepForward();
                        robotY++;
                    }
                    if (robotX == toX) return;
                    if (robotX < toX) {
                        robot.turnRight();
                    } else {
                        robot.turnLeft();
                    }
                    continue;
                }
            }

            if (robot.getDirection() == Direction.DOWN) {
                if (robotY >= toY) {
                    while (robotY != toY) {
                        robot.stepForward();
                        robotY--;
                    }
                    if (robotX == toX) return;
                    if (robotX > toX) {
                        robot.turnRight();
                    } else {
                        robot.turnLeft();
                    }
                    continue;
                }
            }

            if (robot.getDirection() == Direction.LEFT) {
                if (robotX >= toX) {
                    while (robotX != toX) {
                        robot.stepForward();
                        robotX--;
                    }
                    if (robotY == toY) return;
                    if (robotY < toY) {
                        robot.turnRight();
                    } else {
                        robot.turnLeft();
                    }
                    continue;
                }
            }

            if (robot.getDirection() == Direction.RIGHT) {
                if (robotX <= toX) {
                    while (robotX != toX) {
                        robot.stepForward();
                        robotX++;
                    }
                    if (robotY == toY) return;
                    if (robotY > toY) {
                        robot.turnRight();
                    } else {
                        robot.turnLeft();
                    }
                    continue;
                }
            }
            robot.turnRight();
        }

    }
}


