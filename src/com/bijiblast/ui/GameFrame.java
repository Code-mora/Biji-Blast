package com.bijiblast.ui;

import com.bijiblast.input.InputHandler;
import com.bijiblast.model.GameLevel;

import javax.swing.JFrame;
import java.awt.event.KeyListener;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Biji Blast");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true);
        showMenu();
        setVisible(true);
    }

    public void showMenu() {
        removeAllKeyListeners();
        setContentPane(new MenuPanel(this));
        revalidate();
        repaint();
    }

    public void showLevelSelect() {
        removeAllKeyListeners();
        setContentPane(new LevelSelectPanel(this));
        revalidate();
        repaint();
    }

    public void startGame(GameLevel level) {
        removeAllKeyListeners();
        GamePanel gamePanel = new GamePanel(this, level);
        setContentPane(gamePanel);
        addKeyListener(new InputHandler(gamePanel));
        requestFocusInWindow();
        revalidate();
        repaint();
    }

    private void removeAllKeyListeners() {
        for (KeyListener kl : getKeyListeners()) removeKeyListener(kl);
    }
}