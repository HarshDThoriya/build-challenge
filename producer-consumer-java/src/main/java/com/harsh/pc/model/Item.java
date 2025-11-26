package com.harsh.pc.model;

public record Item(long id, String payload) {
    public static final Item POISON = new Item(-1, "POISON");
}
