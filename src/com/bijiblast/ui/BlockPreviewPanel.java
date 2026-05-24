package com.bijiblast.ui;

import com.bijiblast.model.Block;

import javax.swing.*;
import java.awt.*;

public class BlockPreviewPanel extends JPanel {

    private Block block;

    // SELECT + FLASH
    private boolean selected  = false;
    private float   flashAlpha = 0f;
    private javax.swing.Timer flashTimer;

    public BlockPreviewPanel(Block block) {
        this.block = block;
        setPreferredSize(new Dimension(120, 120));
        setOpaque(false);
        setToolTipText("Double-click untuk rotate ↩");
    }

    // =========================
    // SELECT EFFECT
    // =========================
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    // =========================
    // ROTATE FLASH EFFECT
    // =========================
    public void flashRotate() {
        flashAlpha = 0.75f;
        repaint();
        if (flashTimer != null && flashTimer.isRunning()) flashTimer.stop();
        flashTimer = new javax.swing.Timer(30, null);
        flashTimer.addActionListener(e -> {
            flashAlpha -= 0.08f;
            if (flashAlpha <= 0f) { flashAlpha = 0f; flashTimer.stop(); }
            repaint();
        });
        flashTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[][] shape   = block.getShape();
        int     rows    = shape.length;
        int     cols    = shape[0].length;
        int     maxDim  = Math.max(rows, cols);
        int     maxCell = selected ? 30 : 26;
        int     cellSize = Math.min(maxCell, (getWidth() - 20) / maxDim);

        int totalW  = cols * cellSize;
        int totalH  = rows * cellSize;
        int offsetX = (getWidth()  - totalW) / 2;
        int offsetY = (getHeight() - totalH) / 2;

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    int x = col * cellSize + offsetX;
                    int y = row * cellSize + offsetY;

                    // SHADOW
                    g2.setColor(new Color(0, 0, 0, 80));
                    g2.fillRoundRect(x + 4, y + 4, 24, 24, 10, 10);

                    // GLOW
                    g2.setColor(new Color(
                        block.getColor().getRed(),
                        block.getColor().getGreen(),
                        block.getColor().getBlue(), 80));
                    g2.fillRoundRect(x - 2, y - 2, 28, 28, 12, 12);

                    // MAIN BLOCK
                    g2.setPaint(new GradientPaint(
                        x, y, block.getColor().brighter(),
                        x + 24, y + 24, block.getColor().darker()));
                    g2.fillRoundRect(x, y, 24, 24, 10, 10);

                    // BORDER
                    g2.setColor(Color.WHITE);
                    g2.drawRoundRect(x, y, 24, 24, 10, 10);
                }
            }
        }

        // ---- SELECTED RING ----
        if (selected) {
            g2.setColor(new Color(255, 220, 50, 160));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 18, 18);
            g2.setStroke(new BasicStroke(1f));
        }

        // ---- ROTATE FLASH OVERLAY ----
        if (flashAlpha > 0f) {
            g2.setColor(new Color(180, 240, 255, (int)(flashAlpha * 255)));
            g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 18, 18);
        }
    }
}