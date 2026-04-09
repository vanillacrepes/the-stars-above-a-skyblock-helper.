package ephemera.the_stars_above.farming;

import ephemera.the_stars_above.core.MacroManager;
import ephemera.the_stars_above.gui.ModConfig;
import ephemera.the_stars_above.movement.InputManager;
import ephemera.the_stars_above.utils.TimerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class WheatCarrotPotatoPattern extends FarmingPattern {
    private enum State {
        LEFT, RIGHT, SWITCHING, ROW_DELAY
    }

    private State state = State.LEFT;
    private final TimerUtils delayTimer = new TimerUtils();
    private int rowsCompleted = 0;
    private long targetDelay = 0;

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
                if (isAtRowEnd(client)) {
                    state = State.SWITCHING;
                    delayTimer.reset();
                }
                break;

            case RIGHT:
                InputManager.INSTANCE.right(true);
                InputManager.INSTANCE.left(false);
                if (isAtRowEnd(client)) {
                    state = State.SWITCHING;
                    delayTimer.reset();
                }
                break;

            case SWITCHING:
                // Move forward to next row
                InputManager.INSTANCE.forward(true); 
                InputManager.INSTANCE.left(false);
                InputManager.INSTANCE.right(false);
                
                if (delayTimer.hasPassed(600)) { // Time to move to next row
                    InputManager.INSTANCE.forward(false);
                    rowsCompleted++;
                    
                    if (rowsCompleted >= ModConfig.INSTANCE.columnCount) {
                        MacroManager.INSTANCE.startReturnToStart();
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
            Direction dir = (state == State.LEFT) ? client.player.getHorizontalFacing().rotateYCounterclockwise() 
                                                 : client.player.getHorizontalFacing().rotateYClockwise();
            BlockPos pos = client.player.getBlockPos().offset(dir);
            return client.world != null && client.world.getBlockState(pos).isAir();
        } else {
            return client.player.horizontalCollision;
        }
    }
}
