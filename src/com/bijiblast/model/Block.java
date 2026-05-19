package com.bijiblast.model;

import java.util.Random;

public class Block {

    private int[][] shape;

    // Constructor manual
    public Block(int[][] shape) {
        this.shape = shape;
    }

    // Getter
    public int[][] getShape() {
        return shape;
    }

    // Ukuran tinggi block
    public int getHeight() {
        return shape.length;
    }

    // Ukuran lebar block
    public int getWidth() {
        return shape[0].length;
    }

    // Generate block random (simple version)
    public static Block randomBlock() {

        int[][][] shapes = new int[][][] {

                // Kotak
                {
                        {1, 1},
                        {1, 1}
                },

                // Garis
                {
                        {1, 1, 1, 1}
                },

                // L shape
                {
                        {1, 0},
                        {1, 0},
                        {1, 1}
                },

                // T shape
                {
                        {1, 1, 1},
                        {0, 1, 0}
                },

                // Single block
                {
                        {1}
                }
        };

        Random rand = new Random();
        return new Block(shapes[rand.nextInt(shapes.length)]);
    }

    // Debug print block
    public void printBlock() {

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                System.out.print(shape[i][j] + " ");
            }
            System.out.println();
        }
    }
}