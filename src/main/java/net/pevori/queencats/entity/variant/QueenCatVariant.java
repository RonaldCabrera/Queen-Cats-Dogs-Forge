package net.pevori.queencats.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum QueenCatVariant {
    WHITE(0),
    BLACK(1);

    private static final QueenCatVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
    comparingInt(QueenCatVariant::getId)).toArray(QueenCatVariant[]::new);

    private final int id;

    QueenCatVariant(int id) {
    this.id = id;
    }

    public int getId() {
    return this.id;
    }

    public static QueenCatVariant byId(int id) {
    return BY_ID[id % BY_ID.length];
    }
}
