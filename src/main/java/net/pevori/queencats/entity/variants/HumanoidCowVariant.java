package net.pevori.queencats.entity.variants;

import java.util.Arrays;
import java.util.Comparator;

public enum HumanoidCowVariant {
    COFFEE(0),
    MILKSHAKE(1),
    MOOSHROOM(2),
    MOOBLOOM(3),
    WOOLY(4);

    private static final HumanoidCowVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(HumanoidCowVariant::getId)).toArray(HumanoidCowVariant[]::new);

    private final int id;

    HumanoidCowVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static HumanoidCowVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
