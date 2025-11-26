package com.harsh.pc.worker;

import com.harsh.pc.buffer.BoundedBuffer;
import com.harsh.pc.model.DestinationContainer;
import com.harsh.pc.model.Item;
import com.harsh.pc.model.SourceContainer;

public class RunnerWaitNotify {
    public static void runDemo() {
        int total = 10_000;
        BoundedBuffer<Item> buf = new BoundedBuffer<>(64);
        SourceContainer src = new SourceContainer(total);
        DestinationContainer dst = new DestinationContainer();

        Thread producer = new Thread(() -> {
            try {
                for (Item i : src) buf.put(i);
                buf.put(Item.POISON);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "producer");

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Item i = buf.take();
                    if (i == Item.POISON) break;
                    dst.store(i);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "consumer");

        long start = System.currentTimeMillis();
        producer.start(); consumer.start();
        try { producer.join(30_000); consumer.join(30_000); } catch (InterruptedException ignored) {}
        long dur = System.currentTimeMillis() - start;

        System.out.printf("[wait] produced=%d consumed=%d ms=%d%n",
                src.size(), dst.size(), dur);
    }
}