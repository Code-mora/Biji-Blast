package com.bijiblast.ui;

import com.bijiblast.model.Block;

import javax.swing.*;
import java.awt.*;

/**
 * DragGlassPane — Panel transparan yang menggambar block
 * mengikuti cursor saat player melakukan drag dari slot preview.
 *
 * Dipasang sebagai glassPane di GameFrame agar bisa render
 * di atas semua komponen lain.
 */
public class DragGlassPane extends JPanel {

    // =========================
    // FIELD
    // =========================
    private Block  dragBlock;
    private Point  dragPoint;
    private boolean dragging = false;

    // Ukuran cell saat drag (lebih kecil dari board, supaya pas di tangan)
    private static final int DRAG_CELL = 32;

    // =========================
    // CONSTRUCTOR
    // =========================
    public DragGlassPane() {

        setOpaque(false);

        setVisible(false);
    }

    // =========================
    // START DRAG
    // =========================

    /**
     * Mulai drag — aktifkan glass pane dan simpan block + posisi awal.
     *
     * @param block      block yang sedang di-drag
     * @param framePoint posisi mouse dalam koordinat frame
     */
    public void startDrag(Block block, Point framePoint) {

        this.dragBlock = block;
        this.dragPoint = framePoint;
        this.dragging  = true;

        setVisible(true);

        repaint();
    }

    // =========================
    // UPDATE POSISI
    // =========================

    /**
     * Perbarui posisi drag mengikuti mouse.
     *
     * @param framePoint posisi mouse terkini dalam koordinat frame
     */
    public void updatePosition(Point framePoint) {

        this.dragPoint = framePoint;

        repaint();
    }

    // =========================
    // STOP DRAG
    // =========================

    /**
     * Hentikan drag — sembunyikan glass pane dan bersihkan state.
     */
    public void stopDrag() {

        this.dragging  = false;
        this.dragBlock = null;
        this.dragPoint = null;

        setVisible(false);

        repaint();
    }

    // =========================
    // PAINT
    // =========================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (!dragging || dragBlock == null || dragPoint == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int[][] shape = dragBlock.getShape();
        Color   color = dragBlock.getColor();

        // Pusatkan block di kursor
        int blockW  = shape[0].length * DRAG_CELL;
        int blockH  = shape.length    * DRAG_CELL;
        int startX  = dragPoint.x - blockW / 2;
        int startY  = dragPoint.y - blockH / 2;

        for (int row = 0; row < shape.length; row++) {

            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 1) {

                    int x = startX + col * DRAG_CELL;
                    int y = startY + row * DRAG_CELL;

                    // GLOW
                    g2.setColor(new Color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            70
                    ));
                    g2.fillRoundRect(x - 3, y - 3,
                            DRAG_CELL + 6, DRAG_CELL + 6,
                            12, 12);

                    // GRADIENT MAIN BLOCK (semi transparan)
                    GradientPaint gp = new GradientPaint(
                            x, y, color.brighter(),
                            x + DRAG_CELL, y + DRAG_CELL, color.darker()
                    );
                    g2.setPaint(gp);
                    g2.fillRoundRect(x, y,
                            DRAG_CELL - 2, DRAG_CELL - 2,
                            9, 9);

                    // BORDER
                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.drawRoundRect(x, y,
                            DRAG_CELL - 2, DRAG_CELL - 2,
                            9, 9);
                }
            }
        }
    }
}
