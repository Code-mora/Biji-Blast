package com.bijiblast.engine;

import com.bijiblast.model.Block;
import com.bijiblast.model.Board;

public class GameEngine {

    // Cek apakah block bisa ditaruh
    public boolean canPlaceBlock(Board board, Block block, int startRow, int startCol) {

        int[][] grid = board.getGrid();
        int[][] shape = block.getShape();

        for (int row = 0; row < shape.length; row++) {

            for (int col = 0; col < shape[row].length; col++) {

                // Kalau bagian block = 1
                if (shape[row][col] == 1) {

                    int boardRow = startRow + row;
                    int boardCol = startCol + col;

                    // Cek keluar board
                    if (boardRow >= 8 || boardCol >= 8) {
                        return false;
                    }

                    // Cek tabrakan
                    if (grid[boardRow][boardCol] == 1) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    // Menaruh block ke board
public void placeBlock(Board board, Block block, int startRow, int startCol) {

    int[][] grid = board.getGrid();
    int[][] shape = block.getShape();

    for (int row = 0; row < shape.length; row++) {

        for (int col = 0; col < shape[row].length; col++) {

            // Kalau bagian block = 1
            if (shape[row][col] == 1) {

                int boardRow = startRow + row;
                int boardCol = startCol + col;

                grid[boardRow][boardCol] = 1;
            }
        }
    }
}
}