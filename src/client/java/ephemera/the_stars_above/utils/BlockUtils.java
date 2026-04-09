package ephemera.the_stars_above.utils;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class BlockUtils {
    /**
     * Gets the current block state at a specific position.
     * @param pos the block position.
     * @return the BlockState or null if world is not loaded.
     */
    public static BlockState getBlockState(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            return client.world.getBlockState(pos);
        }
        return null;
    }
}
