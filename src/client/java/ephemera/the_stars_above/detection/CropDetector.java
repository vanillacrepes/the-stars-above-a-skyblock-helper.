package ephemera.the_stars_above.detection;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

public class CropDetector {
    /**
     * Scans a specified radius around the player for mature crops.
     * @param radius Block radius to scan.
     * @return true if a mature crop is found.
     */
    public static boolean isMatureCropAhead(int radius) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return false;

        BlockPos playerPos = client.player.getBlockPos();
        // Calculate scanning range
        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = playerPos.add(x, y, z);
                    BlockState state = client.world.getBlockState(targetPos);
                    if (isMature(state)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isMature(BlockState state) {
        if (state.getBlock() instanceof CropBlock crop) {
            return crop.isMature(state);
        }
        if (state.isOf(Blocks.SUGAR_CANE)) {
            // Usually if there is sugarcane in the block space above root, it's farmable
            return true;
        }
        if (state.isOf(Blocks.NETHER_WART)) {
            return state.get(Properties.AGE_3) >= 3;
        }
        if (state.isOf(Blocks.PUMPKIN) || state.isOf(Blocks.MELON)) {
            return true;
        }
        return false;
    }
}
