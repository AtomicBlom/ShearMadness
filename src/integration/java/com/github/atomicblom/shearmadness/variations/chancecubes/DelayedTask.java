package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.utility.Logger;

import java.util.concurrent.Callable;

public class DelayedTask implements Comparable<DelayedTask> {
    private final long expectedTick;
    private final Action task;

    public DelayedTask(long expectedTick, Action task) {

        this.expectedTick = expectedTick;
        this.task = task;
    }

    public void executeJob() {
        try {
            task.execute();
        } catch (Exception e) {
            Logger.warning("Failed to execute Delayed Task", e);
        }
    }

    public long getExpectedTick() {
        return expectedTick;
    }

    @Override
    public int compareTo(DelayedTask delayedTask) {
        if (delayedTask.expectedTick > expectedTick) {
            return -1;
        } else if (delayedTask.expectedTick == expectedTick) {
            return 0;
        } else {
            return 1;
        }
    }

    public interface Action {
        void execute();
    }
}
