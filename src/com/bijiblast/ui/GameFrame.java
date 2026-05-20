package com.bijiblast.ui;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame() {

        setTitle("Biji Blast");

        setSize(500, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        // Tampilkan menu awal
        setContentPane(new MenuPanel(this));

        setVisible(true);
    }

    // =========================
    // START GAME
    // =========================
    public void startGame() {

        setContentPane(new GamePanel(this));

        revalidate();

        repaint();
    }

    // =========================
    // SHOW MENU
    // =========================
    public void showMenu() {

        setContentPane(new MenuPanel(this));

        revalidate();

        repaint();
    }
}