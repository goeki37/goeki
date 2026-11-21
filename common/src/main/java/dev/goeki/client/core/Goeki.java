package dev.goeki.client.core;

import dev.goeki.client.module.ModuleManager;
import dev.goeki.client.util.CpsTracker;

public final class Goeki {
    public static final String NAME = "Goeki Client";
    public static final String VERSION = "0.5.0";
    public static final String BRAND = "LIGHTWEIGHT. CLEAN. COMPETITIVE.";

    private static final Goeki INSTANCE = new Goeki();

    private final ModuleManager moduleManager = new ModuleManager();
    private final CpsTracker cpsTracker = new CpsTracker();

    private Goeki() {
        moduleManager.registerDefaults();
    }

    public static Goeki getInstance() {
        return INSTANCE;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CpsTracker getCpsTracker() {
        return cpsTracker;
    }
}
