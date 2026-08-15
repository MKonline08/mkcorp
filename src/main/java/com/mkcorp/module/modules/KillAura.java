package com.mkcorp.module.modules;

import com.mkcorp.module.Category;
import com.mkcorp.module.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

/**
 * Attacks the nearest living entity in range, snapping your aim onto it.
 *
 * Tuning notes:
 *  - RANGE 4.2 is above vanilla reach (3.0), so strict anticheats CAN flag
 *    the extra reach. Drop it to 3.0 if you want to look fully legit.
 *  - The cooldown jitter (~8-10 attacks/sec) sits under the vanilla 1.9+
 *    attack-speed feel and avoids robotic fixed-interval timing.
 *  - Instant aim snaps are the most obvious part of any aura. If you want
 *    subtlety, this is the first module to soften.
 */
public class KillAura extends Module {

    private static final double RANGE = 4.2;
    private static final Random RANDOM = new Random();

    private int cooldown;

    public KillAura() {
        super("KillAura", "Auto-attack nearby entities", Category.COMBAT, GLFW.GLFW_KEY_K);
    }

    @Override
    public void onTick() {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        ClientPlayerEntity player = client.player;
        LivingEntity target = findNearestTarget(player);
        if (target == null) {
            return;
        }

        aimAt(player, target);

        if (client.interactionManager != null) {
            client.interactionManager.attackEntity(player, target);
            player.swingHand(Hand.MAIN_HAND);
        }

        // 2-4 ticks between swings ~= 5-10 attacks per second, jittered.
        cooldown = 2 + RANDOM.nextInt(3);
    }

    private LivingEntity findNearestTarget(ClientPlayerEntity player) {
        LivingEntity best = null;
        double bestDistance = RANGE;

        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof LivingEntity living)) {
                continue;
            }
            if (entity == player || !living.isAlive() || player.isTeammate(living)) {
                continue;
            }
            double distance = player.distanceTo(living);
            if (distance < bestDistance) {
                best = living;
                bestDistance = distance;
            }
        }
        return best;
    }

    private void aimAt(ClientPlayerEntity player, LivingEntity target) {
        Vec3d eye = player.getEyePos();
        Vec3d center = target.getBoundingBox().getCenter();

        double dx = center.x - eye.x;
        double dy = center.y - eye.y;
        double dz = center.z - eye.z;
        double horizontal = Math.hypot(dx, dz);

        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, horizontal)));

        player.setYaw(yaw);
        player.setPitch(pitch);
    }

    @Override
    public void onDisable() {
        cooldown = 0;
    }
}
