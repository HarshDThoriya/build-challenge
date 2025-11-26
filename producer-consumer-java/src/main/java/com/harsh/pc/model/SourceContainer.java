package com.harsh.pc.model;

import java.util.Iterator;
import java.util.List;

public class SourceContainer implements Iterable<Item> {
    private final List<Item> items;
    public SourceContainer(int count) {
        this.items = java.util.stream.LongStream.range(0, count)
                .mapToObj(i -> new Item(i, "data-" + i)).toList();
    }
    @Override public Iterator<Item> iterator() { return items.iterator(); }
    public int size() { return items.size(); }
}