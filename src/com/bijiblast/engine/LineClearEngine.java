package com.bijiblast.engine;

import com.bijiblast.model.Board;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class LineClearEngine {

    private int lastLinesCleared = 0;

    public int getLastLinesCleared() { return lastLinesCleared; }

    /**
     * Clear baris dan kolom yang penuh (tanpa syarat warna).
     * Mengembalikan list posisi sel yang dihapus dalam format int[] = {row, col, r, g, b}.
     */
    public List<int[]> clearLines(Board board) {
        lastLinesCleared = 0;

        Color[][] grid = board.getGrid();
        int       size = 8;

        List<int[]> cleared = new ArrayList<>();

        // =========================
        // CLEAR ROWS (cukup penuh saja)
        // =========================
        for (int row = 0; row < size; row++) {

            boolean full = true;

            for (int col = 0; col < size; col++) {
                if (grid[row][col] == null) { full = false; break; }
            }

            if (full) {
                lastLinesCleared++;
                for (int col = 0; col < size; col++) {
                    Color c = grid[row][col];
                    cleared.add(new int[]{
                        row, col,
                        c.getRed(), c.getGreen(), c.getBlue()
                    });
                    grid[row][col] = null;
                }
            }
        }

        // =========================
        // CLEAR COLUMNS (cukup penuh saja)
        // =========================
        for (int col = 0; col < size; col++) {

            boolean full = true;

            for (int row = 0; row < size; row++) {
                if (grid[row][col] == null) { full = false; break; }
            }

            if (full) {
                lastLinesCleared++;
                for (int row = 0; row < size; row++) {
                    if (grid[row][col] != null) {
                        Color c = grid[row][col];
                        cleared.add(new int[]{
                            row, col,
                            c.getRed(), c.getGreen(), c.getBlue()
                        });
                        grid[row][col] = null;
                    }
                }
            }
        }

        return cleared;
    }
}