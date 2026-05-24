package com.bijiblast.model;

import java.awt.Color;

/**
 * GameLevel — Enum tingkat kesulitan game.
 * Menentukan tipe block yang muncul dan multiplier skor.
 */
public enum GameLevel {

    EASY(
        1.0,
        "EASY",
        "Blok kecil & santai",
        new Color(80, 200, 100),
        new int[]{0, 4, 9, 10, 18, 5, 6}   // SQUARE, SINGLE, CORNER, LINE3, V, S, Z
    ),

    MEDIUM(
        1.5,
        "MEDIUM",
        "Semua jenis blok",
        new Color(255, 200, 0),
        new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,18}  // semua kecuali 3x3
    ),

    HARD(
        2.0,
        "HARD",
        "Blok besar, skor ×2",
        new Color(255, 80, 80),
        new int[]{1, 2, 3, 7, 8, 11, 13, 14, 15, 17}  // big shapes + 3x3
    );

    public final double scoreMultiplier;
    public final String label;
    public final String description;
    public final Color  color;
    public final int[]  allowedTypes;

    GameLevel(double scoreMultiplier, String label,
              String description, Color color, int[] allowedTypes) {

        this.scoreMultiplier = scoreMultiplier;
        this.label           = label;
        this.description     = description;
        this.color           = color;
        this.allowedTypes    = allowedTypes;
    }
}
