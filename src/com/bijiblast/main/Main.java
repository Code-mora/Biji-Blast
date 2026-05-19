package com.bijiblast.main;

import com.bijiblast.engine.GameManager;
import com.bijiblast.model.Block;

public class Main {

    public static void main(String[] args) {

        GameManager gameManager = new GameManager();

        // Block kotak
        int[][] shape = {
                {1, 1},
                {1, 1}
        };

         // =========================
        // BLOCK L
        // =========================
        int[][] lShape = {
                {1, 0},
                {1, 0},
                {1, 1}
        };


        Block block = new Block(shape);

        Block lBlock = new Block(lShape);


        // Main taro blok
        gameManager.playMove(block, 0, 0);
        gameManager.playMove(lBlock, 5, 5);

        // Print board
        gameManager.getBoard().printBoard();

        // Print score
        System.out.println("SCORE: " + gameManager.getScore());

        // Cek game over
        System.out.println("GAME OVER: " +
                gameManager.isGameOver(block));
    }
}