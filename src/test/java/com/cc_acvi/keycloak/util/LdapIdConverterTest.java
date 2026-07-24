package com.cc_acvi.keycloak.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class LdapIdConverterTest {

    @Test
    @DisplayName("Should return null when input value is null")
    void shouldReturnNullWhenInputIsNull() {
        String result = LdapIdConverter.transform(null, LdapIdConverter.Transformation.UPPERCASE);
        assertThat(result).isNull();
    }

    @ParameterizedTest
    @EnumSource(LdapIdConverter.Transformation.class)
    @DisplayName("Should return null for null input regardless of transformation mode")
    void shouldReturnNullForNullInputAllTransformations(LdapIdConverter.Transformation transformation) {
        String result = LdapIdConverter.transform(null, transformation);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return unchanged value when transformation parameter is null")
    void shouldReturnOriginalWhenTransformationIsNull() {
        String result = LdapIdConverter.transform("SampleValue", null);
        assertThat(result).isEqualTo("SampleValue");
    }

    @Test
    @DisplayName("Should convert input to UPPERCASE using Locale.ROOT")
    void shouldConvertToUppercase() {
        String result = LdapIdConverter.transform("user.name_123", LdapIdConverter.Transformation.UPPERCASE);
        assertThat(result).isEqualTo("USER.NAME_123");
    }

    @Test
    @DisplayName("Should convert input to LOWERCASE using Locale.ROOT")
    void shouldConvertToLowercase() {
        String result = LdapIdConverter.transform("USER.NAME_123", LdapIdConverter.Transformation.LOWERCASE);
        assertThat(result).isEqualTo("user.name_123");
    }

    @Test
    @DisplayName("Should return ORIGINAL input unchanged")
    void shouldReturnOriginalValue() {
        String result = LdapIdConverter.transform("MixedCase_Value-456", LdapIdConverter.Transformation.ORIGINAL);
        assertThat(result).isEqualTo("MixedCase_Value-456");
    }

    @Test
    @DisplayName("Should handle empty string gracefully")
    void shouldHandleEmptyString() {
        String result = LdapIdConverter.transform("", LdapIdConverter.Transformation.UPPERCASE);
        assertThat(result).isEmpty();
    }
}
