package com.bijiblast.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * InputHandler — Menangani semua input keyboard dari player.
 *
 * Tanggung jawab:
 * - Mendengarkan tombol keyboard
 * - Meneruskan aksi ke InputListener (bisa GamePanel atau controller manapun)
 * - Tidak bergantung pada UI secara langsung (decoupled via interface)
 *
 * Cara pakai:
 *   InputHandler handler = new InputHandler(listener);
 *   gameFrame.addKeyListener(handler);
 *
 * Shortcut yang didukung:
 *   R         → Rotate block yang sedang dipilih
 *   1 / 2 / 3 → Pilih slot block ke-1, ke-2, atau ke-3
 *   ESC       → Kembali ke menu utama
 *   P         → Pause / Resume game
 */
public class InputHandler extends KeyAdapter {

    // =========================
    // LISTENER INTERFACE
    // =========================

    /**
     * Interface yang harus diimplementasi oleh pihak yang ingin
     * menerima event dari InputHandler.
     */
    public interface InputListener {

        /** Dipanggil saat player menekan tombol Rotate (R) */
        void onRotate();

        /** Dipanggil saat player memilih slot block (index 0, 1, atau 2) */
        void onSelectSlot(int slotIndex);

        /** Dipanggil saat player menekan ESC */
        void onEscape();

        /** Dipanggil saat player menekan P (pause/resume) */
        void onPause();
    }

    // =========================
    // FIELD
    // =========================
    private final InputListener listener;

    /** Flag untuk mencegah aksi berulang saat tombol ditahan */
    private boolean rotateHeld    = false;
    private boolean pauseHeld     = false;
    private boolean escapeHeld    = false;

    // =========================
    // CONSTRUCTOR
    // =========================

    /**
     * @param listener penerima event input (biasanya GamePanel atau BlockController)
     */
    public InputHandler(InputListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException("InputListener tidak boleh null");
        }

        this.listener = listener;
    }

    // =========================
    // KEY PRESSED
    // =========================
    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        switch (key) {

            // ---- ROTATE (R) ----
            case KeyEvent.VK_R:
                if (!rotateHeld) {
                    rotateHeld = true;
                    listener.onRotate();
                }
                break;

            // ---- PILIH SLOT 1 ----
            case KeyEvent.VK_1:
            case KeyEvent.VK_NUMPAD1:
                listener.onSelectSlot(0);
                break;

            // ---- PILIH SLOT 2 ----
            case KeyEvent.VK_2:
            case KeyEvent.VK_NUMPAD2:
                listener.onSelectSlot(1);
                break;

            // ---- PILIH SLOT 3 ----
            case KeyEvent.VK_3:
            case KeyEvent.VK_NUMPAD3:
                listener.onSelectSlot(2);
                break;

            // ---- PAUSE (P) ----
            case KeyEvent.VK_P:
                if (!pauseHeld) {
                    pauseHeld = true;
                    listener.onPause();
                }
                break;

            // ---- ESCAPE ----
            case KeyEvent.VK_ESCAPE:
                if (!escapeHeld) {
                    escapeHeld = true;
                    listener.onEscape();
                }
                break;

            default:
                break;
        }
    }

    // =========================
    // KEY RELEASED
    // =========================
    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        // Reset flag held saat tombol dilepas
        switch (key) {

            case KeyEvent.VK_R:
                rotateHeld = false;
                break;

            case KeyEvent.VK_P:
                pauseHeld = false;
                break;

            case KeyEvent.VK_ESCAPE:
                escapeHeld = false;
                break;

            default:
                break;
        }
    }
}
