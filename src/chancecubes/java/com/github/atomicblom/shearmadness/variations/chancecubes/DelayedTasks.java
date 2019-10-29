package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.google.common.collect.Queues;

import java.util.PriorityQueue;

public final class DelayedTasks {

    private static final PriorityQueue<DelayedTask> tasks = Queues.newPriorityQueue();

    public static void addDelayedTask(long expectedTick, DelayedTask.Action task) {
        tasks.add(new DelayedTask(expectedTick, task));
    }

    public static void processDelayedTasks(long currentTick) {
        DelayedTask peekTask;
        while ((peekTask = tasks.peek()) != null && peekTask.getExpectedTick() <= currentTick) {
            final DelayedTask task = tasks.poll();
            task.executeJob();
        }
    }

    private DelayedTasks() {}
}
