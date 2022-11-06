package game2048;

import java.awt.event.ItemListener;
import java.util.*;

public class Model {

    /**
     * Создание нового массива 4 на 4.
     * ДЛЯ ТЕСТОВ
     */
    public void createTestArray() {
        Tile[] line0 = {new Tile(1), new Tile(2), new Tile(3), new Tile(4)};
        Tile[] line1 = {new Tile(5), new Tile(6), new Tile(7), new Tile(8)};
        Tile[] line2 = {new Tile(9), new Tile(10), new Tile(11), new Tile(12)};
        Tile[] line3 = {new Tile(13), new Tile(14), new Tile(15), new Tile(16)};
        gameTiles[0] = line0;
        gameTiles[1] = line1;
        gameTiles[2] = line2;
        gameTiles[3] = line3;
        printGameTiles(gameTiles);
    }

    /**
     * Вывод на экран выбранной строки
     * ДЛЯ ТЕСТОВ
     * @param lineNumber
     */
    public void printGameTilesLine(int lineNumber) {
        for (int j = 0; j < gameTiles.length; j++) {
            System.out.print(gameTiles[lineNumber][j].value + " ");
        }
    }

    /**
     * Вывод на экран матрицы игрового поля
     * ДЛЯ ТЕСТОВ
     * @param arr
     */
    public void printGameTiles(Tile[][] arr) {
        System.out.println("------");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                System.out.print(arr[i][j].value + " ");
            }
            System.out.println();
        }
    }

    /**
     * Размерность игрового поля (длина стороны квадратной матрицы)
     */
    private static final int FIELD_WIDTH = 4;

    /**
     * Матрица клеток игрового поля
     */
    private Tile[][] gameTiles;
    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    /**
     * Текущий счёт
     */
    int score;

    /**
     * Стек состояний игрового поля
     */
    Stack previousStates = new Stack<>();

    /**
     * Стек счётов
     */
    Stack previousScores = new Stack<>();

    /**
     * Максимальный вес плитки на игровом поле
     */
    int maxTile;


    boolean isSaveNeeded = true;
    private boolean createNewTile = false;

    /**
     *
     */
    void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        priorityQueue.add(getMoveEfficiency(this::up));
        priorityQueue.add(getMoveEfficiency(this::down));
        priorityQueue.add(getMoveEfficiency(this::left));
        priorityQueue.add(getMoveEfficiency(this::right));
        priorityQueue.peek().getMove().move();
    }

    /**
     * Сохраняет текущее состояние игры, для возможности отмены хода
     * @param tiles
     */
    private void saveState(Tile[][] tiles) {
        Tile[][] newTiles = cloneTileArr(tiles);
        previousStates.push(newTiles);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    /**
     * Устанавливает текущее игровое состояние равным последнему находящемуся в стеке
     */
    public void rollback() {
        if (!previousScores.isEmpty() && !previousStates.isEmpty()) {
            gameTiles = (Tile[][]) previousStates.pop();
            score = (int) previousScores.pop();
        }
    }

    /**
     * Сброс игрового поля
     */
    public Model() {
        resetGameTiles();
    }

    /**
     * Возвращает true в случае, если в текущей позиции возможно сделать ход так,
     * чтобы состояние игрового поля изменилось. Иначе - false.
     * @return
     */
    public boolean canMove2() {
        if (!getEmptyTiles().isEmpty()) return true;
        Tile[][] tempArr = cloneTileArr(gameTiles);

        int tempScore = score;
        int checkCreateNewTile = 0;

            left();
            gameTiles = cloneTileArr(tempArr);
            if (createNewTile) checkCreateNewTile++;

            up();
            gameTiles = cloneTileArr(tempArr);
            if (createNewTile) checkCreateNewTile++;

            down();
            gameTiles = cloneTileArr(tempArr);
            if (createNewTile) checkCreateNewTile++;

            right();
            gameTiles = cloneTileArr(tempArr);
            if (createNewTile) checkCreateNewTile++;

        score = tempScore;
        if (checkCreateNewTile > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Возвращает true в случае, если в текущей позиции возможно сделать ход так,
     * чтобы состояние игрового поля изменилось. Иначе - false.
     * @return
     */
    public boolean canMove() {
        if (!getEmptyTiles().isEmpty()) return true;
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length - 1; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j+1].value) return true;
            }
        }

        for (int i = 0; i < gameTiles.length-1; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                if (gameTiles[i][j].value == gameTiles[i+1][j].value) return true;
            }
        }
        return false;
    }


    /**
     * Создание дубликата матрицы игрового поля
     * @param inputArr
     * @return
     */
    private Tile[][] cloneTileArr(Tile[][] inputArr) {
        Tile[][] outputArr = new Tile[inputArr.length][inputArr.length];
        for (int i = 0; i < inputArr.length; i++) {
            for (int j = 0; j < inputArr.length; j++) {
                outputArr[i][j] = new Tile(inputArr[i][j].value);
            }
        }
        return outputArr;
    }



    /**
     * Сброс игры
     */
    void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                this.gameTiles[j][i] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    /**
     * Смотрит какие плитки пустуют и, если такие имеются,
     * меняет вес одной из них, выбранной случайным образом,
     * на 2 или 4 (на 9 двоек должна приходиться 1 четверка)
     */
    private void addTile() {
        List<Tile> emptyTile = getEmptyTiles();
        if (emptyTile.size() == 0) return; // Выход, если нет пустых клеток
        int randomTile = (int) (Math.random() * emptyTile.size()) % emptyTile.size();
        int randomTileValue = (Math.random() < 0.9 ? 2 : 4);
        emptyTile.get(randomTile).value = randomTileValue;
    }

    /**
     * Возвращает список пустых плиток в массиве gameTiles.
     * @return List<Tile>
     */
    private List<Tile> getEmptyTiles() {
        List<Tile> result = new ArrayList<>();
        for (Tile[] tilear : gameTiles) {
            for (Tile tile :tilear) {
                if (tile.value == 0) result.add(tile);
            }
        }
        return result;
    }

    /**
     * Сжатие плиток, таким образом, чтобы все пустые плитки были справа,
     * т.е. ряд {4, 2, 0, 4} становится рядом {4, 2, 4, 0}
     * {4, 0, 0, 2} становится рядом {4, 2, 0, 0}
     * @param tiles
     */
    private boolean compressTiles(Tile[] tiles) {
        boolean result = false;
        for (int i = 0; i < tiles.length -1; i++) {
            if ((tiles[i].value == 0) && (tiles[i+1].value != 0)) {
                tiles[i].value = tiles[i+1].value;
                tiles[i+1].value = 0;
                i = -1;
                result = true;
            }
        }
        return result;
    }

    /**
     * Слияние плиток одного номинала,
     * т.е. ряд {4, 4, 2, 0} становится рядом {8, 2, 0, 0}.
     *  ряд {4, 4, 4, 4} превратится в {8, 8, 0, 0}, а {4, 4, 4, 0} в {8, 4, 0, 0}
     * @param tiles
     */
    private boolean mergeTiles(Tile[] tiles) {
        boolean result = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].value != 0) {
                if (tiles[i].value == tiles[i + 1].value) {
                    tiles[i].value = tiles[i].value * 2;
                    tiles[i + 1].value = 0;
                    if (tiles[i].value > maxTile) maxTile = tiles[i].value;
                    score += tiles[i].value;
                    i = i + 1;
                    result = true;
                }
            }
        }
        new Model().compressTiles(tiles);
        return result;
    }


    /**
     * Сдвигает все клетки влево
     */
    public void left() {
        if (isSaveNeeded) saveState(gameTiles);
        createNewTile = false;
        for (int i = 0; i < gameTiles.length; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) createNewTile = true;
        }
        if (createNewTile) addTile();
        isSaveNeeded = true;
    }

    /**
     * Сдвигает все клетки вправо
     */
    public void up() {
        saveState(gameTiles);
        rotateRight();
        rotateRight();
        rotateRight();
        left();
        rotateRight();

    }

    /**
     * Сдвигает все клетки вниз
     */
    public void down() {
        saveState(gameTiles);
        rotateRight();
        left();
        rotateRight();
        rotateRight();
        rotateRight();

    }

    /**
     * Сдвигает все клетки вверх
     */
    public void right() {
        saveState(gameTiles);
        rotateRight();
        rotateRight();
        left();
        rotateRight();
        rotateRight();

    }

    /**
     * Поворачивает матрицу gameTiles по часовой стрелке на 90 градусов
     */
    public void rotateRight() {
        Tile[][] result = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                result[i][j] = gameTiles[FIELD_WIDTH-j-1][i];
            }
        }
        gameTiles = result;
    }

    /**
     * Выполнение случайного хода
     */
    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case (1):
                left();
                break;
            case (2):
                right();
                break;
            case (3):
                up();
                break;
            case (4):
                down();
                break;
        }
    }

    /**
     * Возвращает true, в случае, если вес плиток в массиве gameTiles отличается
     * от веса плиток в верхнем массиве стека previousStates.
     * Фактически идёт проверка того изменилось ли игровое поле после попытки сдвига
     */
    boolean hasBoardChanged(){
        Tile[][] tileFromstack = (Tile[][]) previousStates.peek();
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                if (gameTiles[i][j].value != tileFromstack[i][j].value) return true;
            }
        }
        return false;
    }

    /**
     * Возвращает объект типа MoveEfficiency описывающий эффективность переданного хода.
     * @param move
     * @return
     */
    MoveEfficiency getMoveEfficiency(Move move){

        move.move();
        int newNumberOfEmptyTiles = getEmptyTiles().size();
        int newScore = score;
        if (!hasBoardChanged()) {
            newNumberOfEmptyTiles = -1;
            newScore = 0;
        }
        rollback();
        return new MoveEfficiency(newNumberOfEmptyTiles,newScore, move);
    }



}