package net.pevori.queencats.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum PrincessCatVariant {
    WHITE(0),
    BLACK(1),
    CALICO(2),
    CALLAS(3);


    private static final PrincessCatVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
    comparingInt(PrincessCatVariant::getId)).toArray(PrincessCatVariant[]::new);

    private final int id;

    PrincessCatVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PrincessCatVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}