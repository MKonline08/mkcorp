package com.mkcorp.module;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

/**
 * Base class for every MKCorp module. Each module owns a real keybinding
 * (so it shows up in Options -> Controls and can be rebound by the user)
 * plus onEnable/onDisable/onTick hooks.
 */
public abstract class Module {

    protected final MinecraftClient client = MinecraftClient.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private final KeyBinding keyBinding;
    private boolean enabled;

    protected Module(String name, String description, Category category, int defaultKey) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mkcorp." + name.toLowerCase(),
                InputUtil.Type.KEYSYM,
                defaultKey,
                "key.categories.mkcorp"
        ));
    }

    /** Called every client tick from ModuleManager. Handles the toggle key. */
    public void handleKeybind() {
        while (keyBinding.wasPressed()) {
            toggle();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    /** Runs once when the module is switched on. */
    public void onEnable() {
    }

    /** Runs once when the module is switched off. Restore whatever you changed here. */
    public void onDisable() {
    }

    /** Runs every client tick while enabled. Player/world are guaranteed non-null. */
    public void onTick() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
