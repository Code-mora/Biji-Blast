package com.bijiblast.engine;

import com.bijiblast.model.Block;
import com.bijiblast.model.Board;

public class GameOverChecker {

    private final GameEngine engine;

    // Constructor
    public GameOverChecker() {
        engine = new GameEngine();
    }

    // Cek game over
    public boolean isGameOver(Board board, Block block) {

        // Coba semua posisi board
        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {

                // Kalau ada 1 tempat yang muat
                if (engine.canPlaceBlock(board, block, row, col)) {

                    return false;
                }
            }
        }

        // Tidak ada tempat sama sekali
        return true;
    }
}