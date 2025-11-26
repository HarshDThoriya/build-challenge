package com.harsh.pc.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DestinationContainer {
    private final Set<Long> received = Collections.synchronizedSet(new HashSet<>());
    public void store(Item item) { received.add(item.id()); }
    public int size() { return received.size(); }
    public Set<Long> ids() { synchronized (received) { return Set.copyOf(received); } }
}