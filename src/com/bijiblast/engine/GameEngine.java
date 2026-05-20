package com.bijiblast.engine;

import com.bijiblast.model.Block;
import com.bijiblast.model.Board;

public class GameEngine {

    public boolean canPlaceBlock(Board board, Block block, int startRow, int startCol) {

        int[][] shape = block.getShape();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 1) {

                    int r = startRow + row;
                    int c = startCol + col;

                    if (r >= 8 || c >= 8) return false;

                    if (board.getCell(r, c) != null) return false;
                }
            }
        }

        return true;
    }

    public void placeBlock(Board board, Block block, int startRow, int startCol) {

        int[][] shape = block.getShape();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 1) {

                    int r = startRow + row;
                    int c = startCol + col;

                    board.setCell(r, c, block.getColor());
                }
            }
        }
    }
}