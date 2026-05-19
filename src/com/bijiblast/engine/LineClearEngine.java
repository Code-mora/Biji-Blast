package com.bijiblast.engine;

import com.bijiblast.model.Board;

public class LineClearEngine {

    // Clear line penuh
    public void clearLines(Board board) {

        int[][] grid = board.getGrid();

        // =========================
        // CEK BARIS
        // =========================
        for (int row = 0; row < 8; row++) {

            boolean fullRow = true;

            for (int col = 0; col < 8; col++) {

                if (grid[row][col] == 0) {
                    fullRow = false;
                    break;
                }
            }

            // Kalau penuh -> hapus
            if (fullRow) {

                for (int col = 0; col < 8; col++) {
                    grid[row][col] = 0;
                }

                System.out.println("BARIS " + row + " DIHAPUS");
            }
        }

        // =========================
        // CEK KOLOM
        // =========================
        for (int col = 0; col < 8; col++) {

            boolean fullCol = true;

            for (int row = 0; row < 8; row++) {

                if (grid[row][col] == 0) {
                    fullCol = false;
                    break;
                }
            }

            // Kalau penuh -> hapus
            if (fullCol) {

                for (int row = 0; row < 8; row++) {
                    grid[row][col] = 0;
                }

                System.out.println("KOLOM " + col + " DIHAPUS");
            }
        }
    }
}