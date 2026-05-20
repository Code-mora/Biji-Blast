package com.bijiblast.ui;

import com.bijiblast.model.Board;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private Board board;

    private final int CELL_SIZE = 50;

    public BoardPanel(Board board) {

        this.board = board;

        setPreferredSize(
                new Dimension(
                        8 * CELL_SIZE,
                        8 * CELL_SIZE
                )
        );

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // ANTI ALIAS
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // =========================
        // BACKGROUND BOARD
        // =========================
        GradientPaint backgroundGradient =
                new GradientPaint(
                        0, 0,
                        new Color(35, 35, 50),
                        getWidth(), getHeight(),
                        new Color(20, 20, 30)
                );

        g2.setPaint(backgroundGradient);

        g2.fillRoundRect(
                0, 0,
                getWidth(), getHeight(),
                25, 25
        );

        // =========================
        // DRAW GRID
        // =========================
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;

                Color cell = board.getCell(row, col);

                // =========================
                // EMPTY CELL
                // =========================
                if (cell == null) {

                    g2.setColor(new Color(0, 0, 0, 70));

                    g2.fillRoundRect(
                            x + 5, y + 5,
                            42, 42,
                            12, 12
                    );

                    GradientPaint emptyGradient =
                            new GradientPaint(
                                    x, y,
                                    new Color(70, 70, 90),
                                    x + 42, y + 42,
                                    new Color(45, 45, 60)
                            );

                    g2.setPaint(emptyGradient);

                    g2.fillRoundRect(
                            x + 3, y + 3,
                            42, 42,
                            12, 12
                    );

                    g2.setColor(new Color(255, 255, 255, 40));

                    g2.drawRoundRect(
                            x + 3, y + 3,
                            42, 42,
                            12, 12
                    );
                }

                // =========================
                // FILLED CELL (COLOR BLOCK)
                // =========================
                else {

                    // GLOW
                    g2.setColor(
                            new Color(
                                    cell.getRed(),
                                    cell.getGreen(),
                                    cell.getBlue(),
                                    90
                            )
                    );

                    g2.fillRoundRect(
                            x, y,
                            46, 46,
                            14, 14
                    );

                    // MAIN BLOCK (GRADIENT)
                    GradientPaint filledGradient =
                            new GradientPaint(
                                    x, y,
                                    cell.brighter(),
                                    x + 42, y + 42,
                                    cell.darker()
                            );

                    g2.setPaint(filledGradient);

                    g2.fillRoundRect(
                            x + 3, y + 3,
                            42, 42,
                            12, 12
                    );

                    // BORDER
                    g2.setColor(Color.WHITE);

                    g2.drawRoundRect(
                            x + 3, y + 3,
                            42, 42,
                            12, 12
                    );
                }
            }
        }
    }
}