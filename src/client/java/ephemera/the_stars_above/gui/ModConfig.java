package ephemera.the_stars_above.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ephemera.the_stars_above.farming.PatternFactory;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "thestarsabove.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ModConfig INSTANCE = new ModConfig();

    public enum DetectionMode {
        AIR_BLOCK,
        DISTANCE
    }

    public enum ReturnMode {
        COMMAND,
        PATHFIND
    }

    // General
    public boolean enableMacroToggle = true;
    public PatternFactory.PatternType farmingPattern = PatternFactory.PatternType.STRAIGHT_LINE;
    public int rowLength = 50;
    public int columnCount = 5;

    // Farming Settings
    public DetectionMode detectionMode = DetectionMode.DISTANCE;
    public int rowDelayMin = 100;
    public int rowDelayMax = 300;

    // Return to Start
    public ReturnMode returnMode = ReturnMode.COMMAND;
    public double startX = 0;
    public double startY = 0;
    public double startZ = 0;

    // Safety
    public boolean enableStuckDetection = true;
    public int stuckThresholdSeconds = 3;
    public boolean enableAutoWarp = true;
    public boolean failsafeDamage = true;

    // Tool
    public boolean enableAutoToolSwap = true;

    // Rotation
    public float rotationSmoothingSpeed = 0.5f;
    public boolean randomizeRotation = true;

    // Movement
    public float movementThreshold = 0.5f;

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            INSTANCE.save();
            return;
        }
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            INSTANCE = GSON.fromJson(reader, ModConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
