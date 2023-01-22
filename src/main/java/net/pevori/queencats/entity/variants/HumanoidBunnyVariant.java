package net.pevori.queencats.entity.variants;

import java.util.Arrays;
import java.util.Comparator;

public enum HumanoidBunnyVariant {
    COCOA(0),
    SNOW(1),
    SUNDAY(2),
    STRAWBERRY(3);

    private static final HumanoidBunnyVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(HumanoidBunnyVariant::getId)).toArray(HumanoidBunnyVariant[]::new);

    private final int id;

    HumanoidBunnyVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static HumanoidBunnyVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
