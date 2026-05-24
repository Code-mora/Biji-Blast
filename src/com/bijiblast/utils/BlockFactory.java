package com.bijiblast.utils;

import com.bijiblast.model.Block;
import com.bijiblast.model.GameLevel;

import java.awt.Color;
import java.util.Random;

public class BlockFactory {

    // ---- tipe lama ----
    public static final int TYPE_SQUARE   =  0;
    public static final int TYPE_LINE     =  1;
    public static final int TYPE_L_SHAPE  =  2;
    public static final int TYPE_T_SHAPE  =  3;
    public static final int TYPE_SINGLE   =  4;
    public static final int TYPE_S_SHAPE  =  5;
    public static final int TYPE_Z_SHAPE  =  6;
    public static final int TYPE_J_SHAPE  =  7;
    public static final int TYPE_PLUS     =  8;
    public static final int TYPE_CORNER   =  9;

    // ---- tipe baru ----
    public static final int TYPE_LINE3    = 10;  // garis 3 sel
    public static final int TYPE_U_SHAPE  = 11;  // bentuk U
    public static final int TYPE_STAIR3   = 12;  // tangga 3 blok
    public static final int TYPE_BIG_L    = 13;  // L besar (4-tinggi)
    public static final int TYPE_BIG_T    = 14;  // T lebar (3x3)
    public static final int TYPE_S_BIG    = 15;  // S panjang
    public static final int TYPE_DIAG3    = 16;  // diagonal 3
    public static final int TYPE_SQUARE3  = 17;  // kotak 3x3
    public static final int TYPE_V_SHAPE  = 18;  // sudut V (L kecil mirror)

    private static final int TOTAL_TYPES = 19;

    private static final Color[] COLORS = {
        new Color(255, 215,   0),   //  0 SQUARE
        new Color(  0, 220, 255),   //  1 LINE
        new Color(  0, 230, 120),   //  2 L
        new Color(255, 105, 180),   //  3 T
        new Color(255, 120,   0),   //  4 SINGLE
        new Color( 80, 200, 255),   //  5 S
        new Color(200,  80, 255),   //  6 Z
        new Color(255, 165,   0),   //  7 J
        new Color(255,  80,  80),   //  8 PLUS
        new Color(120, 255, 180),   //  9 CORNER
        new Color(150, 255, 100),   // 10 LINE3
        new Color( 80, 180, 255),   // 11 U
        new Color(255, 200,  60),   // 12 STAIR3
        new Color(255,  60, 140),   // 13 BIG_L
        new Color( 60, 220, 200),   // 14 BIG_T
        new Color(180,  80, 255),   // 15 S_BIG
        new Color(255, 140,  60),   // 16 DIAG3
        new Color(255,  80, 200),   // 17 SQUARE3
        new Color( 60, 255, 160),   // 18 V_SHAPE
    };

    private static final int[][][] SHAPES = {
        // 0  SQUARE 2x2
        {{1,1},{1,1}},
        // 1  LINE 1x4
        {{1,1,1,1}},
        // 2  L-shape
        {{1,0},{1,0},{1,1}},
        // 3  T-shape
        {{1,1,1},{0,1,0}},
        // 4  SINGLE
        {{1}},
        // 5  S
        {{0,1,1},{1,1,0}},
        // 6  Z
        {{1,1,0},{0,1,1}},
        // 7  J
        {{0,1},{0,1},{1,1}},
        // 8  PLUS
        {{0,1,0},{1,1,1},{0,1,0}},
        // 9  CORNER 2x2
        {{1,0},{1,1}},
        // 10 LINE3 — garis 3 sel horizontal
        {{1,1,1}},
        // 11 U-shape (3x2)
        {{1,0,1},{1,1,1}},
        // 12 STAIR-3 (tangga 3 blok)
        {{1,0,0},{1,1,0},{0,1,1}},
        // 13 BIG-L (4-tinggi)
        {{1,0},{1,0},{1,0},{1,1}},
        // 14 BIG-T (3x3)
        {{1,1,1},{0,1,0},{0,1,0}},
        // 15 S-BIG (3 wide)
        {{0,1,1,0},{1,1,0,0}},
        // 16 DIAGONAL-3
        {{1,0,0},{0,1,0},{0,0,1}},
        // 17 SQUARE-3 (3x3 penuh)
        {{1,1,1},{1,1,1},{1,1,1}},
        // 18 V-SHAPE (sudut kanan bawah)
        {{1,1},{1,0}},
    };

    public static Block createBlock(int type) {
        if (type < 0 || type >= TOTAL_TYPES) type = TYPE_SINGLE;
        int[][] orig = SHAPES[type];
        int[][] copy = new int[orig.length][];
        for (int i = 0; i < orig.length; i++) copy[i] = orig[i].clone();
        return new Block(copy, COLORS[type]);
    }

    public static Block createRandomBlock() {
        return createBlock(new Random().nextInt(TOTAL_TYPES));
    }

    /** Buat block random sesuai level */
    public static Block createRandomBlockForLevel(GameLevel level) {
        int[] allowed = level.allowedTypes;
        int   type    = allowed[new Random().nextInt(allowed.length)];
        return createBlock(type);
    }

    /** Buat 3 block unik sesuai level */
    public static Block[] createThreeUniqueBlocksForLevel(GameLevel level) {
        Random  rand    = new Random();
        int[]   allowed = level.allowedTypes;
        Block[] blocks  = new Block[3];
        int[]   used    = {-1, -1, -1};

        for (int i = 0; i < 3; i++) {
            int type;
            int tries = 0;
            do {
                type = allowed[rand.nextInt(allowed.length)];
                tries++;
            } while (isUsed(used, type, i) && tries < 30);
            used[i]   = type;
            blocks[i] = createBlock(type);
        }
        return blocks;
    }

    /** @deprecated Gunakan createThreeUniqueBlocksForLevel */
    public static Block[] createThreeUniqueBlocks() {
        return createThreeUniqueBlocksForLevel(GameLevel.MEDIUM);
    }

    private static boolean isUsed(int[] used, int type, int upTo) {
        for (int i = 0; i < upTo; i++) if (used[i] == type) return true;
        return false;
    }

    public static int getTotalTypes() { return TOTAL_TYPES; }
}
