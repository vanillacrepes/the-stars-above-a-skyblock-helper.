package ephemera.the_stars_above.core;

import ephemera.the_stars_above.events.StuckEvent;
import ephemera.the_stars_above.gui.ConfigScreen;
import ephemera.the_stars_above.gui.HudOverlay;
import ephemera.the_stars_above.gui.ModConfig;
import ephemera.the_stars_above.utils.ChatUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TheStarsAboveMod implements ClientModInitializer {
    
    public static KeyBinding toggleKey;
    public static KeyBinding configKey;
    
    private float lastHealth = -1;

    @Override
    public void onInitializeClient() {
        ModConfig.load();
        HudOverlay.init();

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.thestarsabove.toggle", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_GRAVE_ACCENT, 
                KeyBinding.Category.MISC
        ));
        
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.thestarsabove.config", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_EQUAL, 
                KeyBinding.Category.MISC
        ));

        MacroManager.INSTANCE.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                MacroManager.INSTANCE.toggle();
            }
            while (configKey.wasPressed()) {
                client.setScreen(ConfigScreen.create(client.currentScreen));
            }
            
            // Failsafe: GUI open screen
            if (MacroManager.INSTANCE.getState() == MacroState.RUNNING) {
                if (client.currentScreen != null && !(client.currentScreen instanceof net.minecraft.client.gui.screen.ChatScreen)) {
                    MacroManager.INSTANCE.stop("Unexpected screen opened.");
                }
            }
            
            // Failsafe: Damage detection
            if (client.player != null && ModConfig.INSTANCE.failsafeDamage) {
                float health = client.player.getHealth();
                if (lastHealth != -1 && health < lastHealth && MacroManager.INSTANCE.getState() == MacroState.RUNNING) {
                    MacroManager.INSTANCE.stop("Player took damage!");
                }
                lastHealth = health;
            } else {
                lastHealth = -1;
            }
        });

        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            String text = message.getString().toLowerCase();
            if (MacroManager.INSTANCE.getState() == MacroState.RUNNING) {
                if (text.contains("you have been banned") || text.contains("you have been kicked") || text.contains("evading away")) {
                    MacroManager.INSTANCE.stop("Received kick/ban/admin message.");
                }
            }
        });

        StuckEvent.EVENT.register(() -> {
            ChatUtils.sendWarning("Stuck detected! Pausing macro.");
            MacroManager.INSTANCE.pause();
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("setfarmstart").executes(context -> {
                if (context.getSource().getClient().player != null) {
                    var player = context.getSource().getClient().player;
                    ModConfig.INSTANCE.startX = player.getX();
                    ModConfig.INSTANCE.startY = player.getY();
                    ModConfig.INSTANCE.startZ = player.getZ();
                    ModConfig.INSTANCE.save();
                    ChatUtils.sendMessage("Farm start position set to: " + 
                        String.format("%.2f, %.2f, %.2f", ModConfig.INSTANCE.startX, ModConfig.INSTANCE.startY, ModConfig.INSTANCE.startZ));
                }
                return 1;
            }));
        });
    }
}
