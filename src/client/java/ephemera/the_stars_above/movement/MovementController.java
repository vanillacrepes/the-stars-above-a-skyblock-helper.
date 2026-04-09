package ephemera.the_stars_above.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class MovementController {
    
    private static void setKeyState(KeyBinding key, boolean pressed) {
        key.setPressed(pressed);
    }

    public static void moveForward(boolean pressed) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            setKeyState(client.options.forwardKey, pressed);
        }
    }

    public static void moveBackward(boolean pressed) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            setKeyState(client.options.backKey, pressed);
        }
    }

    public static void moveLeft(boolean pressed) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            setKeyState(client.options.leftKey, pressed);
        }
    }

    public static void moveRight(boolean pressed) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            setKeyState(client.options.rightKey, pressed);
        }
    }

    public static void attack(boolean pressed) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            setKeyState(client.options.attackKey, pressed);
        }
    }
    
    public static void jump(boolean pressed) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            setKeyState(client.options.jumpKey, pressed);
        }
    }

    public static void stopAll() {
        moveForward(false);
        moveBackward(false);
        moveLeft(false);
        moveRight(false);
        attack(false);
        jump(false);
    }
}
