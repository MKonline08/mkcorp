package com.mkcorp.module.modules;

import com.mkcorp.module.Category;
import com.mkcorp.module.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * Velocity-based flight. WASD moves horizontally at SPEED, jump goes up,
 * sneak goes down, no input hovers in place.
 *
 * Heads up: blatant movement module. Any server-side anticheat with movement
 * analysis will flag this quickly. Fine on your own server or anarchy servers.
 */
public class Flight extends Module {

    /** Horizontal blocks/tick. 0.35 is quick; vanilla sprint-jump is ~0.28. */
    private static final double SPEED = 0.35;
    private static final double VERTICAL_SPEED = 0.30;

    public Flight() {
        super("Flight", "Fly like creative mode", Category.MOVEMENT, GLFW.GLFW_KEY_G);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = client.player;

        float forward = player.input.movementForward;
        float sideways = player.input.movementSideways;

        // Horizontal unit vectors from the player's yaw.
        Vec3d forwardVec = Vec3d.fromPolar(0.0f, player.getYaw());
        Vec3d leftVec = new Vec3d(forwardVec.z, 0.0, -forwardVec.x);

        Vec3d velocity = forwardVec.multiply(forward * SPEED)
                .add(leftVec.multiply(sideways * SPEED));

        double y = 0.0;
        if (client.options.jumpKey.isPressed()) {
            y = VERTICAL_SPEED;
        } else if (client.options.sneakKey.isPressed()) {
            y = -VERTICAL_SPEED;
        }

        player.setVelocity(velocity.x, y, velocity.z);
    }

    @Override
    public void onDisable() {
        // Stop dead instead of flinging the player on toggle off.
        if (client.player != null) {
            client.player.setVelocity(Vec3d.ZERO);
        }
    }
}
