package ephemera.the_stars_above.detection;

import ephemera.the_stars_above.utils.ScoreboardUtils;

import java.util.List;

public class PlayerStateDetector {
    
    /**
     * Determines if the player is currently on Hypixel Skyblock.
     * @return true if on Skyblock
     */
    public static boolean isOnSkyblock() {
        String title = ScoreboardUtils.getScoreboardTitle();
        return title.toUpperCase().contains("SKYBLOCK");
    }

    private static long lastDebugTime = 0;

    /**
     * Determines if the player is on their Skyblock Garden island.
     * @return true if on the Garden
     */
    public static boolean isOnGarden() {
        boolean onSkyblock = isOnSkyblock();
        List<String> lines = ScoreboardUtils.getScoreboardLines();
        boolean onGarden = false;
        
        for (String line : lines) {
            if (line.toLowerCase().contains("garden")) {
                onGarden = true;
                break;
            }
        }

        // Debug logging: send results if debug mode is ON
        if (ephemera.the_stars_above.gui.ModConfig.INSTANCE.enableDebugMode) {
            long now = System.currentTimeMillis();
            if (now - lastDebugTime > 3000) { 
                lastDebugTime = now;
                
                // Summary in Chat
                ephemera.the_stars_above.utils.ChatUtils.sendMessage(
                    String.format("State Check: Skyblock=%b, Garden=%b (Detailed lines in log)", onSkyblock, onGarden)
                );
                
                // Detailed lines in Log File
                ephemera.the_stars_above.utils.DebugLogger.log("Scaning Scoreboard (Garden Check):");
                ephemera.the_stars_above.utils.DebugLogger.log("Skyblock: " + onSkyblock + ", Garden: " + onGarden);
                for (String line : lines) {
                    ephemera.the_stars_above.utils.DebugLogger.log("- '" + line + "'");
                }
            }
        }

        return onGarden;
    }
}
