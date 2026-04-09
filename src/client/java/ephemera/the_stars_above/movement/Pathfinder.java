package ephemera.the_stars_above.movement;

import ephemera.the_stars_above.gui.ModConfig;
import net.minecraft.client.MinecraftClient;

public class Pathfinder {
    public static final Pathfinder INSTANCE = new Pathfinder();

    private Pathfinder() {}

    public boolean moveToward(double targetX, double targetY, double targetZ) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return false;

        double posX = client.player.getX();
        double posZ = client.player.getZ();
        double deltaX = targetX - posX;
        double deltaZ = targetZ - posZ;
        double distanceSq = deltaX * deltaX + deltaZ * deltaZ;

        if (distanceSq < ModConfig.INSTANCE.movementThreshold * ModConfig.INSTANCE.movementThreshold) {
            InputManager.INSTANCE.clear();
            return true;
        }

        // Simple logic: move toward target using directional keys
        // We assume 0/180/90/270 degree alignment for simplicity in "Farming" environment
        // But let's make it a bit more flexible based on player yaw
        
        float playerYaw = client.player.getYaw() % 360;
        if (playerYaw < 0) playerYaw += 360;

        // Determine which keys to press based on delta and yaw
        // For simplicity in Skyblock Garden, usually you are aligned to axis.
        // We'll use the delta to decide W/S/A/D.
        
        InputManager.INSTANCE.clear();
        
        // This is a very basic "move toward" logic without pathfinding (A*)
        // as requested: "Simple logic ONLY (no A*)"
        
        if (Math.abs(deltaX) > ModConfig.INSTANCE.movementThreshold) {
            if (deltaX > 0) {
                // Target is at +X
                pressKeyForDirection(0, playerYaw); // Assuming 0 is +X or similar, but let's just use absolute logic for now
            } else {
                // Target is at -X
            }
        }
        
        // Actually, a better way for "Simple logic" is to just use the delta relative to player orientation
        // or just use raw movement keys if we assume they are at 0 yaw.
        
        // Let's use a simpler approach:
        double angle = Math.atan2(-deltaX, deltaZ); // Minecraft's coordinate system
        float yawToTarget = (float) Math.toDegrees(angle);
        
        // Since we aren't rotating, we just press keys.
        float relativeYaw = (yawToTarget - playerYaw) % 360;
        if (relativeYaw < -180) relativeYaw += 360;
        if (relativeYaw > 180) relativeYaw -= 360;

        if (relativeYaw > -45 && relativeYaw <= 45) {
            InputManager.INSTANCE.forward(true);
        } else if (relativeYaw > 45 && relativeYaw <= 135) {
            InputManager.INSTANCE.left(true);
        } else if (relativeYaw > -135 && relativeYaw <= -45) {
            InputManager.INSTANCE.right(true);
        } else {
            InputManager.INSTANCE.back(true);
        }

        return false;
    }

    private void pressKeyForDirection(float targetYaw, float currentYaw) {
        // Placeholder for more complex logic if needed
    }
}
