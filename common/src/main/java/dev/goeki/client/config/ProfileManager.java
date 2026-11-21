package dev.goeki.client.config;

import dev.goeki.client.core.Goeki;
import dev.goeki.client.module.Module;
import dev.goeki.client.setting.Setting;
import java.io.*;
import java.util.*;

/** Persistent, user-named profiles. No network access or telemetry. */
public final class ProfileManager {
    private final File directory;
    private String active = "default";
    private String accent = "#A970FF";
    private int hudX = 6, hudY = 6;

    public ProfileManager(File directory) { this.directory = directory; }
    public String getActive() { return active; }
    public String getAccent() { return accent; }
    public int getHudX() { return hudX; }
    public int getHudY() { return hudY; }
    public void setHudPosition(int x, int y) { hudX = Math.max(0,x); hudY = Math.max(0,y); }
    public void setAccent(String hex) {
        if (hex == null) return;
        String h = hex.trim();
        if (!h.startsWith("#")) h = "#" + h;
        if (h.matches("(?i)#[0-9a-f]{6}")) accent = h.toUpperCase(Locale.ROOT);
    }
    public int accentRgb() { return Integer.parseInt(accent.substring(1), 16); }
    private File file(String name) {
        if (name == null || !name.matches("[A-Za-z0-9_-]{1,32}")) throw new IllegalArgumentException("Invalid profile name");
        return new File(directory, name + ".properties");
    }
    public void save() throws IOException {
        File target = file(active);
        if (!directory.isDirectory() && !directory.mkdirs()) throw new IOException("Cannot create profile directory");
        Properties p = new Properties();
        p.setProperty("ui.accent", accent);
        p.setProperty("hud.x", Integer.toString(hudX));
        p.setProperty("hud.y", Integer.toString(hudY));
        for (Module m : Goeki.getInstance().getModuleManager().getModules()) {
            String key = "module." + m.getName() + ".";
            p.setProperty(key+"enabled", Boolean.toString(m.isEnabled()));
            p.setProperty(key+"key", Integer.toString(m.getKeyCode()));
        }
        File temp = new File(directory, active + ".tmp");
        try (OutputStream out = new FileOutputStream(temp)) { p.store(out, "Goeki Client profile"); }
        if (target.exists() && !target.delete()) throw new IOException("Cannot replace profile");
        if (!temp.renameTo(target)) throw new IOException("Cannot finalize profile");
    }
    public void load(String name) throws IOException {
        File source = file(name);
        active = name;
        if (!source.isFile()) return;
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(source)) { p.load(in); }
        setAccent(p.getProperty("ui.accent", accent));
        hudX = parseNonnegative(p.getProperty("hud.x"), hudX);
        hudY = parseNonnegative(p.getProperty("hud.y"), hudY);
        for (Module m : Goeki.getInstance().getModuleManager().getModules()) {
            String key = "module." + m.getName() + ".";
            String enabled = p.getProperty(key+"enabled");
            if (enabled != null) m.setEnabled(Boolean.parseBoolean(enabled));
            String binding = p.getProperty(key+"key");
            if (binding != null) { try { m.setKeyCode(Integer.parseInt(binding)); } catch (NumberFormatException ignored) { } }
        }
    }
    private int parseNonnegative(String value, int fallback) {
        try { return Math.max(0,Integer.parseInt(value)); } catch (Exception ignored) { return fallback; }
    }
    public List<String> list() {
        List<String> result = new ArrayList<String>();
        File[] files = directory.listFiles();
        if (files != null) for (File f : files) {
            String n=f.getName();
            if (n.endsWith(".properties")) result.add(n.substring(0,n.length()-11));
        }
        Collections.sort(result);
        return result;
    }
}
