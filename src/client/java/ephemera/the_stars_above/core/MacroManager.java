package ephemera.the_stars_above.core;

import ephemera.the_stars_above.events.MacroStateChangeEvent;
import ephemera.the_stars_above.farming.FarmingPattern;
import ephemera.the_stars_above.farming.PatternFactory;
import ephemera.the_stars_above.gui.ModConfig;
import ephemera.the_stars_above.movement.InputManager;
import ephemera.the_stars_above.movement.Pathfinder;
import ephemera.the_stars_above.movement.StuckDetector;
import ephemera.the_stars_above.tool.GardenWarpManager;
import ephemera.the_stars_above.tool.ToolManager;
import ephemera.the_stars_above.utils.ChatUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class MacroManager {
    public static final MacroManager INSTANCE = new MacroManager();

    private MacroState state = MacroState.IDLE;
    private FarmingPattern currentPattern;
    
    private final RotationHandler rotationHandler = new RotationHandler();
    private final StuckDetector stuckDetector = new StuckDetector();
    private final ToolManager toolManager = new ToolManager();
    private final GardenWarpManager warpManager = new GardenWarpManager();

    private int cropsBroken = 0;
    private long sessionStartTime = 0;

    private MacroManager() {}

    public void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> onTick());
    }

    public void start() {
        if (state == MacroState.RUNNING) return;
        setState(MacroState.RUNNING);
        
        currentPattern = PatternFactory.create(ModConfig.INSTANCE.farmingPattern);
        currentPattern.syncConfig(ModConfig.INSTANCE.rowLength, ModConfig.INSTANCE.columnCount);
        currentPattern.start();
        
        stuckDetector.setEnabled(true);
        sessionStartTime = System.currentTimeMillis();
        cropsBroken = 0;
        
        ChatUtils.sendMessage("Macro started!");
    }

    public void stop(String reason) {
        if (state == MacroState.IDLE) return;
        setState(MacroState.IDLE);
        
        if (currentPattern != null) {
            currentPattern.stop();
        }
        InputManager.INSTANCE.clear();
        ChatUtils.sendMessage("Macro stopped: " + reason);
    }
    
    public void pause() {
        if (state == MacroState.RUNNING) {
            setState(MacroState.PAUSED);
            if (currentPattern != null) {
                currentPattern.stop(); 
            }
        }
    }
    
    public void resume() {
        if (state == MacroState.PAUSED) {
            setState(MacroState.RUNNING);
            if (currentPattern != null) {
                currentPattern.start();
            }
        }
    }

    public void toggle() {
        if (state == MacroState.RUNNING) {
            stop("Toggled off by user.");
        } else {
            start();
        }
    }

    private void onTick() {
        if (state == MacroState.IDLE || state == MacroState.PAUSED) return;
        
        rotationHandler.update();
        toolManager.update();
        warpManager.update();
        stuckDetector.update();
        
        if (state == MacroState.RUNNING) {
            if (currentPattern != null && !rotationHandler.isRotating()) {
                currentPattern.onTick();
            }
        } else if (state == MacroState.RETURN_TO_START) {
            handleReturnToStart();
        }
    }

    public void startReturnToStart() {
        if (state != MacroState.RUNNING) return;
        setState(MacroState.RETURN_TO_START);
        if (currentPattern != null) {
            currentPattern.stop();
        }
        InputManager.INSTANCE.clear();
        ChatUtils.sendMessage("Farming completed. Returning to start...");
    }

    private void handleReturnToStart() {
        if (ModConfig.INSTANCE.returnMode == ModConfig.ReturnMode.COMMAND) {
            net.minecraft.client.MinecraftClient.getInstance().execute(() -> {
                if (net.minecraft.client.MinecraftClient.getInstance().player != null) {
                    net.minecraft.client.MinecraftClient.getInstance().player.networkHandler.sendChatCommand("warp garden");
                }
            });
            stop("Warping to garden.");
        } else {
            boolean reached = Pathfinder.INSTANCE.moveToward(
                ModConfig.INSTANCE.startX,
                ModConfig.INSTANCE.startY,
                ModConfig.INSTANCE.startZ
            );
            if (reached) {
                stop("Reached start position.");
            }
        }
    }
    
    public void incrementCrops() {
        cropsBroken++;
    }

    private void setState(MacroState newState) {
        MacroState oldState = this.state;
        this.state = newState;
        MacroStateChangeEvent.EVENT.invoker().onStateChange(oldState, newState);
    }

    public MacroState getState() {
        return state;
    }
    
    public int getCropsBroken() {
        return cropsBroken;
    }
    
    public long getSessionTime() {
        if (sessionStartTime == 0) return 0;
        return System.currentTimeMillis() - sessionStartTime;
    }
}
