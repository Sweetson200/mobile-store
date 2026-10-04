package com.learning.config;

import java.util.Map;

public final class PersistenceProperties {

    private PersistenceProperties() {
    }

    public static Map<String, Object> fromEnvironment() {
        return Map.of(
                "jakarta.persistence.jdbc.url", requiredEnvironmentVariable("JDBC_DATABASE_URL"),
                "jakarta.persistence.jdbc.user", requiredEnvironmentVariable("DB_USER"),
                "jakarta.persistence.jdbc.password", requiredEnvironmentVariable("DB_PASSWORD"));
    }

    private static String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Required environment variable " + name + " is not set.");
        }
        return value;
    }
}
