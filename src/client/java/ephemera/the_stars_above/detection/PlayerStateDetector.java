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

    /**
     * Determines if the player is on their Skyblock Garden island.
     * @return true if on the Garden
     */
    public static boolean isOnGarden() {
        if (!isOnSkyblock()) return false;
        
        List<String> lines = ScoreboardUtils.getScoreboardLines();
        for (String line : lines) {
            String upper = line.toUpperCase();
            if (upper.contains("LOCATION:") && upper.contains("GARDEN")) {
                return true;
            }
        }
        return false;
    }
}
