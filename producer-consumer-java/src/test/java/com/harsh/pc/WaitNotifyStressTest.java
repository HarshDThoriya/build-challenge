package com.harsh.pc;

import com.harsh.pc.buffer.BoundedBuffer;
import com.harsh.pc.model.DestinationContainer;
import com.harsh.pc.model.Item;
import com.harsh.pc.model.SourceContainer;
import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WaitNotifyStressTest {

    @Test
    void largeVolume_noLossNoDup_withSmallBuffer() throws Exception {
        int total = 40_000;
        BoundedBuffer<Item> buf = new BoundedBuffer<>(2);
        SourceContainer src = new SourceContainer(total);
        DestinationContainer dst = new DestinationContainer();

        Thread producer = new Thread(() -> {
            try {
                for (Item i : src) buf.put(i);
                buf.put(Item.POISON);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Item i = buf.take();
                    if (i == Item.POISON) break;
                    dst.store(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");

        producer.start(); consumer.start();
        producer.join(20_000); consumer.join(20_000);

        assertEquals(total, dst.size());
        var expected = LongStream.range(0, total).boxed().collect(java.util.stream.Collectors.toSet());
        assertEquals(expected, dst.ids());
    }
}
