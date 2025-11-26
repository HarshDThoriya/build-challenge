package com.harsh.pc.worker;

import com.harsh.pc.model.DestinationContainer;
import com.harsh.pc.model.Item;
import com.harsh.pc.model.SourceContainer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RunnerBlockingQueue {
    public static void runDemo() {
        int total = 10_000;
        BlockingQueue<Item> q = new ArrayBlockingQueue<>(128);
        SourceContainer src = new SourceContainer(total);
        DestinationContainer dst = new DestinationContainer();

        Thread p = new Thread(new Producer(src, q), "producer");
        Thread c = new Thread(new Consumer(q, dst), "consumer");
        long start = System.currentTimeMillis();
        p.start(); c.start();
        try { p.join(30_000); c.join(30_000); } catch (InterruptedException ignored) {}
        long dur = System.currentTimeMillis() - start;

        System.out.printf("[queue] produced=%d consumed=%d ms=%d%n",
                src.size(), dst.size(), dur);
    }
}
