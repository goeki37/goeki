package dev.goeki.client.util;

import java.util.ArrayDeque;
import java.util.Deque;

public final class CpsTracker {
    private static final long WINDOW_MS = 1000L;
    private final Deque<Long> leftClicks = new ArrayDeque<Long>();
    private final Deque<Long> rightClicks = new ArrayDeque<Long>();

    public synchronized void recordLeftClick(long now) { record(leftClicks, now); }
    public synchronized void recordRightClick(long now) { record(rightClicks, now); }

    public synchronized int getLeftCps(long now) { prune(leftClicks, now); return leftClicks.size(); }
    public synchronized int getRightCps(long now) { prune(rightClicks, now); return rightClicks.size(); }

    private void record(Deque<Long> clicks, long now) {
        prune(clicks, now);
        clicks.addLast(now);
    }

    private void prune(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() >= WINDOW_MS) {
            clicks.removeFirst();
        }
    }
}
