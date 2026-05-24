package com.bijiblast.engine;

import com.bijiblast.model.Block;
import com.bijiblast.model.GameLevel;
import com.bijiblast.utils.BlockFactory;

import java.util.List;

public class BlockController {

    public static final int SLOT_COUNT   = 3;
    public static final int NO_SELECTION = -1;

    private final GameManager gameManager;
    private final GameLevel   level;

    private Block[]   slots;
    private boolean[] used;
    private int       selectedIndex;

    public interface BlockControllerListener {
        void onBlockPlaced(int slotIndex, int score, List<int[]> clearedCells);
        void onBlocksRegenerated(Block[] newSlots);
        void onGameOver();
        void onSlotSelected(int slotIndex);
    }

    private BlockControllerListener controllerListener;

    public BlockController(GameManager gameManager) {
        this(gameManager, GameLevel.MEDIUM);
    }

    public BlockController(GameManager gameManager, GameLevel level) {
        if (gameManager == null) throw new IllegalArgumentException("GameManager null");
        this.gameManager   = gameManager;
        this.level         = level;
        this.slots         = new Block[SLOT_COUNT];
        this.used          = new boolean[SLOT_COUNT];
        this.selectedIndex = NO_SELECTION;
        fillSlots();
    }

    public void setListener(BlockControllerListener listener) {
        this.controllerListener = listener;
    }

    public boolean selectSlot(int index) {
        if (index < 0 || index >= SLOT_COUNT || used[index]) return false;
        selectedIndex = index;
        if (controllerListener != null) controllerListener.onSlotSelected(index);
        return true;
    }

    public boolean rotateSelected() {
        Block b = getSelectedBlock();
        if (b == null) return false;
        b.rotate();
        return true;
    }

    public boolean tryPlace(int row, int col) {
        if (selectedIndex == NO_SELECTION) return false;
        Block block = getSelectedBlock();
        if (block == null) return false;

        boolean success = gameManager.playMove(block, row, col);
        if (!success) return false;

        int placedIndex = selectedIndex;
        used[placedIndex] = true;
        selectedIndex     = NO_SELECTION;

        int         score        = gameManager.getScore();
        List<int[]> clearedCells = gameManager.getLastClearedCells();

        if (controllerListener != null)
            controllerListener.onBlockPlaced(placedIndex, score, clearedCells);

        fillSlots();

        if (controllerListener != null)
            controllerListener.onBlocksRegenerated(slots.clone());

        if (isGameOver()) {
            if (controllerListener != null) controllerListener.onGameOver();
        }

        return true;
    }

    public boolean isGameOver() {
        for (int i = 0; i < SLOT_COUNT; i++)
            if (!used[i] && slots[i] != null && !gameManager.isGameOver(slots[i]))
                return false;
        return true;
    }

    public Block   getSelectedBlock()      { return selectedIndex == NO_SELECTION ? null : slots[selectedIndex]; }
    public int     getSelectedIndex()      { return selectedIndex; }
    public Block   getSlot(int i)          { return (i < 0 || i >= SLOT_COUNT || used[i]) ? null : slots[i]; }
    public Block[] getAllSlots()            { return slots.clone(); }
    public boolean isSlotUsed(int i)       { return (i < 0 || i >= SLOT_COUNT) || used[i]; }
    public void    reset()                 { selectedIndex = NO_SELECTION; fillSlots(); }

    private void fillSlots() {
        Block[] fresh = BlockFactory.createThreeUniqueBlocksForLevel(level);
        for (int i = 0; i < SLOT_COUNT; i++) { slots[i] = fresh[i]; used[i] = false; }
    }
}
