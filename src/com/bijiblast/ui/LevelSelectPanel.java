package com.bijiblast.ui;

import com.bijiblast.model.GameLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelSelectPanel extends JPanel {

    public LevelSelectPanel(GameFrame frame) {

        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40);

        // TITLE
        JLabel title = new JLabel("PILIH LEVEL");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy  = 0;
        gbc.insets = new Insets(0, 40, 30, 40);
        add(title, gbc);

        // 3 LEVEL CARDS
        gbc.insets = new Insets(8, 40, 8, 40);

        gbc.gridy = 1;
        add(buildCard(GameLevel.EASY, frame), gbc);

        gbc.gridy = 2;
        add(buildCard(GameLevel.MEDIUM, frame), gbc);

        gbc.gridy = 3;
        add(buildCard(GameLevel.HARD, frame), gbc);

        // BACK BUTTON
        JButton back = new JButton("← BACK");
        back.setFocusPainted(false);
        back.setBackground(new Color(60, 60, 80));
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 14));
        back.addActionListener(e -> frame.showMenu());
        gbc.gridy  = 4;
        gbc.insets = new Insets(20, 40, 0, 40);
        add(back, gbc);
    }

    // =========================
    // BUILD LEVEL CARD
    // =========================
    private JPanel buildCard(GameLevel level, GameFrame frame) {

        JPanel card = new JPanel(new BorderLayout(15, 0)) {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) {
                        hovered = true; repaint();
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                    @Override public void mouseExited(MouseEvent e)  {
                        hovered = false; repaint();
                        setCursor(Cursor.getDefaultCursor());
                    }
                    @Override public void mouseClicked(MouseEvent e) {
                        frame.startGame(level);
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Background card
                Color bg = hovered
                        ? new Color(level.color.getRed(),
                                    level.color.getGreen(),
                                    level.color.getBlue(), 40)
                        : new Color(35, 35, 50);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // Border warna level
                g2.setColor(hovered ? level.color
                                    : level.color.darker());
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(380, 72));
        card.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        // Left: colored dot
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(level.color);
                g2.fillOval(0, 0, 18, 18);
            }
        };
        dot.setPreferredSize(new Dimension(18, 18));
        dot.setOpaque(false);

        // Center: label + desc
        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);

        JLabel lbl = new JLabel(level.label);
        lbl.setForeground(level.color);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel desc = new JLabel(level.description);
        desc.setForeground(new Color(180, 180, 200));
        desc.setFont(new Font("Arial", Font.PLAIN, 13));

        text.add(lbl);
        text.add(desc);

        // Right: multiplier
        JLabel mult = new JLabel("× " + level.scoreMultiplier);
        mult.setForeground(Color.WHITE);
        mult.setFont(new Font("Arial", Font.BOLD, 16));
        mult.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(dot,  BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        card.add(mult, BorderLayout.EAST);

        return card;
    }
}
