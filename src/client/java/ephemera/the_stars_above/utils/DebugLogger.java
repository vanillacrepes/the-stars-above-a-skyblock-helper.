package ephemera.the_stars_above.utils;

import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DebugLogger {
    private static final File LOG_FILE = new File(FabricLoader.getInstance().getGameDir().toFile(), "logs/thestarsabove-debug.log");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static boolean initialized = false;

    /**
     * Logs a message to the debug log file.
     * @param message The message to log.
     */
    public static synchronized void log(String message) {
        ensureInitialized();
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            pw.println("[" + timestamp + "] " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ensureInitialized() {
        if (initialized) return;
        
        File parent = LOG_FILE.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        // Clear log file on startup
        try (FileWriter fw = new FileWriter(LOG_FILE, false)) {
            fw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        initialized = true;
        log("--- Debug Session Started ---");
    }
}
