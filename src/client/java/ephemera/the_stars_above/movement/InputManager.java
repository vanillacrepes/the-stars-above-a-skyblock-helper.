package ephemera.the_stars_above.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import java.util.HashSet;
import java.util.Set;

public class InputManager {
    public static final InputManager INSTANCE = new InputManager();
    
    private final Set<KeyBinding> activeKeys = new HashSet<>();

    private InputManager() {}

    public void setKeyState(KeyBinding key, boolean pressed) {
        if (key == null) return;
        key.setPressed(pressed);
        if (pressed) {
            activeKeys.add(key);
        } else {
            activeKeys.remove(key);
        }
    }

    public void forward(boolean pressed) {
        setKeyState(MinecraftClient.getInstance().options.forwardKey, pressed);
    }

    public void back(boolean pressed) {
        setKeyState(MinecraftClient.getInstance().options.backKey, pressed);
    }

    public void left(boolean pressed) {
        setKeyState(MinecraftClient.getInstance().options.leftKey, pressed);
    }

    public void right(boolean pressed) {
        setKeyState(MinecraftClient.getInstance().options.rightKey, pressed);
    }

    public void attack(boolean pressed) {
        setKeyState(MinecraftClient.getInstance().options.attackKey, pressed);
    }

    public void jump(boolean pressed) {
        setKeyState(MinecraftClient.getInstance().options.jumpKey, pressed);
    }

    public void sneak(boolean pressed) {
        setKeyState(MinecraftClient.getInstance().options.sneakKey, pressed);
    }

    public void clear() {
        for (KeyBinding key : activeKeys) {
            key.setPressed(false);
        }
        activeKeys.clear();
        
        // Safety: also clear via options directly in case some weren't tracked
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            client.options.forwardKey.setPressed(false);
            client.options.backKey.setPressed(false);
            client.options.leftKey.setPressed(false);
            client.options.rightKey.setPressed(false);
            client.options.attackKey.setPressed(false);
            client.options.jumpKey.setPressed(false);
            client.options.sneakKey.setPressed(false);
        }
    }
}
