package net.pevori.queencats.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum HumanoidCatVariant {
    WHITE(0),
    BLACK(1),
    CALICO(2),
    CALLAS(3);


    private static final HumanoidCatVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
    comparingInt(HumanoidCatVariant::getId)).toArray(HumanoidCatVariant[]::new);

    private final int id;

    HumanoidCatVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static HumanoidCatVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}