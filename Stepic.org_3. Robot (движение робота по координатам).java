/*
�� ������� ���� ��������� �����. ������� ������ �� ���� ����������� ����� ������������� ������������: X � Y. ��� X ������� ����� �������, ��� Y � ����� �����. (�������, ��� �������� ������� ������� � �����?)
� ��������� ������ ����� ��������� � ��������� ������� �� ����. ����� ��������, ���� ����� �������: �����, ����, ������� ��� ������. ���� ������ � �������� ������ � �������� ����� �������� ����.
����� ����������� ������� Robot. �� ������ ������������ ���������� ��� �������� (���������� ��� ����������):

public class Robot {

    public Direction getDirection() {
        // ������� ����������� �������
    }
    public int getX() {
        // ������� ���������� X
    }
    public int getY() {
        // ������� ���������� Y
    }
    public void turnLeft() {
        // ����������� �� 90 �������� ������ ������� �������
    }
    public void turnRight() {
        // ����������� �� 90 �������� �� ������� �������
    }
    public void stepForward() {
        // ��� � ����������� �������
        // �� ���� ��� ����� �������� ���� ���� ���������� �� �������
    }
}
Direction, ����������� ������� ������,  � ��� ������������:
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

��� ���  ��������:

������:

� ����� ��������: toX == 3, toY == 0; ��������� ��������� ������ �����: robot.getX() == 0, robot.getY() == 0, robot.getDirection() == Direction.UP

����� �������� ������ � ��������� ����� (3, 0), ����� ������ ������� � ������ ��������� ������:

robot.turnRight();
robot.stepForward();
robot.stepForward();
robot.stepForward();


P.S. ���� ��� ����������� ��� �������, �� ��� ����� �������� �� ���� ���� Robocode, � ������� ���� �������� �������� ���������� ������. ���������, ���������� ������� ������, ����������� ����� �����.

*/

// ======== ������� =============

import java.math.*;
import java.util.Arrays;

public class module2 {
    static int placeX = -40;
    static int placeY = -50;
    public static void main(String[] args) {

        Robot robot = new Robot(-10,-10, Direction.UP);
        System.out.println("����� ��������� �����:  X = " + robot.getX() + " Y = " + robot.getY() + " Direction = " + robot.getDirection());
        moveRobot(robot, placeX, placeY);

        System.out.println("���� ���� ������ � placeX = " + placeX + " placeY = " + placeY);
        System.out.println("����� ��������� �����:  X = " + robot.getX() + " Y = " + robot.getY() + " Direction = " + robot.getDirection());

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


