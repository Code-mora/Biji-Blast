package com.bijiblast.model;

import java.awt.Color;

public class Board {

    private final int ROWS = 8;
    private final int COLS = 8;

    private Color[][] grid;

    // Constructor
    public Board() {
        grid = new Color[ROWS][COLS];
    }

    // Getter grid
    public Color[][] getGrid() {
        return grid;
    }

    // Get cell
    public Color getCell(int row, int col) {
        return grid[row][col];
    }

    // Set cell
    public void setCell(int row, int col, Color value) {
        grid[row][col] = value;
    }

    // Reset board
    public void clearBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = null;
            }
        }
    }

    // Print board
    public void printBoard() {
        System.out.println("BOARD:");

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.print((grid[row][col] == null ? 0 : 1) + " ");
            }
            System.out.println();
        }
    }
}