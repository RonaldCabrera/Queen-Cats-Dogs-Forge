package net.pevori.queencats.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum HumanoidDogVariant {
    SHIRO(0),
    HUSKY(1),
    CREAM(2);

    private static final HumanoidDogVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
    comparingInt(HumanoidDogVariant::getId)).toArray(HumanoidDogVariant[]::new);

    private final int id;

    HumanoidDogVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static HumanoidDogVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}