package ephemera.the_stars_above.tool;

import ephemera.the_stars_above.detection.PlayerStateDetector;
import ephemera.the_stars_above.utils.ChatUtils;
import ephemera.the_stars_above.utils.TimerUtils;
import net.minecraft.client.MinecraftClient;

public class GardenWarpManager {
    private boolean enabled = true;
    private final TimerUtils warpTimer = new TimerUtils();
    
    public void update() {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        if (!PlayerStateDetector.isOnGarden()) {
            if (warpTimer.hasPassed(5000)) { 
                ChatUtils.sendMessage("Detected player outside of Garden. Warping back...");
                ChatUtils.sendCommand("warp garden");
                warpTimer.reset();
            }
        }
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
