package com.unibuc.game_manager.model;

import java.util.Arrays;

public enum ProviderStatus {
    PENDING,
    APPROVED,
    REJECTED,
    BANNED;

    public static ProviderStatus fromString(String value) {
        if (value == null || value.isBlank()) return null;
        return Arrays.stream(ProviderStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElse(null);
    }

    public static String toString(ProviderStatus value) {
        if (value == null) return null;
        return value.name().toLowerCase();
    }

}
