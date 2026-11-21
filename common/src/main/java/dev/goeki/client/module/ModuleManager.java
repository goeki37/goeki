package dev.goeki.client.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<Module>();

    public void register(Module module) {
        if (module == null || getByName(module.getName()) != null) {
            throw new IllegalArgumentException("Module is null or already registered");
        }
        modules.add(module);
    }

    public Module getByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
        return null;
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) module.onTick();
        }
    }

    public void registerDefaults() {
        if (!modules.isEmpty()) return;
        register(new Module("HUD", "Displays Goeki's lightweight status overlay.", Category.HUD, 0) { });
        register(new Module("Performance Mode", "Disables optional Goeki visual effects.", Category.UTILITY, 0) { });
        register(new Module("Keystrokes", "Shows movement and mouse-button input.", Category.HUD, 0) { });
        register(new Module("FPS", "Displays the current frame rate.", Category.HUD, 0) { });
        register(new Module("CPS", "Displays left and right clicks per second.", Category.HUD, 0) { });
        register(new Module("Ping", "Displays connection latency when available.", Category.HUD, 0) { });
        register(new Module("Armor", "Shows equipped armor durability.", Category.HUD, 0) { });
        register(new Module("Potions", "Shows active potion effects.", Category.HUD, 0) { });
        register(new Module("Direction", "Shows compass heading.", Category.HUD, 0) { });
        register(new Module("Memory", "Shows JVM memory use and frame time.", Category.HUD, 0) { });
        register(new Module("Fullbright", "Client-side gamma boost; server rules apply.", Category.VISUAL, 0) { });
        for (Module module : modules) {
            if (module.getCategory() == Category.HUD) module.setEnabled(true);
        }
    }
}
