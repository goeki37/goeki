package dev.goeki.client.setting;

public final class ColorSetting extends Setting<String> {
    public ColorSetting(String name, String hex) { super(name, hex); }

    @Override protected String validate(String value) {
        if (value == null) throw new IllegalArgumentException("Color cannot be null");
        String normalized = value.startsWith("#") ? value.substring(1) : value;
        if (!normalized.matches("(?i)[0-9a-f]{6}")) {
            throw new IllegalArgumentException("Expected a six-digit HEX color, e.g. #A56BFF");
        }
        return "#" + normalized.toUpperCase();
    }
}
