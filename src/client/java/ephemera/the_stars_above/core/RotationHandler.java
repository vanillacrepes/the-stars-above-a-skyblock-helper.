package ephemera.the_stars_above.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class RotationHandler {
    private float targetYaw;
    private float targetPitch;
    private boolean isRotating;
    private float speed = 0.5f;

    public void setTarget(float yaw, float pitch) {
        this.targetYaw = yaw;
        this.targetPitch = pitch;
        this.isRotating = true;
    }

    public void update() {
        if (!isRotating) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        float currentYaw = client.player.getYaw();
        float currentPitch = client.player.getPitch();

        float diffYaw = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float diffPitch = MathHelper.wrapDegrees(targetPitch - currentPitch);

        if (Math.abs(diffYaw) < 1.0f && Math.abs(diffPitch) < 1.0f) {
            client.player.setYaw(targetYaw);
            client.player.setPitch(targetPitch);
            isRotating = false;
            return;
        }

        client.player.setYaw(currentYaw + (diffYaw * speed));
        client.player.setPitch(currentPitch + (diffPitch * speed));
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public boolean isRotating() {
        return isRotating;
    }
}
