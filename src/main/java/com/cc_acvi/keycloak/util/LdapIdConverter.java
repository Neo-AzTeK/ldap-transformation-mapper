package com.cc_acvi.keycloak.util;

import java.util.Locale;

public final class LdapIdConverter {

    private LdapIdConverter() {
    }

    public enum Transformation {
        ORIGINAL,
        UPPERCASE,
        LOWERCASE
    }

    public static String transform(String value, Transformation transformation) {
        if (value == null) {
            return null;
        }

        if (transformation == null) {
            return value;
        }

        return switch (transformation) {
            case ORIGINAL -> value;
            case UPPERCASE -> value.toUpperCase(Locale.ROOT);
            case LOWERCASE -> value.toLowerCase(Locale.ROOT);
        };
    }
}