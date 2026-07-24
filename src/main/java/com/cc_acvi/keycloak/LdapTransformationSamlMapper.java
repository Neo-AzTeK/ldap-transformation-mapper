package com.cc_acvi.keycloak;

import org.keycloak.models.AuthenticatedClientSessionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;

import org.keycloak.protocol.saml.mappers.AbstractSAMLProtocolMapper;
import org.keycloak.protocol.saml.mappers.SAMLAttributeStatementMapper;
import org.keycloak.protocol.saml.mappers.AttributeStatementHelper;

import org.keycloak.dom.saml.v2.assertion.AttributeStatementType;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;


public class LdapTransformationSamlMapper
        extends AbstractSAMLProtocolMapper
        implements SAMLAttributeStatementMapper {


    public static final String PROVIDER_ID = "ldap-transformation-saml-mapper";


    @Override
    public String getDisplayCategory() {
        return "SAML Attribute Statement";
    }


    @Override
    public String getDisplayType() {
        return "LDAP ID Transformation Mapper";
    }


    @Override
    public String getHelpText() {
        return "Maps an LDAP attribute with configurable transformation";
    }


    @Override
    public String getId() {
        return PROVIDER_ID;
    }


    @Override
    public List<ProviderConfigProperty> getConfigProperties() {

        List<ProviderConfigProperty> config =
                LdapTransformationMapperConfig.buildBaseConfigProperties();

        /*
         * Standard SAML mapper fields (SAML Attribute Name, Friendly Name,
         * Name Format) are added in one call by AttributeStatementHelper.
         */
        AttributeStatementHelper.setConfigProperties(config);

        return config;
    }


    @Override
    public void transformAttributeStatement(
            AttributeStatementType attributeStatement,
            ProtocolMapperModel mappingModel,
            KeycloakSession session,
            UserSessionModel userSession,
            AuthenticatedClientSessionModel clientSession) {

        String converted = LdapTransformationMapperConfig.resolveTransformedValue(
                mappingModel,
                userSession.getUser()
        );

        if (converted == null) {
            return;
        }

        AttributeStatementHelper.addAttribute(
                attributeStatement,
                mappingModel,
                converted
        );
    }
}