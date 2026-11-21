package dev.goeki.client.setting;

public abstract class Setting<T> {
    private final String name;
    private T value;

    protected Setting(String name, T initialValue) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("name");
        this.name = name;
        this.value = initialValue;
    }

    public String getName() { return name; }
    public T getValue() { return value; }
    public void setValue(T value) { this.value = validate(value); }
    protected abstract T validate(T value);
}
