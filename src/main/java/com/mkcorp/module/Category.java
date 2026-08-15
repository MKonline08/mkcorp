package com.mkcorp.module;

/**
 * Module categories. The color is used for the HUD array list.
 */
public enum Category {
    COMBAT("Combat", 0xFF5555),
    MOVEMENT("Movement", 0x55FFFF),
    RENDER("Render", 0xFFAA00),
    PLAYER("Player", 0x55FF55);

    private final String displayName;
    private final int color;

    Category(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getColor() {
        return color;
    }
}
