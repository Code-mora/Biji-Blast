package com.bijiblast.model;

import java.awt.Color;
import java.util.Random;

public class Block {

    private int[][] shape;

    private Color color;

    // =========================
    // CONSTRUCTOR
    // =========================
    public Block(int[][] shape, Color color) {

        this.shape = shape;

        this.color = color;
    }

    // =========================
    // GET SHAPE
    // =========================
    public int[][] getShape() {

        return shape;
    }

    // =========================
    // GET COLOR
    // =========================
    public Color getColor() {

        return color;
    }

    // =========================
    // HEIGHT
    // =========================
    public int getHeight() {

        return shape.length;
    }

    // =========================
    // WIDTH
    // =========================
    public int getWidth() {

        return shape[0].length;
    }

    // =========================
    // ROTATE
    // =========================
    public void rotate() {

        int rows = shape.length;

        int cols = shape[0].length;

        int[][] rotated =
                new int[cols][rows];

        for (int row = 0;
             row < rows;
             row++) {

            for (int col = 0;
                 col < cols;
                 col++) {

                rotated[col][rows - 1 - row]
                        = shape[row][col];
            }
        }

        shape = rotated;
    }

    // =========================
    // RANDOM BLOCK
    // =========================
    public static Block randomBlock() {

        Random rand = new Random();

        int randomShape =
                rand.nextInt(5);

        switch (randomShape) {

            // KOTAK
            case 0:
                return new Block(
                        new int[][]{
                                {1, 1},
                                {1, 1}
                        },
                        new Color(255, 215, 0)
                );

            // GARIS
            case 1:
                return new Block(
                        new int[][]{
                                {1, 1, 1, 1}
                        },
                        new Color(0, 255, 255)
                );

            // L
            case 2:
                return new Block(
                        new int[][]{
                                {1, 0},
                                {1, 0},
                                {1, 1}
                        },
                        new Color(0, 255, 120)
                );

            // T
            case 3:
                return new Block(
                        new int[][]{
                                {1, 1, 1},
                                {0, 1, 0}
                        },
                        new Color(255, 105, 180)
                );

            // SINGLE
            default:
                return new Block(
                        new int[][]{
                                {1}
                        },
                        new Color(255, 120, 0)
                );
        }
    }

    // =========================
    // DEBUG
    // =========================
    public void printBlock() {

        for (int row = 0;
             row < shape.length;
             row++) {

            for (int col = 0;
                 col < shape[row].length;
                 col++) {

                System.out.print(
                        shape[row][col] + " "
                );
            }

            System.out.println();
        }
    }
}