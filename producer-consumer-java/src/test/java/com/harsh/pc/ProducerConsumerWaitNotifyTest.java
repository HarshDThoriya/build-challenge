package com.harsh.pc;

import com.harsh.pc.buffer.BoundedBuffer;
import com.harsh.pc.model.DestinationContainer;
import com.harsh.pc.model.Item;
import com.harsh.pc.model.SourceContainer;

import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProducerConsumerWaitNotifyTest {
    @Test
    void endToEnd_noLossNoDup() throws Exception {
        int total = 2_000;
        BoundedBuffer<Item> buf = new BoundedBuffer<>(3);
        SourceContainer src = new SourceContainer(total);
        DestinationContainer dst = new DestinationContainer();

        Thread p = new Thread(() -> {
            try {
                for (Item i : src) buf.put(i);
                buf.put(Item.POISON);
            } catch (InterruptedException ignored) {}
        });
        Thread c = new Thread(() -> {
            try {
                while (true) {
                    Item i = buf.take();
                    if (i == Item.POISON) break;
                    dst.store(i);
                }
            } catch (InterruptedException ignored) {}
        });

        p.start(); c.start();
        p.join(10_000); c.join(10_000);

        assertEquals(total, dst.size());
        var expected = LongStream.range(0, total).boxed().collect(java.util.stream.Collectors.toSet());
        assertEquals(expected, dst.ids());
    }
}