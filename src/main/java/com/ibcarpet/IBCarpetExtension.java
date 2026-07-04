package com.ibcarpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;

import java.util.Map;

public class IBCarpetExtension implements CarpetExtension {
    // Create a settings manager for your specific extension
    // The string is your mod's version, usually fetched from fabric.mod.json
    private static final IBCarpetExtension INSTANCE = new IBCarpetExtension();

//    private static final SettingsManager settingsManager = new SettingsManager("1.0.0", "ibcarpet", "IBCarpet");

    public static void loadExtension() {
        // Register your extension with Carpet
        CarpetServer.manageExtension(INSTANCE);
    }


    @Override
    public void onGameStarted() {
        // Parse your settings class so the rules show up in-game
        CarpetServer.settingsManager.parseSettingsClass(IBCarpetSettings.class);
    }

    @Override
    public String version() {
        return "IBCarpet";
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return Translations.getTranslationFromResourcePath(String.format("assets/ibcarpet/locale/%s.json", lang));
    }

}
