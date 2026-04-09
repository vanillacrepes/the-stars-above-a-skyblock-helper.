package ephemera.the_stars_above;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.client.network.ClientPlayerEntity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MemberChecker {
    public static void check() {
        System.out.println("Fields in PlayerInventory:");
        for (Field f : PlayerInventory.class.getDeclaredFields()) {
            System.out.println(f.getName() + " (" + (java.lang.reflect.Modifier.isPublic(f.getModifiers()) ? "public" : "private") + ")");
        }
        System.out.println("Methods in ClientPlayerEntity:");
        for (Method m : ClientPlayerEntity.class.getMethods()) {
            if (m.getName().toLowerCase().contains("pos")) {
                System.out.println(m.getName());
            }
        }
    }
}
