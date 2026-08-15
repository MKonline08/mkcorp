package com.mkcorp.module.modules;

import com.mkcorp.module.Category;
import com.mkcorp.module.Module;
import org.lwjgl.glfw.GLFW;

/**
 * Cranks the brightness (gamma) option far past the slider maximum while
 * enabled, then restores your previous setting on disable. Simple and safe:
 * it only touches a local video setting, which is why anticheats cannot see it.
 */
public class FullBright extends Module {

    private static final double BOOSTED_GAMMA = 12.0;

    private double previousGamma = 1.0;

    public FullBright() {
        super("FullBright", "See in the dark", Category.RENDER, GLFW.GLFW_KEY_B);
    }

    @Override
    public void onEnable() {
        previousGamma = client.options.getGamma().getValue();
        client.options.getGamma().setValue(BOOSTED_GAMMA);
    }

    @Override
    public void onDisable() {
        client.options.getGamma().setValue(previousGamma);
    }
}
