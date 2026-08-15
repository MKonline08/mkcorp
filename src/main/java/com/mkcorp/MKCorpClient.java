package com.mkcorp;

import com.mkcorp.module.ModuleManager;
import com.mkcorp.ui.HudOverlay;
import net.fabricmc.api.ClientModInitializer;

public class MKCorpClient implements ClientModInitializer {

    public static final String MOD_ID = "mkcorp";
    public static final String NAME = "MKCorp";
    public static final String VERSION = "0.1.0";

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        HudOverlay.init();
    }
}
