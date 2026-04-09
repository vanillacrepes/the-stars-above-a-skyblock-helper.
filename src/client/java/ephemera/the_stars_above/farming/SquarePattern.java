package ephemera.the_stars_above.farming;

import ephemera.the_stars_above.movement.MovementController;
import net.minecraft.client.MinecraftClient;

public class SquarePattern extends FarmingPattern {
    
    private int sideLength = 10;
    private int ticksInCurrentSide = 0;
    private int sidesCompleted = 0;
    
    @Override
    public void syncConfig(int rowLength, int cols) {
        this.sideLength = rowLength / 2; // rough approx for expanding bounds
    }

    @Override
    public void start() {
        MovementController.attack(true);
        ticksInCurrentSide = 0;
        sidesCompleted = 0;
    }

    @Override
    public void stop() {
        MovementController.stopAll();
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        MovementController.moveForward(true);
        MovementController.moveLeft(true); // Diagonal harvest
        
        ticksInCurrentSide++;
        if (client.player.horizontalCollision || ticksInCurrentSide > sideLength * 10) {
            client.player.setYaw(client.player.getYaw() + 90f);
            ticksInCurrentSide = 0;
            sidesCompleted++;
            
            if (sidesCompleted % 2 == 0) {
                sideLength += 2;
            }
        }
    }
}
