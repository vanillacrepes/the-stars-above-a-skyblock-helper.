package ephemera.the_stars_above.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScoreboardUtils {
    
    /**
     * Retrieves the unformatted scoreboard title.
     * @return The title, or an empty string if none exists.
     */
    public static String getScoreboardTitle() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return "";
        Scoreboard scoreboard = client.world.getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        if (objective != null) {
            return cleanColorCodes(objective.getDisplayName().getString());
        }
        return "";
    }

    /**
     * Retrieves all lines from the sidebar scoreboard, cleaned of formatting.
     * @return A list of string lines on the scoreboard.
     */
    public static List<String> getScoreboardLines() {
        List<String> lines = new ArrayList<>();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return lines;

        Scoreboard scoreboard = client.world.getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        
        if (objective != null) {
            Collection<ScoreboardEntry> scores = scoreboard.getScoreboardEntries(objective);
            for (ScoreboardEntry score : scores) {
                String name = score.owner();
                Team team = scoreboard.getScoreHolderTeam(name);
                if (team != null) {
                    String prefix = team.getPrefix().getString();
                    String suffix = team.getSuffix().getString();
                    lines.add(cleanColorCodes(prefix + name + suffix));
                } else {
                    lines.add(cleanColorCodes(name));
                }
            }
        }
        return lines;
    }
    
    /**
     * Removes all Minecraft formatting codes from a string.
     * @param input the formatted text
     * @return unformatted text
     */
    public static String cleanColorCodes(String input) {
        if (input == null) return "";
        return input.replaceAll("(?i)\\u00A7.", "");
    }
}
