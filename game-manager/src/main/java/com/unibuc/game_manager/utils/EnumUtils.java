package com.unibuc.game_manager.utils;

import java.util.Arrays;

public class EnumUtils {

    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String value) {
        if (value == null || value.isBlank()) return null;
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElse(null);
    }

    public static <E extends Enum<E>> String toString(E value) {
        return value == null ? null : value.name().toLowerCase();
    }

}
