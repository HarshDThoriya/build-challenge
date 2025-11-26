package com.harsh.pc.worker;

import com.harsh.pc.model.DestinationContainer;
import com.harsh.pc.model.Item;
import com.harsh.pc.model.SourceContainer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RunnerBlockingQueue {
    /**
     * Runs the BlockingQueue demo end-to-end.
     * <p>Adjust {@code total} or queue capacity to explore behavior under different contention levels.</p>
     */
    public static void runDemo() {
        // Number of items to produce/consume in this demo.
        // Using an underscore improves readability: 10_000 == 10000.
        int total = 10_000;

        // Bounded FIFO queue shared between producer and consumer.
        // Capacity 128 provides a balance: not too large (to hide coordination), not too small (to starve throughput).
        BlockingQueue<Item> q = new ArrayBlockingQueue<>(128);
        
        // Synthetic data source [0..total) and a thread-safe destination accumulator.
        SourceContainer src = new SourceContainer(total);
        DestinationContainer dst = new DestinationContainer();

        // Producer reads from src and puts into q; Consumer takes from q and stores into dst.
        // Thread names help when debugging stack traces.
        Thread p = new Thread(new Producer(src, q), "producer");
        Thread c = new Thread(new Consumer(q, dst), "consumer");
        long start = System.currentTimeMillis();

        // Start both threads; order does not matter because queue operations are blocking.
        p.start(); c.start();

        try { 
            p.join(30_000); 
            c.join(30_000); 
        } catch (InterruptedException ignored) {}
        
        long dur = System.currentTimeMillis() - start;

        System.out.printf("[queue] produced=%d consumed=%d ms=%d%n",
                src.size(), dst.size(), dur);
    }
}
