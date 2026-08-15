package com.mkcorp.module.modules;

import com.mkcorp.module.Category;
import com.mkcorp.module.Module;
import com.mkcorp.render.RenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

/**
 * Draws wireframe boxes around entities, visible through walls.
 * Color code: players red, hostile mobs orange, items green, everything else white.
 *
 * Rendering is purely client-side — no anticheat can detect ESP itself,
 * though staring at people through walls on camera is on you.
 */
public class ESP extends Module {

    private static final boolean THROUGH_WALLS = true;

    public ESP() {
        super("ESP", "See entities through walls", Category.RENDER, GLFW.GLFW_KEY_U);

        WorldRenderEvents.LAST.register(context -> {
            if (!isEnabled() || client.world == null || client.player == null) {
                return;
            }

            for (Entity entity : client.world.getEntities()) {
                if (entity == client.player || entity.isRemoved()) {
                    continue;
                }

                float[] color = colorFor(entity);
                RenderUtils.drawOutlinedBox(
                        context.matrixStack(),
                        entity.getBoundingBox(),
                        context.camera().getPos(),
                        color[0], color[1], color[2], 1.0f,
                        THROUGH_WALLS
                );
            }
        });
    }

    private static float[] colorFor(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return new float[]{1.0f, 0.25f, 0.25f};
        }
        if (entity instanceof HostileEntity) {
            return new float[]{1.0f, 0.6f, 0.1f};
        }
        if (entity instanceof ItemEntity) {
            return new float[]{0.3f, 1.0f, 0.4f};
        }
        return new float[]{1.0f, 1.0f, 1.0f};
    }
}
