package com.bijiblast.engine;

import com.bijiblast.model.Board;

import java.awt.Color;

public class LineClearEngine {

    // Clear full rows / columns
    public void clearLines(Board board) {

        Color[][] grid = board.getGrid();

        int size = 8;

        // =========================
        // CLEAR ROWS
        // =========================
        for (int row = 0; row < size; row++) {

            boolean full = true;

            for (int col = 0; col < size; col++) {

                if (grid[row][col] == null) {
                    full = false;
                    break;
                }
            }

            if (full) {

                for (int col = 0; col < size; col++) {
                    grid[row][col] = null;
                }
            }
        }

        // =========================
        // CLEAR COLUMNS
        // =========================
        for (int col = 0; col < size; col++) {

            boolean full = true;

            for (int row = 0; row < size; row++) {

                if (grid[row][col] == null) {
                    full = false;
                    break;
                }
            }

            if (full) {

                for (int row = 0; row < size; row++) {
                    grid[row][col] = null;
                }
            }
        }
    }
}