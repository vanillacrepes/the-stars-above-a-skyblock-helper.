package ephemera.the_stars_above.farming;

import ephemera.the_stars_above.core.MacroManager;
import ephemera.the_stars_above.gui.ModConfig;
import ephemera.the_stars_above.movement.InputManager;
import ephemera.the_stars_above.utils.TimerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MushroomPattern extends FarmingPattern {
    private enum State {
        LEFT, RIGHT, SWITCHING, ROW_DELAY
    }

    private State state = State.LEFT;
    private final TimerUtils delayTimer = new TimerUtils();
    private int rowsCompleted = 0;
    private long targetDelay = 0;

    @Override
    public void syncConfig(int rowCount) {
    }

    @Override
    public void start() {
        state = State.LEFT;
        InputManager.INSTANCE.clear();
        InputManager.INSTANCE.attack(true);
        rowsCompleted = 0;
    }

    @Override
    public void stop() {
        InputManager.INSTANCE.clear();
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        switch (state) {
            case LEFT:
                InputManager.INSTANCE.left(true);
                InputManager.INSTANCE.right(false);
                InputManager.INSTANCE.back(false);
                
                if (isAtRowEnd(client)) {
                    state = State.SWITCHING;
                    delayTimer.reset();
                }
                break;

            case RIGHT:
                InputManager.INSTANCE.right(true);
                InputManager.INSTANCE.back(true);
                InputManager.INSTANCE.left(false);
                
                if (isAtRowEnd(client)) {
                    state = State.SWITCHING;
                    delayTimer.reset();
                }
                break;

            case SWITCHING:
                InputManager.INSTANCE.clear();
                if (delayTimer.hasPassed(300)) {
                    if (state == State.LEFT) {
                        state = State.RIGHT;
                    } else {
                        state = State.LEFT;
                    }
                    rowsCompleted++;
                    
                    if (rowsCompleted >= ModConfig.INSTANCE.rowCount) {
                        MacroManager.INSTANCE.startReturnToStart("All rows completed (Mushroom)");
                        return;
                    }

                    state = State.ROW_DELAY;
                    targetDelay = ModConfig.INSTANCE.rowDelayMin + 
                        (long) (Math.random() * (ModConfig.INSTANCE.rowDelayMax - ModConfig.INSTANCE.rowDelayMin));
                    delayTimer.reset();
                }
                break;

            case ROW_DELAY:
                if (delayTimer.hasPassed(targetDelay)) {
                    // Decide next state based on current
                    if (rowsCompleted % 2 == 0) {
                        state = State.LEFT;
                    } else {
                        state = State.RIGHT;
                    }
                    InputManager.INSTANCE.attack(true);
                }
                break;
        }
    }

    private boolean isAtRowEnd(MinecraftClient client) {
        if (ModConfig.INSTANCE.detectionMode == ModConfig.DetectionMode.AIR_BLOCK) {
            // For mushroom, we check the side we are moving toward
            Direction dir = (state == State.LEFT) ? client.player.getHorizontalFacing().rotateYCounterclockwise() 
                                                 : client.player.getHorizontalFacing().rotateYClockwise();
            BlockPos pos = client.player.getBlockPos().offset(dir);
            return client.world != null && client.world.getBlockState(pos).isAir();
        } else {
            return client.player.horizontalCollision;
        }
    }
}
