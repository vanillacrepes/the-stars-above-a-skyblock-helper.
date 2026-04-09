package ephemera.the_stars_above.tool;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.HoeItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ToolManager {
    
    private boolean enabled = true;

    public void update() {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        PlayerInventory inventory = client.player.getInventory();
        int bestSlot = -1;
        float bestScore = -1;

        // Scan hotbar (0-8)
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) continue;
            
            float score = evaluateTool(stack);
            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }
        
        if (bestSlot != -1 && client.player.getInventory().getSelectedSlot() != bestSlot) {
            client.player.getInventory().setSelectedSlot(bestSlot);
        }
    }

    private float evaluateTool(ItemStack stack) {
        Item item = stack.getItem();
        float score = 0;
        
        if (item instanceof HoeItem) {
            score += 100;
        } else if (item instanceof AxeItem) {
            score += 50; 
        }
        
        String name = stack.getName().getString().toLowerCase();
        if (name.contains("hoe")) score += 10;
        if (name.contains("axe")) score += 5;
        if (name.contains("dicer")) score += 50; 
        if (name.contains("math")) score += 50; 
        
        return score;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
