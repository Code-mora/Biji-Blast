package com.bijiblast.ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    public MenuPanel(GameFrame frame) {

        setLayout(new GridBagLayout());

        setBackground(new Color(20, 20, 30));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // =========================
        // TITLE
        // =========================
        JLabel title =
                new JLabel("BIJI BLAST");

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        40
                )
        );

        gbc.gridy = 0;

        gbc.insets =
                new Insets(0, 0, 40, 0);

        add(title, gbc);

        // =========================
        // START BUTTON
        // =========================
        JButton startButton =
                new JButton("START GAME");

        startButton.setFocusPainted(false);

        startButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        startButton.setBackground(
                new Color(0, 170, 255)
        );

        startButton.setForeground(Color.WHITE);

        gbc.gridy = 1;

        gbc.insets =
                new Insets(10, 0, 10, 0);

        add(startButton, gbc);

        // =========================
        // EXIT BUTTON
        // =========================
        JButton exitButton =
                new JButton("EXIT");

        exitButton.setFocusPainted(false);

        exitButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        exitButton.setBackground(
                new Color(255, 70, 70)
        );

        exitButton.setForeground(Color.WHITE);

        gbc.gridy = 2;

        add(exitButton, gbc);

        // =========================
        // BUTTON ACTION
        // =========================
        startButton.addActionListener(e -> {

            frame.startGame();

        });

        exitButton.addActionListener(e -> {

            System.exit(0);

        });
    }
}