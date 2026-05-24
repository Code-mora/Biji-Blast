package com.bijiblast.ui;

import com.bijiblast.engine.BlockController;
import com.bijiblast.engine.GameManager;
import com.bijiblast.input.InputHandler;
import com.bijiblast.model.Block;
import com.bijiblast.model.GameLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.prefs.Preferences;

public class GamePanel extends JPanel
        implements InputHandler.InputListener,
                   BlockController.BlockControllerListener {

    private GameManager     gameManager;
    private BlockController blockController;
    private BoardPanel      boardPanel;
    private GameFrame       frame;
    private DragGlassPane   dragGlassPane;

    private BlockPreviewPanel preview1, preview2, preview3;
    private JPanel            bottomPanel;

    // ---- UI Labels ----
    private JLabel scoreLabel;
    private JLabel highScoreLabel;
    private JLabel linesLabel;

    // ---- Preferences ----
    private static final String PREF_TUTORIAL  = "tutorial_shown_v1";
    private static final String PREF_HIGHSCORE = "high_score";
    private int highScore;

    // ---- Score pulse timer ----
    private javax.swing.Timer pulseTimer;
    private int               pulseTick = 0;

    public GamePanel(GameFrame frame, GameLevel level) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));

        dragGlassPane = new DragGlassPane();
        frame.setGlassPane(dragGlassPane);

        gameManager     = new GameManager(level);
        blockController = new BlockController(gameManager, level);
        blockController.setListener(this);

        // Load high score
        Preferences prefs = Preferences.userNodeForPackage(GamePanel.class);
        highScore = prefs.getInt(PREF_HIGHSCORE, 0);

        // ---- SCORE ----
        scoreLabel = new JLabel("SCORE : 0");
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 26));

        // ---- LINES CLEARED ----
        linesLabel = new JLabel("Lines: 0");
        linesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        linesLabel.setForeground(new Color(180, 180, 200));
        linesLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel scoreStack = new JPanel();
        scoreStack.setLayout(new BoxLayout(scoreStack, BoxLayout.Y_AXIS));
        scoreStack.setBackground(new Color(20, 20, 30));
        scoreStack.setBorder(BorderFactory.createEmptyBorder(14, 0, 10, 0));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        linesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreStack.add(scoreLabel);
        scoreStack.add(linesLabel);

        // ---- LEVEL BADGE ----
        JLabel levelBadge = new JLabel(level.label);
        levelBadge.setForeground(level.color);
        levelBadge.setFont(new Font("Arial", Font.BOLD, 13));
        levelBadge.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        // ---- HIGH SCORE ----
        highScoreLabel = new JLabel("BEST: " + highScore);
        highScoreLabel.setForeground(new Color(255, 200, 60));
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 13));
        highScoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        highScoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // ---- TOP PANEL ----
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 30));
        topPanel.add(scoreStack,     BorderLayout.CENTER);
        topPanel.add(levelBadge,     BorderLayout.WEST);
        topPanel.add(highScoreLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ---- BOARD ----
        boardPanel = new BoardPanel(gameManager.getBoard());
        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(new Color(20, 20, 30));
        centerWrapper.add(boardPanel);
        add(centerWrapper, BorderLayout.CENTER);

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int col = e.getX() / 50, row = e.getY() / 50;
                blockController.tryPlace(row, col);
            }
        });

        // ---- BOTTOM PREVIEW ----
        bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(20, 20, 30));
        bottomPanel.setPreferredSize(new Dimension(500, 140));
        add(bottomPanel, BorderLayout.SOUTH);

        refreshPreviews();

        // Tutorial (hanya sekali)
        SwingUtilities.invokeLater(this::maybeShowTutorial);
    }

    // =========================
    // TUTORIAL
    // =========================
    private void maybeShowTutorial() {
        Preferences prefs = Preferences.userNodeForPackage(GamePanel.class);
        if (!prefs.getBoolean(PREF_TUTORIAL, false)) {
            showTutorialDialog();
            prefs.putBoolean(PREF_TUTORIAL, true);
        }
    }

    private void showTutorialDialog() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(28, 28, 42));
        content.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        Font titleFont = new Font("Arial", Font.BOLD, 18);
        Font bodyFont  = new Font("Arial", Font.PLAIN, 14);

        String[][] tips = {
            {"🎮  CARA MAIN BIJI BLAST", "title"},
            {"", ""},
            {"🖱  DRAG  blok dari bawah ke papan untuk menaruhnya.", "body"},
            {"", ""},
            {"↩  ROTATE  ➜  Double-click blok untuk merotasinya!", "body"},
            {"", ""},
            {"💥  HAPUS BARIS/KOLOM  ➜  Isi seluruh baris atau kolom", "body"},
            {"     (beda warna boleh, yang penting penuh!)", "body"},
            {"", ""},
            {"⭐  SKOR  ➜  Poin didapat saat berhasil clear baris/kolom.", "body"},
            {"     Clear 2 sekaligus = COMBO ×1.5 bonus!", "body"},
            {"", ""},
            {"❌  GAME OVER  ➜  Saat tidak ada blok yang bisa ditaruh.", "body"},
        };

        for (String[] row : tips) {
            JLabel lbl = new JLabel(row[0].isEmpty() ? " " : row[0]);
            lbl.setForeground("title".equals(row[1]) ? new Color(255, 200, 60) : Color.WHITE);
            lbl.setFont("title".equals(row[1]) ? titleFont : bodyFont);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(lbl);
        }

        JOptionPane.showMessageDialog(frame, content,
            "Selamat Datang di Biji Blast! 🎉", JOptionPane.PLAIN_MESSAGE);
    }

    // =========================
    // REFRESH PREVIEWS
    // =========================
    private void refreshPreviews() {
        bottomPanel.removeAll();
        Block b1 = blockController.getSlot(0);
        Block b2 = blockController.getSlot(1);
        Block b3 = blockController.getSlot(2);
        preview1 = new BlockPreviewPanel(b1);
        preview2 = new BlockPreviewPanel(b2);
        preview3 = new BlockPreviewPanel(b3);
        addDragListeners(preview1, 0);
        addDragListeners(preview2, 1);
        addDragListeners(preview3, 2);
        bottomPanel.add(preview1);
        bottomPanel.add(preview2);
        bottomPanel.add(preview3);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    private BlockPreviewPanel getPreviewForSlot(int slot) {
        if (slot == 0) return preview1;
        if (slot == 1) return preview2;
        if (slot == 2) return preview3;
        return null;
    }

    // =========================
    // DRAG + DOUBLE-CLICK
    // =========================
    private void addDragListeners(BlockPreviewPanel preview, int slot) {
        final boolean[] dragging = {false};

        preview.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                dragging[0] = false;
                Block b = blockController.getSlot(slot);
                if (b == null) return;
                blockController.selectSlot(slot);
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (dragging[0]) {
                    dragging[0] = false;
                    dragGlassPane.stopDrag();
                    boardPanel.clearGhost();
                    boardPanel.clearHintBlock();
                    Point bp = SwingUtilities.convertPoint(preview, e.getPoint(), boardPanel);
                    if (boardPanel.contains(bp))
                        blockController.tryPlace(bp.y / 50, bp.x / 50);
                }
            }
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    blockController.selectSlot(slot);
                    blockController.rotateSelected();
                    refreshPreviews();
                    updateSelectionVisuals(blockController.getSelectedIndex());
                    // Flash konfirmasi rotate
                    BlockPreviewPanel p = getPreviewForSlot(slot);
                    if (p != null) p.flashRotate();
                }
            }
        });

        preview.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                Block b = blockController.getSlot(slot);
                if (b == null) return;
                if (!dragging[0]) {
                    dragging[0] = true;
                    Point fp = SwingUtilities.convertPoint(preview, e.getPoint(), frame);
                    dragGlassPane.startDrag(b, fp);
                    boardPanel.setHintBlock(b);  // tampilkan hint
                }
                dragGlassPane.updatePosition(SwingUtilities.convertPoint(preview, e.getPoint(), frame));
                Point bp = SwingUtilities.convertPoint(preview, e.getPoint(), boardPanel);
                if (boardPanel.contains(bp)) boardPanel.setGhost(b, bp.y / 50, bp.x / 50);
                else boardPanel.clearGhost();
            }
        });
    }

    // =========================
    // SELECTION VISUAL
    // =========================
    private void updateSelectionVisuals(int idx) {
        if (preview1 != null) preview1.setSelected(idx == 0);
        if (preview2 != null) preview2.setSelected(idx == 1);
        if (preview3 != null) preview3.setSelected(idx == 2);
    }

    // =========================
    // SCORE PULSE
    // =========================
    private void pulseScore() {
        pulseTick = 0;
        if (pulseTimer != null && pulseTimer.isRunning()) pulseTimer.stop();
        pulseTimer = new javax.swing.Timer(40, null);
        pulseTimer.addActionListener(e -> {
            pulseTick++;
            float t = pulseTick / 8f;
            // Bright gold → white cycle
            int r = 255, green = (int)(200 + 55 * Math.min(1, t)), b = (int)(60 * (1 - Math.min(1, t)));
            scoreLabel.setForeground(new Color(r, green, Math.max(0, b)));
            if (pulseTick >= 10) {
                scoreLabel.setForeground(Color.WHITE);
                pulseTimer.stop();
            }
        });
        pulseTimer.start();
    }

    // =========================
    // GAME OVER DIALOG
    // =========================
    private void showGameOverDialog() {
        // Update high score if beaten
        Preferences prefs = Preferences.userNodeForPackage(GamePanel.class);
        int finalScore = gameManager.getScore();
        boolean newRecord = finalScore > highScore;
        if (newRecord) {
            highScore = finalScore;
            prefs.putInt(PREF_HIGHSCORE, highScore);
        }

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(28, 28, 42));
        content.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));

        Font bigFont  = new Font("Arial", Font.BOLD, 22);
        Font bodyFont = new Font("Arial", Font.PLAIN, 15);

        JLabel title = new JLabel("💀  GAME OVER!");
        title.setFont(bigFont);
        title.setForeground(new Color(255, 80, 80));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLbl = new JLabel("Skor kamu : " + finalScore);
        scoreLbl.setFont(bodyFont);
        scoreLbl.setForeground(Color.WHITE);
        scoreLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bestLbl = new JLabel(newRecord
            ? "🏆  NEW RECORD!  " + highScore
            : "Best Score : " + highScore);
        bestLbl.setFont(bodyFont);
        bestLbl.setForeground(newRecord ? new Color(255, 215, 0) : new Color(180, 180, 200));
        bestLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel linesLbl = new JLabel("Total Lines Cleared : " + gameManager.getTotalLinesCleared());
        linesLbl.setFont(bodyFont);
        linesLbl.setForeground(new Color(150, 220, 255));
        linesLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(12));
        content.add(scoreLbl);
        content.add(Box.createVerticalStrut(6));
        content.add(bestLbl);
        content.add(Box.createVerticalStrut(6));
        content.add(linesLbl);

        String[] opts = {"MAIN LAGI", "KEMBALI KE MENU"};
        int choice = JOptionPane.showOptionDialog(frame, content, "Biji Blast",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);

        if (choice == 0) frame.startGame(gameManager.getLevel());
        else             frame.showMenu();
    }

    // ---- InputHandler.InputListener ----
    @Override public void onRotate() {
        blockController.rotateSelected();
        refreshPreviews();
        repaint();
    }
    @Override public void onSelectSlot(int i) { blockController.selectSlot(i); }
    @Override public void onEscape()          { frame.showMenu(); }
    @Override public void onPause()           { }

    // ---- BlockController.BlockControllerListener ----
    @Override public void onSlotSelected(int i) { updateSelectionVisuals(i); }

    @Override
    public void onBlockPlaced(int slotIndex, int score, List<int[]> clearedCells) {
        // Update labels
        scoreLabel.setText("SCORE : " + score);
        linesLabel.setText("Lines: " + gameManager.getTotalLinesCleared());

        // Update high score label
        if (score > highScore) {
            highScore = score;
            Preferences.userNodeForPackage(GamePanel.class).putInt(PREF_HIGHSCORE, highScore);
            highScoreLabel.setText("BEST: " + highScore);
        }

        if (!clearedCells.isEmpty()) {
            boardPanel.triggerExplosion(clearedCells);

            // Floating score text
            int clearScore = gameManager.getLastClearScore();
            boardPanel.triggerFloatingText("+" + clearScore,
                new Color(255, 230, 60), 24);

            // Floating combo text (jika ada)
            String comboText = gameManager.getLastComboText();
            if (!comboText.isEmpty()) {
                boardPanel.triggerFloatingText(comboText,
                    new Color(255, 120, 40), 20);
            }

            // Score pulse
            pulseScore();
        }

        boardPanel.repaint();
        updateSelectionVisuals(BlockController.NO_SELECTION);
    }

    @Override public void onBlocksRegenerated(Block[] s) { refreshPreviews(); }
    @Override public void onGameOver()                   { showGameOverDialog(); }
}