package com.bijiblast.engine;

import com.bijiblast.model.GameLevel;

public class ScoreManager {

    private int       score;
    private int       totalLinesCleared;
    private int       lastClearScore;
    private String    lastComboText;
    private GameLevel level;

    public ScoreManager(GameLevel level) {
        this.score             = 0;
        this.totalLinesCleared = 0;
        this.lastClearScore    = 0;
        this.lastComboText     = "";
        this.level             = level;
    }

    /**
     * Skor dari clear — semakin banyak baris/kolom sekaligus, semakin besar bonus COMBO.
     * 1 line  → normal (×1.0)
     * 2 lines → COMBO x2  (×1.5)
     * 3 lines → COMBO x3  (×2.0)
     * 4+lines → COMBO x4+ (×2.5)
     */
    public void addClearScore(int clearedCells, int linesCleared) {
        totalLinesCleared += linesCleared;

        double combo;
        if      (linesCleared >= 4) { combo = 2.5; lastComboText = "COMBO x" + linesCleared + "!  🔥🔥"; }
        else if (linesCleared == 3) { combo = 2.0; lastComboText = "COMBO x3!  🔥"; }
        else if (linesCleared == 2) { combo = 1.5; lastComboText = "COMBO x2!  ✨"; }
        else                        { combo = 1.0; lastComboText = ""; }

        lastClearScore = (int)(clearedCells * 50 * level.scoreMultiplier * combo);
        score         += lastClearScore;
    }

    public int    getScore()             { return score; }
    public int    getTotalLinesCleared() { return totalLinesCleared; }
    public int    getLastClearScore()    { return lastClearScore; }
    public String getLastComboText()     { return lastComboText; }

    public void resetScore() {
        score = 0; totalLinesCleared = 0; lastClearScore = 0; lastComboText = "";
    }

    public void printScore() { System.out.println("SCORE: " + score); }
}