package com.bijiblast.ui;

import com.bijiblast.model.Block;

import javax.swing.*;
import java.awt.*;

public class BlockPreviewPanel extends JPanel {

    private Block block;

    // STATUS SELECT
    private boolean selected = false;

    public BlockPreviewPanel(Block block) {

        this.block = block;

        setPreferredSize(
                new Dimension(120, 120)
        );

        setOpaque(false);
    }

    // =========================
    // SELECT EFFECT
    // =========================
    public void setSelected(boolean selected) {

        this.selected = selected;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;

        // SMOOTH
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int[][] shape = block.getShape();

        // ANIMASI SIZE
        int cellSize =
                selected ? 32 : 28;

        int offset =
                selected ? 10 : 20;

        for (int row = 0;
             row < shape.length;
             row++) {

            for (int col = 0;
                 col < shape[row].length;
                 col++) {

                if (shape[row][col] == 1) {

                    int x =
                            col * cellSize + offset;

                    int y =
                            row * cellSize + offset;

                    // =========================
                    // SHADOW
                    // =========================
                    g2.setColor(
                            new Color(0, 0, 0, 80)
                    );

                    g2.fillRoundRect(
                            x + 4,
                            y + 4,
                            24,
                            24,
                            10,
                            10
                    );

                    // =========================
                    // GLOW
                    // =========================
                    g2.setColor(
                            new Color(
                                    block.getColor().getRed(),
                                    block.getColor().getGreen(),
                                    block.getColor().getBlue(),
                                    80
                            )
                    );

                    g2.fillRoundRect(
                            x - 2,
                            y - 2,
                            28,
                            28,
                            12,
                            12
                    );

                    // =========================
                    // MAIN BLOCK
                    // =========================
                    GradientPaint gradient =
                            new GradientPaint(
                                    x,
                                    y,
                                    block.getColor().brighter(),
                                    x + 24,
                                    y + 24,
                                    block.getColor().darker()
                            );

                    g2.setPaint(gradient);

                    g2.fillRoundRect(
                            x,
                            y,
                            24,
                            24,
                            10,
                            10
                    );

                    // =========================
                    // BORDER
                    // =========================
                    g2.setColor(Color.WHITE);

                    g2.drawRoundRect(
                            x,
                            y,
                            24,
                            24,
                            10,
                            10
                    );
                }
            }
        }
    }
}