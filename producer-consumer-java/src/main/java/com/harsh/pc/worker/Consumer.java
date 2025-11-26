package com.harsh.pc.worker;

import com.harsh.pc.model.DestinationContainer;
import com.harsh.pc.model.Item;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private final BlockingQueue<Item> queue;
    private final DestinationContainer dest;

    public Consumer(BlockingQueue<Item> queue, DestinationContainer dest) {
        this.queue = queue; this.dest = dest;
    }

    @Override public void run() {
        try {
            while (true) {
                Item i = queue.take();
                if (i == Item.POISON) break;
                dest.store(i);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}