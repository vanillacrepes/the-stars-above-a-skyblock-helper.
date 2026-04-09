package ephemera.the_stars_above.gui;

import ephemera.the_stars_above.core.MacroManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class HudOverlay implements HudRenderCallback {
    public static void init() {
        HudRenderCallback.EVENT.register(new HudOverlay());
    }

    @Override
    public void onHudRender(DrawContext drawContext, net.minecraft.client.render.RenderTickCounter tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        if (client.options.hudHidden) return;

        int y = 10;
        int x = 10;
        int color = 0xFFFFFF;

        drawContext.drawText(client.textRenderer, Text.literal("The Stars Above: " + MacroManager.INSTANCE.getState().name()), x, y, color, true);
        y += 12;
        
        if (MacroManager.INSTANCE.getState() != ephemera.the_stars_above.core.MacroState.IDLE) {
            long time = MacroManager.INSTANCE.getSessionTime();
            long seconds = (time / 1000) % 60;
            long minutes = (time / (1000 * 60)) % 60;
            long hours = (time / (1000 * 60 * 60)) % 24;
            String timeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            drawContext.drawText(client.textRenderer, Text.literal("Time: " + timeStr), x, y, color, true);
            y += 12;
            
            int broken = MacroManager.INSTANCE.getCropsBroken();
            drawContext.drawText(client.textRenderer, Text.literal("Crops: " + broken), x, y, color, true);
            y += 12;

            double bps = 0;
            if (time > 0) bps = (double) broken / (time / 1000.0);
            drawContext.drawText(client.textRenderer, Text.literal(String.format("BPS: %.1f", Math.min(bps, 99.9))), x, y, color, true);
        }
    }
}
