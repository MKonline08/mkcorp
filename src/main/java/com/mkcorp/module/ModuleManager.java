package com.mkcorp.module;

import com.mkcorp.module.modules.ESP;
import com.mkcorp.module.modules.Flight;
import com.mkcorp.module.modules.FullBright;
import com.mkcorp.module.modules.KillAura;
import com.mkcorp.module.modules.NoFall;
import com.mkcorp.module.modules.Speed;
import com.mkcorp.module.modules.Sprint;
import com.mkcorp.module.modules.Xray;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Registers all modules and drives their keybinds + per-tick logic.
 */
public final class ModuleManager {

    private static final List<Module> MODULES = new ArrayList<>();

    private ModuleManager() {
    }

    public static void init() {
        register(new KillAura());
        register(new Flight());
        register(new Speed());
        register(new NoFall());
        register(new Sprint());
        register(new FullBright());
        register(new ESP());
        register(new Xray());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Module module : MODULES) {
                module.handleKeybind();
            }
            // Only run module logic while actually in a world.
            if (client.player == null || client.world == null) {
                return;
            }
            for (Module module : MODULES) {
                if (module.isEnabled()) {
                    module.onTick();
                }
            }
        });
    }

    private static void register(Module module) {
        MODULES.add(module);
    }

    public static List<Module> getModules() {
        return MODULES;
    }

    /** Enabled modules, alphabetical, for the HUD array list. */
    public static List<Module> getEnabled() {
        List<Module> enabled = new ArrayList<>();
        for (Module module : MODULES) {
            if (module.isEnabled()) {
                enabled.add(module);
            }
        }
        enabled.sort(Comparator.comparing(Module::getName));
        return enabled;
    }

    public static <T extends Module> T get(Class<T> type) {
        for (Module module : MODULES) {
            if (type.isInstance(module)) {
                return type.cast(module);
            }
        }
        return null;
    }
}
