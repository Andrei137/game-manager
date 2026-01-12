package com.unibuc.game_manager.utils;

import java.util.function.Consumer;

public class MapperUtils {
    public static <T> void setIfPresent(T value, Consumer<T> setter) {
        if (value != null) setter.accept(value);
    }
}
