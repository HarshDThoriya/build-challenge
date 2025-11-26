package com.harsh.pc.buffer;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedBuffer<T> {
    private final Queue<T> q = new ArrayDeque<>();
    private final int capacity;

    public BoundedBuffer(int capacity) { this.capacity = capacity; }

    public synchronized void put(T t) throws InterruptedException {
        while (q.size() == capacity) wait();
        q.add(t);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (q.isEmpty()) wait();
        T t = q.remove();
        notifyAll();
        return t;
    }
}
