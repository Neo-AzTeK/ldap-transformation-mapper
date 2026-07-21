package com.cc_acvi.keycloak.util;

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

        return switch (transformation) {

            case ORIGINAL ->
                    value;

            case UPPERCASE ->
                    value.toUpperCase();

            case LOWERCASE ->
                    value.toLowerCase();
        };
    }
}