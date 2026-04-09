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
        LEFT, RIGHT, ROW_DELAY
    }

    private State state = State.LEFT;
    private final TimerUtils delayTimer = new TimerUtils();
    private int rowsCompleted = 0;
    private long targetDelay = 0;
    private double lastY = -1;

    @Override
    public void syncConfig(int rowCount) {
    }

    @Override
    public void start() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            lastY = client.player.getY();
            
            // Startup Direction Sensing: Check perpendicular blocks
            Direction facing = client.player.getHorizontalFacing();
            Direction left = facing.rotateYCounterclockwise();
            Direction right = facing.rotateYClockwise();
            
            if (isAir(client, left)) {
                state = State.LEFT;
            } else if (isAir(client, right)) {
                state = State.RIGHT;
            } else {
                state = State.LEFT; // fallback
            }
        }
        
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
                checkRowSwitch(client);
                break;

            case RIGHT:
                InputManager.INSTANCE.right(true);
                InputManager.INSTANCE.left(false);
                checkRowSwitch(client);
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

    private void checkRowSwitch(MinecraftClient client) {
        // Fall Detection
        if (client.player.getY() < lastY - 0.5) {
            lastY = client.player.getY();
            rowsCompleted++;
            
            if (rowsCompleted >= ModConfig.INSTANCE.rowCount) {
                MacroManager.INSTANCE.startReturnToStart("All rows completed (Wheat)");
                return;
            }

            state = State.ROW_DELAY;
            targetDelay = ModConfig.INSTANCE.rowDelayMin + 
                (long) (Math.random() * (ModConfig.INSTANCE.rowDelayMax - ModConfig.INSTANCE.rowDelayMin));
            delayTimer.reset();
            InputManager.INSTANCE.clear();
            return;
        }
    }

    private boolean isAir(MinecraftClient client, Direction dir) {
        if (client.world == null || client.player == null) return false;
        BlockPos pos = client.player.getBlockPos().offset(dir);
        return client.world.getBlockState(pos).isAir();
    }
}
