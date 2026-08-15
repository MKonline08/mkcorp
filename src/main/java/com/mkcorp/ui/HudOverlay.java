package com.mkcorp.ui;

import com.mkcorp.MKCorpClient;
import com.mkcorp.module.Module;
import com.mkcorp.module.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

/**
 * Top-left HUD: watermark plus the classic array list of enabled modules,
 * colored by category.
 */
public final class HudOverlay {

    private HudOverlay() {
    }

    public static void init() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.options.hudHidden) {
                return;
            }

            int x = 4;
            int y = 4;

            drawContext.drawTextWithShadow(client.textRenderer,
                    MKCorpClient.NAME + " v" + MKCorpClient.VERSION, x, y, 0x55FFFF);
            y += 12;

            for (Module module : ModuleManager.getEnabled()) {
                drawContext.drawTextWithShadow(client.textRenderer,
                        module.getName(), x, y, module.getCategory().getColor());
                y += 10;
            }
        });
    }
}
