package com.mkcorp.module.modules;

import com.mkcorp.module.Category;
import com.mkcorp.module.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

/**
 * Keeps you sprinting whenever you're moving forward, without needing
 * to double-tap W or hold the sprint key. Practically undetectable since
 * sprinting is normal gameplay — it just never lets go.
 */
public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Sprint automatically", Category.MOVEMENT, GLFW.GLFW_KEY_N);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = client.player;
        if (player.input.movementForward > 0.0f && !player.isSprinting()
                && !player.isSneaking() && !player.hasVehicle()) {
            player.setSprinting(true);
        }
    }
}
