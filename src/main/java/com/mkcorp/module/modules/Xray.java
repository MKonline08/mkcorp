package com.mkcorp.module.modules;

import com.mkcorp.module.Category;
import com.mkcorp.module.Module;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

/**
 * Hides every block that isn't on the whitelist via the
 * AbstractBlockStateMixin render hook, leaving ores and valuables
 * floating in empty space.
 *
 * Two practical notes:
 *  - Caves will be pitch black; run this together with FullBright.
 *  - Some servers send fake ore placements (anti-xray obfuscation).
 *    No client-side xray can see through that; it's a server defense.
 */
public class Xray extends Module {

    /** Blocks that stay visible while xray is on. Extend to taste. */
    private static final Set<Block> VISIBLE = new HashSet<>(Set.of(
            Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE,
            Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.ANCIENT_DEBRIS, Blocks.NETHER_QUARTZ_ORE,
            Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST,
            Blocks.BARREL, Blocks.SPAWNER,
            Blocks.END_PORTAL_FRAME, Blocks.END_PORTAL,
            Blocks.NETHER_PORTAL, Blocks.BEDROCK
    ));

    private static Xray instance;

    public Xray() {
        super("Xray", "See ores through blocks", Category.RENDER, GLFW.GLFW_KEY_X);
        instance = this;
    }

    /** Called from AbstractBlockStateMixin on every block's render-type query. */
    public static boolean shouldHide(AbstractBlock.AbstractBlockState state) {
        return instance != null && instance.isEnabled() && !VISIBLE.contains(state.getBlock());
    }

    @Override
    public void onEnable() {
        reloadChunks();
    }

    @Override
    public void onDisable() {
        reloadChunks();
    }

    private void reloadChunks() {
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
    }
}
