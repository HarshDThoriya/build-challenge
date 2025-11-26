package com.harsh.pc.worker;

import com.harsh.pc.buffer.BoundedBuffer;
import com.harsh.pc.model.DestinationContainer;
import com.harsh.pc.model.Item;
import com.harsh.pc.model.SourceContainer;

public class RunnerWaitNotify {
    /**
     * Runs the wait/notify demo end-to-end.
     * <p>Adjust {@code total} or buffer capacity to explore different contention profiles.</p>
     */
    public static void runDemo() {
        // Total number of items in this run (0..total-1).
        int total = 10_000;

        // Custom bounded buffer used instead of BlockingQueue.
        // BoundedBuffer<T> is a simple monitor-based queue with put()/take() blocking.
        BoundedBuffer<Item> buf = new BoundedBuffer<>(64);

        // Synthetic source and thread-safe destination accumulator.
        SourceContainer src = new SourceContainer(total);
        DestinationContainer dst = new DestinationContainer();

        // --- Producer thread ---
        // Puts each item into the buffer; posts a poison pill when done.
        Thread producer = new Thread(() -> {
            try {
                for (Item i : src) buf.put(i); // blocks if buffer is full
                buf.put(Item.POISON);
            } catch (InterruptedException e) { 
                // Preserve interrupt status so higher layers can react if needed.
                Thread.currentThread().interrupt(); 
            }
        }, "producer");

        // --- Consumer thread ---
        // Takes items until the poison pill is observed.
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Item i = buf.take(); // blocks if buffer is empty
                    if (i == Item.POISON) break; // graceful shutdown
                    dst.store(i);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "consumer");

        long start = System.currentTimeMillis();

        // Start both threads; either can start first because operations are blocking/synchronized.
        producer.start(); consumer.start();

        try { 
            producer.join(30_000); 
            consumer.join(30_000); 
        } catch (InterruptedException ignored) {}
        
        long dur = System.currentTimeMillis() - start;

        System.out.printf("[wait] produced=%d consumed=%d ms=%d%n",
                src.size(), dst.size(), dur);
    }
}