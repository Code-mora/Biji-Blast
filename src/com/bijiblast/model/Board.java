package com.bijiblast.model;

public class Board {

    private final int ROWS = 8;
    private final int COLS = 8;

    private int[][] grid;

    // Constructor
    public Board() {
        grid = new int[ROWS][COLS];
    }

    // Getter board
    public int[][] getGrid() {
        return grid;
    }

    // Cek isi cell
    public int getCell(int row, int col) {
        return grid[row][col];
    }

    // Set isi cell
    public void setCell(int row, int col, int value) {
        grid[row][col] = value;
    }

    // Reset board
    public void clearBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = 0;
            }
        }
    }

    // Print board ke console
    public void printBoard() {

        System.out.println("BOARD:");

        for (int row = 0; row < ROWS; row++) {

            for (int col = 0; col < COLS; col++) {
                System.out.print(grid[row][col] + " ");
            }

            System.out.println();
        }
    }
}