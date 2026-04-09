package ephemera.the_stars_above.farming;

import ephemera.the_stars_above.movement.MovementController;
import ephemera.the_stars_above.utils.TimerUtils;
import net.minecraft.client.MinecraftClient;

public class StraightLinePattern extends FarmingPattern {
    private enum State {
        WALKING, SHIFTING, TURNING
    }
    
    private State currentState = State.WALKING;
    private final TimerUtils actionTimer = new TimerUtils();
    
    private int rowLength = 50; 
    private int currentTicks = 0;
    private float targetYaw = 0;
    
    @Override
    public void syncConfig(int rowLength, int cols) {
        this.rowLength = rowLength;
    }

    @Override
    public void start() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            targetYaw = client.player.getYaw();
        }
        currentState = State.WALKING;
        MovementController.attack(true);
        MovementController.moveForward(true);
        currentTicks = 0;
    }

    @Override
    public void stop() {
        MovementController.stopAll();
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        switch (currentState) {
            case WALKING:
                MovementController.moveForward(true);
                currentTicks++;
                if (client.player.horizontalCollision || currentTicks > rowLength * 15) {
                    MovementController.moveForward(false);
                    currentState = State.SHIFTING;
                    actionTimer.reset();
                }
                break;
            case SHIFTING:
                MovementController.moveLeft(true);
                if (actionTimer.hasPassed(500)) { 
                    MovementController.moveLeft(false);
                    targetYaw += 180f;
                    currentState = State.TURNING;
                    actionTimer.reset();
                }
                break;
            case TURNING:
                // Instantly snap for simple logic; RotationHandler does smoothing if integrated
                client.player.setYaw(targetYaw);
                if (actionTimer.hasPassed(200)) {
                    currentState = State.WALKING;
                    currentTicks = 0;
                }
                break;
        }
    }
}
