package ephemera.the_stars_above.farming;

import ephemera.the_stars_above.core.MacroManager;
import ephemera.the_stars_above.gui.ModConfig;
import ephemera.the_stars_above.movement.InputManager;
import ephemera.the_stars_above.utils.TimerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MelonPumpkinPattern extends FarmingPattern {
    private enum State {
        FARMING, SWITCHING_ROW, ROW_DELAY
    }

    private State state = State.FARMING;
    private boolean movingRight = true;
    private final TimerUtils delayTimer = new TimerUtils();
    private int rowsCompleted = 0;
    private long targetDelay = 0;

    @Override
    public void start() {
        state = State.FARMING;
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
            case FARMING:
                InputManager.INSTANCE.forward(true);
                if (movingRight) {
                    InputManager.INSTANCE.right(true);
                    InputManager.INSTANCE.left(false);
                } else {
                    InputManager.INSTANCE.left(true);
                    InputManager.INSTANCE.right(false);
                }

                if (isAtRowEnd(client)) {
                    state = State.SWITCHING_ROW;
                    delayTimer.reset();
                }
                break;

            case SWITCHING_ROW:
                // Move forward a bit then switch direction
                InputManager.INSTANCE.forward(true);
                InputManager.INSTANCE.left(false);
                InputManager.INSTANCE.right(false);
                
                if (delayTimer.hasPassed(500)) { // Move forward for 500ms
                    movingRight = !movingRight;
                    rowsCompleted++;
                    
                    if (rowsCompleted >= ModConfig.INSTANCE.columnCount) {
                        MacroManager.INSTANCE.startReturnToStart();
                        return;
                    }

                    state = State.ROW_DELAY;
                    targetDelay = ModConfig.INSTANCE.rowDelayMin + 
                        (long) (Math.random() * (ModConfig.INSTANCE.rowDelayMax - ModConfig.INSTANCE.rowDelayMin));
                    delayTimer.reset();
                    InputManager.INSTANCE.clear();
                }
                break;

            case ROW_DELAY:
                if (delayTimer.hasPassed(targetDelay)) {
                    state = State.FARMING;
                    InputManager.INSTANCE.attack(true);
                }
                break;
        }
    }

    private boolean isAtRowEnd(MinecraftClient client) {
        if (ModConfig.INSTANCE.detectionMode == ModConfig.DetectionMode.AIR_BLOCK) {
            Direction dir = client.player.getHorizontalFacing();
            BlockPos pos = client.player.getBlockPos().offset(dir);
            return client.world != null && client.world.getBlockState(pos).isAir();
        } else {
             // Distance-based logic could go here, but for now we use collision as a fallback
             return client.player.horizontalCollision;
        }
    }
}
