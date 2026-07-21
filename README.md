# LDAP Transformation Mapper

Keycloak protocol mapper (OIDC + SAML) that reads an LDAP attribute from the
user and applies a configurable transformation (`ORIGINAL`, `UPPERCASE`,
`LOWERCASE`) before adding it to the token or SAML assertion.

Built for **Keycloak 26.6.0+**.

## Requirements

- **Java JDK 21+**
  [Download JDK](https://www.oracle.com/java/technologies/downloads/#jdk26-windows)
- **Apache Maven**
  [Download Maven](https://maven.apache.org/download.cgi#CurrentMaven) → Binary zip archive

## Build

```bash
mvn clean package
```

The compiled JAR is output to `target/ldap-transformation-mapper-1.0.0.jar`.

Restart Keycloak, and the **LDAP ID Transformation Mapper** will be available
under both OIDC token mappers and SAML attribute statement mappers.