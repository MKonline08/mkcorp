package com.mkcorp.module.modules;

import com.mkcorp.module.Category;
import com.mkcorp.module.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

/**
 * Sends an on-ground packet once you've fallen far enough to take damage,
 * so the server never sees a damaging fall. Classic NoFall.
 *
 * Note: strict anticheats compare your reported on-ground state against
 * simulated movement, so this can be caught by good movement checks.
 */
public class NoFall extends Module {

    /** Fall distance (blocks) at which we start lying to the server. */
    private static final float TRIGGER_DISTANCE = 2.0f;

    public NoFall() {
        super("NoFall", "Never take fall damage", Category.PLAYER, GLFW.GLFW_KEY_J);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = client.player;
        if (player.fallDistance > TRIGGER_DISTANCE && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        }
    }
}
