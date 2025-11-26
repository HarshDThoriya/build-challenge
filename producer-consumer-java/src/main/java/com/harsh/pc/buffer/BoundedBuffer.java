package com.harsh.pc.buffer;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A minimal bounded buffer implemented with intrinsic locking.
 * Uses the classic wait/notifyAll pattern:
 * - put() blocks when the buffer is full
 * - take() blocks when the buffer is empty
 * This class is intentionally simple for clarity in the assignment.
 */
public class BoundedBuffer<T> {
    private final Queue<T> q = new ArrayDeque<>();
    private final int capacity;

    /**
     * @param capacity maximum number of elements that can be held.
     */
    public BoundedBuffer(int capacity) { this.capacity = capacity; }

    /**
     * Inserts an element, blocking if the buffer is currently full.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public synchronized void put(T t) throws InterruptedException {
        while (q.size() == capacity) wait(); // Buffer full: release the monitor and suspend until notified.
        q.add(t);

        // We just made the queue non-empty -> wake up takers.
        notifyAll();
    }

    /**
     * Takes the next element, blocking if the buffer is currently empty.
     * @return next element in FIFO order.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public synchronized T take() throws InterruptedException {
        while (q.isEmpty()) wait(); // Buffer empty: release the monitor and suspend until notified.
        T t = q.remove();

        // We just made space -> wake up putters.
        notifyAll();
        return t;
    }
}
