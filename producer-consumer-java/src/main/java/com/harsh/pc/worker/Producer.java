package com.harsh.pc.worker;

import com.harsh.pc.model.Item;
import com.harsh.pc.model.SourceContainer;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private final SourceContainer source;
    private final BlockingQueue<Item> queue;

    public Producer(SourceContainer source, BlockingQueue<Item> queue) {
        this.source = source; this.queue = queue;
    }

    @Override public void run() {
        try {
            for (Item i : source) queue.put(i);
            queue.put(Item.POISON); // completion signal
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}