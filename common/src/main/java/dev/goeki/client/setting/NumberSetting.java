package dev.goeki.client.setting;

public final class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;

    public NumberSetting(String name, double value, double min, double max) {
        super(name, value);
        if (min > max) throw new IllegalArgumentException("min > max");
        this.min = min;
        this.max = max;
        setValue(value);
    }

    @Override protected Double validate(Double value) {
        if (value == null || Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid number");
        }
        return Math.max(min, Math.min(max, value));
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
}
