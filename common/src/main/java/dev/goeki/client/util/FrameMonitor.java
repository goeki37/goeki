package dev.goeki.client.util;

/** Samples client frame times without allocating per frame. */
public final class FrameMonitor {
    private final long[] samples = new long[120];
    private int index, count;
    private long lastNanos;
    public void frame(long now) {
        if (lastNanos != 0 && now > lastNanos) {
            samples[index] = now - lastNanos;
            index = (index+1) % samples.length;
            if (count < samples.length) count++;
        }
        lastNanos = now;
    }
    public double averageMs() {
        if (count == 0) return 0;
        long sum=0;
        for (int i=0;i<count;i++) sum+=samples[i];
        return sum / (count * 1000000.0);
    }
    public long usedMemoryMb() {
        Runtime rt=Runtime.getRuntime();
        return (rt.totalMemory()-rt.freeMemory()) / (1024L*1024L);
    }
}
