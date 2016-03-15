package hu.ait.android.minesweeper.model;

import hu.ait.android.minesweeper.view.MinesweeperView;

/**
 * Created by joe on 9/24/15.
 */
public class MinesweeperModel {

    private static MinesweeperModel instance = null;

    private MinesweeperModel() {
    }

    public static MinesweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    public static final short EMPTY = 0;
    public static final short BOMB = -1;
    public static final short NUMBOMBS = 3;

    private boolean flagOn = false;
    private short correctFlags = 0;
    private boolean gameOver = false;


    private short[][] model = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY}
    };

    private boolean[][] clicked = {
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false}
    };

    private boolean[][] flagged = {
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false}
    };

    public void resetModel() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                model[i][j] = EMPTY;
                clicked[i][j] = false;
                flagged[i][j] = false;
            }
        }
        placeBombs(NUMBOMBS);
        setNumberSquares();
        correctFlags = 0;
        gameOver = false;
        flagOn = false;
    }

    public short getFieldContent(int x, int y) {
        return model[x][y];
    }

    public boolean setClickedContent(int x, int y) {
        return clicked[x][y] = true;
    }


    public boolean getClickedContent(int x, int y) {
        return clicked[x][y];
    }

    public boolean getFlaggedContent(int x, int y) {
        return flagged[x][y];
    }

    public boolean setFlaggedContent(int x, int y) {
        return flagged[x][y] = true;
    }

    private void placeBombs(short amnt) {
        for (int i = 0; i < amnt; i++) {
            int x = (short) Math.floor(Math.random() * 5);
            int y = (short) Math.floor(Math.random() * 5);
            if (model[x][y] == EMPTY) {
                model[x][y] = BOMB;
            } else {
                i--;
            }
        }
    }

    private void checkandSet(int x, int y) {
        if (x >= 0 && x < 5 && y >= 0 && y < 5 && model[x][y] != BOMB) {
            model[x][y]++;
        }
    }

    private void setNumberSquares() {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (model[x][y] == BOMB) {
                    checkandSet(x + 1, y);
                    checkandSet(x + 1, y + 1);
                    checkandSet(x, y + 1);
                    checkandSet(x - 1, y);
                    checkandSet(x - 1, y - 1);
                    checkandSet(x, y - 1);
                    checkandSet(x - 1, y + 1);
                    checkandSet(x + 1, y - 1);
                }
            }
        }
    }

    public void increaseCorrectFlags() {
        correctFlags++;
    }

    public void decideWinner() {
        if (correctFlags == NUMBOMBS) {
            gameOver = true;
        } else {
            gameOver = false;
        }
    }

    public void setGameOver() {
        gameOver = true;
    }

    public void toggleFlags() {

        if (flagOn == true) {
            flagOn = false;
        } else {
            flagOn = true;
        }
    }

    public boolean getFlagStatus() {
        return flagOn;
    }

    public boolean getGameOver() {
        return gameOver;
    }


    public void dealWithZeros(int x, int y) {
        checkandSetZero(x + 1, y);
        checkandSetZero(x + 1, y + 1);
        checkandSetZero(x, y + 1);
        checkandSetZero(x - 1, y);
        checkandSetZero(x - 1, y - 1);
        checkandSetZero(x, y - 1);
        checkandSetZero(x - 1, y + 1);
        checkandSetZero(x + 1, y - 1);
    }

    private void checkandSetZero(int x, int y) {
        if (x >= 0 && x < 5 && y >= 0 && y < 5 && !clicked[x][y]) {
            clicked[x][y] = true;
            if (model[x][y] == EMPTY) {
                dealWithZeros(x, y);
            }
        }
    }
}
