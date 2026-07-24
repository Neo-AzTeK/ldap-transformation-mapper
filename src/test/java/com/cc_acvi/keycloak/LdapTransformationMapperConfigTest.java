package com.cc_acvi.keycloak;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LdapTransformationMapperConfigTest {

    @Mock
    private ProtocolMapperModel mappingModel;

    @Mock
    private UserModel userModel;

    @Test
    @DisplayName("buildBaseConfigProperties should return source attribute and transformation properties")
    void shouldBuildBaseConfigProperties() {
        List<ProviderConfigProperty> properties = LdapTransformationMapperConfig.buildBaseConfigProperties();

        assertThat(properties).hasSize(2);
        assertThat(properties.get(0).getName()).isEqualTo(LdapTransformationMapperConfig.SOURCE_ATTRIBUTE);
        assertThat(properties.get(0).getDefaultValue()).isEqualTo(LdapTransformationMapperConfig.DEFAULT_SOURCE_ATTRIBUTE);

        assertThat(properties.get(1).getName()).isEqualTo(LdapTransformationMapperConfig.TRANSFORMATION);
        assertThat(properties.get(1).getOptions()).containsExactly("ORIGINAL", "UPPERCASE", "LOWERCASE");
    }

    @Test
    @DisplayName("Should return null if user, mappingModel or config is null")
    void shouldReturnNullOnNullArguments() {
        assertThat(LdapTransformationMapperConfig.resolveTransformedValue(null, userModel)).isNull();
        assertThat(LdapTransformationMapperConfig.resolveTransformedValue(mappingModel, null)).isNull();
    }

    @Test
    @DisplayName("Should resolve and convert LDAP attribute to UPPERCASE")
    void shouldResolveAndConvertUppercase() {
        Map<String, String> config = new HashMap<>();
        config.put(LdapTransformationMapperConfig.SOURCE_ATTRIBUTE, "employeeId");
        config.put(LdapTransformationMapperConfig.TRANSFORMATION, "UPPERCASE");

        when(mappingModel.getConfig()).thenReturn(config);
        when(userModel.getFirstAttribute("employeeId")).thenReturn("emp-abc-123");

        String result = LdapTransformationMapperConfig.resolveTransformedValue(mappingModel, userModel);

        assertThat(result).isEqualTo("EMP-ABC-123");
    }

    @Test
    @DisplayName("Should resolve and convert LDAP attribute to LOWERCASE")
    void shouldResolveAndConvertLowercase() {
        Map<String, String> config = new HashMap<>();
        config.put(LdapTransformationMapperConfig.SOURCE_ATTRIBUTE, "employeeId");
        config.put(LdapTransformationMapperConfig.TRANSFORMATION, "LOWERCASE");

        when(mappingModel.getConfig()).thenReturn(config);
        when(userModel.getFirstAttribute("employeeId")).thenReturn("EMP-ABC-123");

        String result = LdapTransformationMapperConfig.resolveTransformedValue(mappingModel, userModel);

        assertThat(result).isEqualTo("emp-abc-123");
    }

    @Test
    @DisplayName("Should return null if user has no attribute value for source")
    void shouldReturnNullIfUserAttributeMissing() {
        Map<String, String> config = new HashMap<>();
        config.put(LdapTransformationMapperConfig.SOURCE_ATTRIBUTE, "employeeId");

        when(mappingModel.getConfig()).thenReturn(config);
        when(userModel.getFirstAttribute("employeeId")).thenReturn(null);

        String result = LdapTransformationMapperConfig.resolveTransformedValue(mappingModel, userModel);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should fallback to UPPERCASE when invalid transformation mode is configured")
    void shouldFallbackToUppercaseOnInvalidMode() {
        Map<String, String> config = new HashMap<>();
        config.put(LdapTransformationMapperConfig.SOURCE_ATTRIBUTE, "employeeId");
        config.put(LdapTransformationMapperConfig.TRANSFORMATION, "INVALID_MODE");

        when(mappingModel.getConfig()).thenReturn(config);
        when(mappingModel.getName()).thenReturn("Test Mapper");
        when(userModel.getFirstAttribute("employeeId")).thenReturn("my-id");

        String result = LdapTransformationMapperConfig.resolveTransformedValue(mappingModel, userModel);

        assertThat(result).isEqualTo("MY-ID");
    }
}
