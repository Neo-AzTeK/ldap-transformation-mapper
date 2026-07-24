package com.cc_acvi.keycloak;

import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;

import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapper;
import org.keycloak.protocol.oidc.mappers.OIDCAccessTokenMapper;
import org.keycloak.protocol.oidc.mappers.OIDCIDTokenMapper;
import org.keycloak.protocol.oidc.mappers.UserInfoTokenMapper;
import org.keycloak.protocol.oidc.mappers.OIDCAttributeMapperHelper;

import org.keycloak.representations.IDToken;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;


public class LdapTransformationMapper
        extends AbstractOIDCProtocolMapper
        implements OIDCAccessTokenMapper,
                   OIDCIDTokenMapper,
                   UserInfoTokenMapper {


    public static final String PROVIDER_ID = "ldap-transformation-mapper";


    @Override
    public String getDisplayCategory() {
        return "Token mapper";
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
         * Adds standard Keycloak OIDC mapper options:
         *
         * - Token Claim Name
         * - JSON Type
         * - Include in ID Token
         * - Include in Access Token
         * - Include in UserInfo
         */
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(config);
        OIDCAttributeMapperHelper.addJsonTypeConfig(config);
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(
                config,
                this.getClass()
        );

        return config;
    }


    @Override
    protected void setClaim(
            IDToken token,
            ProtocolMapperModel mappingModel,
            UserSessionModel userSession,
            KeycloakSession session,
            ClientSessionContext clientSessionCtx) {

        String converted = LdapTransformationMapperConfig.resolveTransformedValue(
                mappingModel,
                userSession.getUser()
        );

        if (converted == null) {
            return;
        }

        OIDCAttributeMapperHelper.mapClaim(
                token,
                mappingModel,
                converted
        );
    }
}
