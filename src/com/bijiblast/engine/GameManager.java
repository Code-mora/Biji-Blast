package com.bijiblast.engine;

import com.bijiblast.model.Block;
import com.bijiblast.model.Board;
import com.bijiblast.model.GameLevel;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private final Board           board;
    private final GameEngine      gameEngine;
    private final LineClearEngine lineClearEngine;
    private final ScoreManager    scoreManager;
    private final GameOverChecker gameOverChecker;
    private final GameLevel       level;

    private List<int[]> lastClearedCells = new ArrayList<>();

    public GameManager(GameLevel level) {
        this.level       = level;
        board            = new Board();
        gameEngine       = new GameEngine();
        lineClearEngine  = new LineClearEngine();
        scoreManager     = new ScoreManager(level);
        gameOverChecker  = new GameOverChecker();
    }

    /** Main logic saat player naruh block */
    public boolean playMove(Block block, int row, int col) {
        if (!gameEngine.canPlaceBlock(board, block, row, col)) {
            System.out.println("BLOCK TIDAK BISA DITARUH");
            return false;
        }

        gameEngine.placeBlock(board, block, row, col);
        lastClearedCells = lineClearEngine.clearLines(board);

        int linesCleared = lineClearEngine.getLastLinesCleared();
        if (!lastClearedCells.isEmpty()) {
            scoreManager.addClearScore(lastClearedCells.size(), linesCleared);
        }

        return true;
    }

    public boolean isGameOver(Block block) {
        return gameOverChecker.isGameOver(board, block);
    }

    /** Cek apakah block bisa ditempatkan di posisi tertentu (untuk hint) */
    public boolean canPlace(Block block, int row, int col) {
        return gameEngine.canPlaceBlock(board, block, row, col);
    }

    public Board       getBoard()              { return board; }
    public int         getScore()              { return scoreManager.getScore(); }
    public int         getTotalLinesCleared()  { return scoreManager.getTotalLinesCleared(); }
    public int         getLastClearScore()     { return scoreManager.getLastClearScore(); }
    public String      getLastComboText()      { return scoreManager.getLastComboText(); }
    public GameLevel   getLevel()              { return level; }
    public List<int[]> getLastClearedCells()   { return lastClearedCells; }
}