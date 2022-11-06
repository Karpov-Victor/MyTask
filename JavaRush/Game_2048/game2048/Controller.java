package game2048;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private Model model;
    private View view;

    /**
     * определяет вес плитки при достижении которого игра будет считаться выигранной.
     */
    private static final int WINNING_TILE = 2048;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
    }

    public View getView() {
        return view;
    }

    /**
     * Сброс игры
     */
    public void resetGame() {
        view.isGameWon = false;
        view.isGameLost = false;
        model.score = 0;
        model.resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // https://jcup.ru/java/keylistener-swing-java
        if (e.getKeyCode()==KeyEvent.VK_ESCAPE) resetGame();
        if (!model.canMove()) view.isGameLost = true;
        if (!view.isGameLost && !view.isGameWon) {
            if (e.getKeyCode()==KeyEvent.VK_LEFT) model.left();
            if (e.getKeyCode()==KeyEvent.VK_RIGHT) model.right();
            if (e.getKeyCode()==KeyEvent.VK_UP) model.up();
            if (e.getKeyCode()==KeyEvent.VK_DOWN) model.down();
            if (e.getKeyCode()==KeyEvent.VK_Z) model.rollback();
            if (e.getKeyCode()==KeyEvent.VK_R) model.randomMove();
            if (e.getKeyCode()==KeyEvent.VK_A) model.autoMove();
        }
        if (model.maxTile == WINNING_TILE) view.isGameWon = true;
        //view.isGameWon = true;
        view.repaint();
    }
}
