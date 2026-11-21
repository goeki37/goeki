package dev.goeki.client.setting;

public final class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, boolean initialValue) { super(name, initialValue); }
    @Override protected Boolean validate(Boolean value) {
        if (value == null) throw new IllegalArgumentException("Boolean setting cannot be null");
        return value;
    }
}
