package com.bijiblast.ui;

import com.bijiblast.engine.GameManager;
import com.bijiblast.model.Block;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {

    private GameManager gameManager;

    private BoardPanel boardPanel;

    private JLabel scoreLabel;

    private GameFrame frame;

    // BLOCK TERPILIH
    private Block selectedBlock;

    // BLOCK SLOT
    private Block block1;
    private Block block2;
    private Block block3;

    // PREVIEW PANEL
    private BlockPreviewPanel preview1;
    private BlockPreviewPanel preview2;
    private BlockPreviewPanel preview3;

    // PANEL BAWAH
    private JPanel bottomPanel;

    public GamePanel(GameFrame frame) {

        this.frame = frame;

        setLayout(new BorderLayout());

        setBackground(new Color(20, 20, 30));

        // =========================
        // GAME MANAGER
        // =========================
        gameManager = new GameManager();

        // =========================
        // SCORE LABEL
        // =========================
        scoreLabel =
                new JLabel("SCORE : 0");

        scoreLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        scoreLabel.setForeground(Color.WHITE);

        scoreLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        scoreLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        0,
                        20,
                        0
                )
        );

        // =========================
        // ROTATE BUTTON
        // =========================
        JButton rotateButton =
                new JButton("ROTATE");

        rotateButton.setFocusPainted(false);

        rotateButton.setBackground(
                new Color(255, 170, 0)
        );

        rotateButton.setForeground(Color.WHITE);

        rotateButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        rotateButton.addActionListener(e -> {

            if (selectedBlock != null) {

                selectedBlock.rotate();

                repaint();
            }
        });

        // =========================
        // TOP PANEL
        // =========================
        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.setBackground(
                new Color(20, 20, 30)
        );

        topPanel.add(scoreLabel,
                BorderLayout.CENTER);

        topPanel.add(rotateButton,
                BorderLayout.EAST);

        add(topPanel,
                BorderLayout.NORTH);

        // =========================
        // BOARD PANEL
        // =========================
        boardPanel =
                new BoardPanel(
                        gameManager.getBoard()
                );

        JPanel centerWrapper =
                new JPanel();

        centerWrapper.setBackground(
                new Color(20, 20, 30)
        );

        centerWrapper.add(boardPanel);

        add(centerWrapper,
                BorderLayout.CENTER);

        // =========================
        // CLICK BOARD
        // =========================
        boardPanel.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        if (selectedBlock == null) {
                            return;
                        }

                        int cellSize = 50;

                        int col =
                                e.getX() / cellSize;

                        int row =
                                e.getY() / cellSize;

                        boolean success =
                                gameManager.playMove(
                                        selectedBlock,
                                        row,
                                        col
                                );

                        if (success) {

                            scoreLabel.setText(
                                    "SCORE : "
                                            + gameManager.getScore()
                            );

                            boardPanel.repaint();

                            regenerateUsedBlock();

                            // RESET SELECT
                            preview1.setSelected(false);
                            preview2.setSelected(false);
                            preview3.setSelected(false);

                            // GAME OVER
                            if (gameManager.isGameOver(
                                    selectedBlock
                            )) {

                                showGameOverDialog();
                            }
                        }
                    }
                });

        // =========================
        // PANEL BAWAH
        // =========================
        bottomPanel =
                new JPanel();

        bottomPanel.setBackground(
                new Color(20, 20, 30)
        );

        bottomPanel.setPreferredSize(
                new Dimension(500, 140)
        );

        add(bottomPanel,
                BorderLayout.SOUTH);

        // GENERATE BLOCK
        generateBlocks();
    }

    // =========================
    // GENERATE BLOCK
    // =========================
    private void generateBlocks() {

        bottomPanel.removeAll();

        // RANDOM BLOCK
        block1 = Block.randomBlock();
        block2 = Block.randomBlock();
        block3 = Block.randomBlock();

        // PREVIEW
        preview1 =
                new BlockPreviewPanel(block1);

        preview2 =
                new BlockPreviewPanel(block2);

        preview3 =
                new BlockPreviewPanel(block3);

        // =========================
        // CLICK PREVIEW 1
        // =========================
        preview1.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        selectedBlock = block1;

                        preview1.setSelected(true);
                        preview2.setSelected(false);
                        preview3.setSelected(false);
                    }
                });

        // =========================
        // CLICK PREVIEW 2
        // =========================
        preview2.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        selectedBlock = block2;

                        preview1.setSelected(false);
                        preview2.setSelected(true);
                        preview3.setSelected(false);
                    }
                });

        // =========================
        // CLICK PREVIEW 3
        // =========================
        preview3.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        selectedBlock = block3;

                        preview1.setSelected(false);
                        preview2.setSelected(false);
                        preview3.setSelected(true);
                    }
                });

        bottomPanel.add(preview1);
        bottomPanel.add(preview2);
        bottomPanel.add(preview3);

        bottomPanel.revalidate();

        bottomPanel.repaint();
    }

    // =========================
    // REGENERATE BLOCK
    // =========================
    private void regenerateUsedBlock() {

        generateBlocks();

        selectedBlock = null;
    }

    // =========================
    // GAME OVER
    // =========================
    private void showGameOverDialog() {

        String[] options = {
                "CONTINUE",
                "BACK TO LOBBY"
        };

        int choice =
                JOptionPane.showOptionDialog(
                        null,
                        "GAME OVER!",
                        "Biji Blast",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        // CONTINUE
        if (choice == 0) {

            frame.startGame();
        }

        // BACK TO LOBBY
        else {

            frame.showMenu();
        }
    }
}