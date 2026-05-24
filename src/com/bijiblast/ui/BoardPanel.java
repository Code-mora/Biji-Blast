package com.bijiblast.ui;

import com.bijiblast.model.Block;
import com.bijiblast.model.Board;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BoardPanel extends JPanel {

    private Board board;
    private final int CELL_SIZE = 50;

    // GHOST BLOCK (preview saat drag)
    private Block ghostBlock;
    private int   ghostRow = -1;
    private int   ghostCol = -1;

    // HINT (valid placement cells saat drag)
    private Block       hintBlock = null;
    private Set<String> hintCells = new HashSet<>();

    // EXPLOSION ANIMATION
    private final List<ExplodingCell> explosions = new ArrayList<>();
    private javax.swing.Timer animTimer;

    // FLOATING TEXT ANIMATION
    private final List<FloatingText> floatingTexts = new ArrayList<>();
    private javax.swing.Timer floatTimer;

    // ---- Inner classes ----

    private static class ExplodingCell {
        int row, col, r, g, b;
        float alpha = 1.0f;
        float scale = 2.0f;
        boolean flashing = true;
        int flashTick = 0;
        ExplodingCell(int row, int col, int r, int g, int b) {
            this.row=row; this.col=col; this.r=r; this.g=g; this.b=b;
        }
    }

    private static class FloatingText {
        String text;
        Color  color;
        float  x, y;
        float  alpha = 1.0f;
        int    size;
        FloatingText(String text, Color color, float x, float y, int size) {
            this.text=text; this.color=color; this.x=x; this.y=y; this.size=size;
        }
    }

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(8 * CELL_SIZE, 8 * CELL_SIZE));
        setOpaque(false);
    }

    // =========================
    // HINT BLOCK
    // =========================
    public void setHintBlock(Block block) {
        this.hintBlock = block;
        recomputeHintCells();
        repaint();
    }

    public void clearHintBlock() {
        this.hintBlock = null;
        hintCells.clear();
        repaint();
    }

    private void recomputeHintCells() {
        hintCells.clear();
        if (hintBlock == null) return;
        int[][] shape = hintBlock.getShape();
        for (int topRow = 0; topRow < 8; topRow++) {
            for (int topCol = 0; topCol < 8; topCol++) {
                if (canFitAt(shape, topRow, topCol)) {
                    for (int r = 0; r < shape.length; r++)
                        for (int c = 0; c < shape[r].length; c++)
                            if (shape[r][c] == 1)
                                hintCells.add((topRow + r) + "," + (topCol + c));
                }
            }
        }
    }

    private boolean canFitAt(int[][] shape, int topRow, int topCol) {
        for (int r = 0; r < shape.length; r++)
            for (int c = 0; c < shape[r].length; c++)
                if (shape[r][c] == 1) {
                    int br = topRow + r, bc = topCol + c;
                    if (br < 0 || br >= 8 || bc < 0 || bc >= 8) return false;
                    if (board.getCell(br, bc) != null) return false;
                }
        return true;
    }

    // =========================
    // FLOATING TEXT
    // =========================
    public void triggerFloatingText(String text, Color color, int sizePt) {
        synchronized (floatingTexts) {
            // Muncul di tengah papan, sedikit atas
            floatingTexts.add(new FloatingText(
                text, color,
                getWidth() / 2f,
                getHeight() / 4f - floatingTexts.size() * 30f,
                sizePt
            ));
        }
        if (floatTimer == null || !floatTimer.isRunning()) {
            floatTimer = new javax.swing.Timer(30, null);
            floatTimer.addActionListener(e -> {
                boolean any = false;
                synchronized (floatingTexts) {
                    Iterator<FloatingText> it = floatingTexts.iterator();
                    while (it.hasNext()) {
                        FloatingText ft = it.next();
                        ft.y     -= 1.2f;
                        ft.alpha -= 0.018f;
                        if (ft.alpha <= 0f) it.remove();
                        else any = true;
                    }
                }
                repaint();
                if (!any) floatTimer.stop();
            });
            floatTimer.start();
        }
    }

    // =========================
    // PAINT
    // =========================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // BACKGROUND
        g2.setPaint(new GradientPaint(0, 0, new Color(35, 35, 50),
                getWidth(), getHeight(), new Color(20, 20, 30)));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        // GRID
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                Color cell = board.getCell(row, col);

                if (cell == null) {
                    // HINT HIGHLIGHT
                    boolean isHint = hintCells.contains(row + "," + col);

                    g2.setColor(new Color(0, 0, 0, 70));
                    g2.fillRoundRect(x + 5, y + 5, 42, 42, 12, 12);

                    g2.setPaint(new GradientPaint(x, y,
                        isHint ? new Color(80, 160, 100) : new Color(70, 70, 90),
                        x + 42, y + 42,
                        isHint ? new Color(50, 120, 70) : new Color(45, 45, 60)));
                    g2.fillRoundRect(x + 3, y + 3, 42, 42, 12, 12);

                    g2.setColor(isHint
                        ? new Color(100, 255, 140, 90)
                        : new Color(255, 255, 255, 40));
                    g2.drawRoundRect(x + 3, y + 3, 42, 42, 12, 12);

                } else {
                    // GLOW
                    g2.setColor(new Color(cell.getRed(), cell.getGreen(), cell.getBlue(), 90));
                    g2.fillRoundRect(x, y, 46, 46, 14, 14);

                    // MAIN BLOCK
                    g2.setPaint(new GradientPaint(x, y, cell.brighter(),
                            x + 42, y + 42, cell.darker()));
                    g2.fillRoundRect(x + 3, y + 3, 42, 42, 12, 12);

                    // BORDER
                    g2.setColor(Color.WHITE);
                    g2.drawRoundRect(x + 3, y + 3, 42, 42, 12, 12);
                }
            }
        }

        // GHOST BLOCK
        if (ghostBlock != null && ghostRow >= 0 && ghostCol >= 0) {
            int[][] shape = ghostBlock.getShape();
            Color   color = ghostBlock.getColor();
            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] == 1) {
                        int r = ghostRow + row, c = ghostCol + col;
                        if (r >= 0 && r < 8 && c >= 0 && c < 8 && board.getCell(r, c) == null) {
                            int x = c * CELL_SIZE, y = r * CELL_SIZE;
                            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 110));
                            g2.fillRoundRect(x + 3, y + 3, 42, 42, 12, 12);
                            g2.setColor(new Color(255, 255, 255, 160));
                            g2.drawRoundRect(x + 3, y + 3, 42, 42, 12, 12);
                        }
                    }
                }
            }
        }

        // EXPLOSION PARTICLES
        synchronized (explosions) {
            for (ExplodingCell ec : explosions) {
                int cx   = ec.col * CELL_SIZE + CELL_SIZE / 2;
                int cy   = ec.row * CELL_SIZE + CELL_SIZE / 2;
                int size = (int)(CELL_SIZE * ec.scale);
                int half = size / 2;
                int a    = Math.max(0, Math.min(255, (int)(ec.alpha * 255)));

                if (ec.flashing) {
                    int fa = Math.max(0, 255 - ec.flashTick * 30);
                    g2.setColor(new Color(255, 255, 255, fa));
                    g2.fillRoundRect(ec.col * CELL_SIZE + 2, ec.row * CELL_SIZE + 2,
                            CELL_SIZE - 4, CELL_SIZE - 4, 10, 10);
                    g2.setColor(new Color(ec.r, ec.g, ec.b, fa));
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawRoundRect(ec.col * CELL_SIZE + 2, ec.row * CELL_SIZE + 2,
                            CELL_SIZE - 4, CELL_SIZE - 4, 10, 10);
                    g2.setStroke(new BasicStroke(1f));
                } else {
                    g2.setColor(new Color(ec.r, ec.g, ec.b, Math.min(a / 2, 100)));
                    g2.fillOval(cx - half - 6, cy - half - 6, size + 12, size + 12);
                    g2.setColor(new Color(ec.r, ec.g, ec.b, a));
                    g2.fillRoundRect(cx - half, cy - half, size, size, 12, 12);
                    int cf = Math.max(0, a - 100);
                    g2.setColor(new Color(255, 255, 255, cf));
                    g2.fillOval(cx - half / 3, cy - half / 3, size / 3, size / 3);
                }
            }
        }

        // FLOATING TEXTS
        synchronized (floatingTexts) {
            for (FloatingText ft : floatingTexts) {
                int a = Math.max(0, Math.min(255, (int)(ft.alpha * 255)));
                // Shadow
                g2.setFont(new Font("Arial", Font.BOLD, ft.size));
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(ft.text);
                g2.setColor(new Color(0, 0, 0, a / 2));
                g2.drawString(ft.text, (int)(ft.x - tw / 2f) + 2, (int)ft.y + 2);
                // Main text
                g2.setColor(new Color(ft.color.getRed(), ft.color.getGreen(), ft.color.getBlue(), a));
                g2.drawString(ft.text, (int)(ft.x - tw / 2f), (int)ft.y);
            }
        }
    }

    // =========================
    // TRIGGER EXPLOSION
    // =========================
    public void triggerExplosion(List<int[]> clearedCells) {
        synchronized (explosions) {
            for (int[] cell : clearedCells)
                explosions.add(new ExplodingCell(cell[0], cell[1], cell[2], cell[3], cell[4]));
        }
        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
        animTimer = new javax.swing.Timer(30, null);
        animTimer.addActionListener(e -> {
            boolean anyAlive = false;
            synchronized (explosions) {
                Iterator<ExplodingCell> it = explosions.iterator();
                while (it.hasNext()) {
                    ExplodingCell ec = it.next();
                    if (ec.flashing) {
                        ec.flashTick++;
                        if (ec.flashTick >= 6) ec.flashing = false;
                        anyAlive = true;
                    } else {
                        ec.alpha -= 0.04f;
                        ec.scale -= 0.07f;
                        if (ec.alpha <= 0f) it.remove();
                        else anyAlive = true;
                    }
                }
            }
            repaint();
            if (!anyAlive) animTimer.stop();
        });
        animTimer.start();
    }

    // =========================
    // SET / CLEAR GHOST
    // =========================
    public void setGhost(Block block, int row, int col) {
        this.ghostBlock = block; this.ghostRow = row; this.ghostCol = col;
        repaint();
    }

    public void clearGhost() {
        this.ghostBlock = null; this.ghostRow = -1; this.ghostCol = -1;
        repaint();
    }
}