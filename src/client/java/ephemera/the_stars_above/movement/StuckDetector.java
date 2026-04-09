package ephemera.the_stars_above.movement;

import ephemera.the_stars_above.events.StuckEvent;
import ephemera.the_stars_above.utils.TimerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class StuckDetector {
    private final TimerUtils timer = new TimerUtils();
    private Vec3d lastPosition = Vec3d.ZERO;
    private boolean enabled = true;
    private int thresholdSeconds = 3;

    public void update() {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Vec3d currentPos = new Vec3d(client.player.getX(), client.player.getY(), client.player.getZ());
        
        // Distance check (squared to save math.sqrt)
        if (currentPos.distanceTo(lastPosition) > 0.25) { 
            lastPosition = currentPos;
            timer.reset();
        } else if (timer.hasPassed(thresholdSeconds * 1000L)) {
            // Player hasn't moved 0.5 blocks in threshold interval
            StuckEvent.EVENT.invoker().onStuck();
            timer.reset();
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            this.timer.reset();
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                this.lastPosition = new Vec3d(client.player.getX(), client.player.getY(), client.player.getZ());
            }
        }
    }

    public void setThresholdSeconds(int seconds) {
        this.thresholdSeconds = seconds;
    }
}
