package com.mkcorp.module.modules;

import com.mkcorp.module.Category;
import com.mkcorp.module.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * Modest ground-speed boost with optional auto-jump (bhop style).
 * The boost is capped so it accelerates up to a ceiling instead of
 * compounding forever.
 *
 * Deliberately tuned mild: big speed multipliers are the single fastest
 * way to trip movement checks.
 */
public class Speed extends Module {

    /** Applied per tick while below the horizontal speed ceiling. */
    private static final double MULTIPLIER = 1.18;
    /** Horizontal blocks/tick ceiling. Vanilla sprint-jump peaks around 0.28-0.33. */
    private static final double MAX_HORIZONTAL = 0.50;
    private static final boolean AUTO_JUMP = true;

    public Speed() {
        super("Speed", "Run faster on the ground", Category.MOVEMENT, GLFW.GLFW_KEY_H);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = client.player;
        if (!player.isOnGround()) {
            return;
        }

        boolean moving = player.input.movementForward != 0.0f || player.input.movementSideways != 0.0f;
        if (!moving) {
            return;
        }

        Vec3d velocity = player.getVelocity();
        double horizontal = Math.hypot(velocity.x, velocity.z);
        if (horizontal < MAX_HORIZONTAL) {
            player.setVelocity(velocity.x * MULTIPLIER, velocity.y, velocity.z * MULTIPLIER);
        }

        if (AUTO_JUMP) {
            player.jump();
        }
    }
}
