package com.bijiblast.engine;

public class ScoreManager {

    private int score;

    // Constructor
    public ScoreManager() {
        score = 0;
    }

    // Tambah score
    public void addScore(int points) {
        score += points;
    }

    // Ambil score
    public int getScore() {
        return score;
    }

    // Reset score
    public void resetScore() {
        score = 0;
    }

    // Print score
    public void printScore() {
        System.out.println("SCORE: " + score);
    }
}