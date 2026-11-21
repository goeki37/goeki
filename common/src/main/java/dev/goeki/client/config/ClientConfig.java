package dev.goeki.client.config;

import dev.goeki.client.core.Goeki;
import dev.goeki.client.module.Module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class ClientConfig {
    private static final String ACCENT_KEY = "ui.accent";
    private final File file;

    public ClientConfig(File file) { this.file = file; }

    public void save(String accentHex) throws IOException {
        Properties properties = new Properties();
        properties.setProperty(ACCENT_KEY, accentHex);
        for (Module module : Goeki.getInstance().getModuleManager().getModules()) {
            properties.setProperty("module." + module.getName() + ".enabled", Boolean.toString(module.isEnabled()));
            properties.setProperty("module." + module.getName() + ".key", Integer.toString(module.getKeyCode()));
        }
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Could not create config directory: " + parent);
        }
        FileOutputStream output = new FileOutputStream(file);
        try { properties.store(output, "Goeki Client settings"); }
        finally { output.close(); }
    }

    public String load(String defaultAccentHex) throws IOException {
        if (!file.isFile()) return defaultAccentHex;
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream(file);
        try { properties.load(input); }
        finally { input.close(); }

        for (Module module : Goeki.getInstance().getModuleManager().getModules()) {
            String enabled = properties.getProperty("module." + module.getName() + ".enabled");
            String key = properties.getProperty("module." + module.getName() + ".key");
            if (enabled != null) module.setEnabled(Boolean.parseBoolean(enabled));
            if (key != null) {
                try { module.setKeyCode(Integer.parseInt(key)); }
                catch (NumberFormatException ignored) { }
            }
        }
        return properties.getProperty(ACCENT_KEY, defaultAccentHex);
    }
}
