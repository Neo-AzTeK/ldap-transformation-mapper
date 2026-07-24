package com.cc_acvi.keycloak;

import com.cc_acvi.keycloak.util.LdapIdConverter;

import org.jboss.logging.Logger;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared configuration constants and helper logic for the LDAP ID
 * transformation mappers (OIDC token mapper and SAML attribute mapper).
 *
 * Both mapper classes previously declared their own copies of
 * SOURCE_ATTRIBUTE / TRANSFORMATION and duplicated the "resolve + convert"
 * logic. That duplication is centralized here so there is a single
 * source of truth for the config keys and the conversion behavior.
 */
public final class LdapTransformationMapperConfig {

    private static final Logger logger = Logger.getLogger(LdapTransformationMapperConfig.class);

    private LdapTransformationMapperConfig() {
    }

    public static final String SOURCE_ATTRIBUTE = "ldap.id.source";

    public static final String TRANSFORMATION = "ldap.id.transformation";

    public static final String DEFAULT_SOURCE_ATTRIBUTE = "LDAP_ID";

    /**
     * Builds the two mapper-specific config properties (source attribute +
     * transformation mode). Standard SAML/OIDC fields (attribute name,
     * friendly name, token claim name, etc.) are added separately by each
     * mapper via the platform helpers (SAMLAttributeMapperHelper /
     * OIDCAttributeMapperHelper), since those differ per protocol.
     */
    public static List<ProviderConfigProperty> buildBaseConfigProperties() {

        List<ProviderConfigProperty> config = new ArrayList<>();

        ProviderConfigProperty source = new ProviderConfigProperty();
        source.setName(SOURCE_ATTRIBUTE);
        source.setLabel("LDAP Source Attribute");
        source.setType(ProviderConfigProperty.STRING_TYPE);
        source.setDefaultValue(DEFAULT_SOURCE_ATTRIBUTE);
        source.setHelpText("LDAP user attribute to transform");
        config.add(source);

        ProviderConfigProperty transformation = new ProviderConfigProperty();
        transformation.setName(TRANSFORMATION);
        transformation.setLabel("LDAP ID Transformation");
        transformation.setType(ProviderConfigProperty.LIST_TYPE);
        transformation.setDefaultValue(LdapIdConverter.Transformation.UPPERCASE.name());
        transformation.setOptions(
                List.of(
                        LdapIdConverter.Transformation.ORIGINAL.name(),
                        LdapIdConverter.Transformation.UPPERCASE.name(),
                        LdapIdConverter.Transformation.LOWERCASE.name()
                )
        );
        config.add(transformation);

        return config;
    }

    /**
     * Reads the configured source attribute off the user, applies the
     * configured transformation, and returns the converted value (or null
     * if the user has no value for the source attribute).
     */
    public static String resolveTransformedValue(
            ProtocolMapperModel mappingModel,
            UserModel user) {

        if (user == null || mappingModel == null || mappingModel.getConfig() == null) {
            logger.trace("Null user, mappingModel, or config provided to resolveTransformedValue");
            return null;
        }

        String source = mappingModel.getConfig()
                .getOrDefault(SOURCE_ATTRIBUTE, DEFAULT_SOURCE_ATTRIBUTE);

        String ldapId = user.getFirstAttribute(source);

        if (ldapId == null) {
            logger.tracef("User '%s' has no attribute '%s'", user.getUsername(), source);
            return null;
        }

        String mode = mappingModel.getConfig()
                .getOrDefault(TRANSFORMATION, LdapIdConverter.Transformation.UPPERCASE.name());

        LdapIdConverter.Transformation transformation;
        try {
            transformation = LdapIdConverter.Transformation.valueOf(mode);
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warnf("Invalid transformation mode '%s' configured for mapper '%s'. Falling back to UPPERCASE.",
                    mode, mappingModel.getName());
            transformation = LdapIdConverter.Transformation.UPPERCASE;
        }

        String transformed = LdapIdConverter.transform(ldapId, transformation);
        logger.tracef("Transformed attribute '%s' value for user '%s' using mode %s: '%s' -> '%s'",
                source, user.getUsername(), transformation, ldapId, transformed);

        return transformed;
    }
}
