package com.harsh.pc;

import com.harsh.pc.model.DestinationContainer;
import com.harsh.pc.model.Item;
import com.harsh.pc.model.SourceContainer;
import com.harsh.pc.worker.Consumer;
import com.harsh.pc.worker.Producer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

public class ProducerConsumerBlockingQueueTest {
    @Test
    void endToEnd_noLossNoDup() throws Exception {
        int total = 2_000;
        BlockingQueue<Item> q = new ArrayBlockingQueue<>(2); // force blocking
        SourceContainer src = new SourceContainer(total);
        DestinationContainer dst = new DestinationContainer();

        Thread p = new Thread(new Producer(src, q));
        Thread c = new Thread(new Consumer(q, dst));
        p.start(); c.start();
        p.join(10_000); c.join(10_000);

        assertEquals(total, dst.size());
        var expected = LongStream.range(0, total).boxed().collect(java.util.stream.Collectors.toSet());
        assertEquals(expected, dst.ids());
    }
}