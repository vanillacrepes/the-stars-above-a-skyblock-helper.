package ephemera.the_stars_above.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatUtils {
    public static final String PREFIX = "§8[§aThe Stars Above§8] §r";

    /**
     * Sends a chat message to the player.
     * @param message The message to send.
     */
    public static void sendMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(PREFIX + message), false);
        }
    }
    
    /**
     * Sends a warning/critical chat message to the player.
     * @param message The warning message to send.
     */
    public static void sendWarning(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§8[§cThe Stars Above§8] ⚠ §c" + message), false);
        }
    }

    /**
     * Sends a command as if the player typed it.
     * @param command The command to execute (e.g., "warp garden").
     */
    public static void sendCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.networkHandler.sendChatCommand(command);
        }
    }
}
