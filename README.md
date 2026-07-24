# LDAP Transformation Mapper

Keycloak protocol mapper (OIDC + SAML) that reads an LDAP attribute from the user and applies a configurable transformation (`ORIGINAL`, `UPPERCASE`, `LOWERCASE`) before adding it to the token or SAML assertion.

Built for **Keycloak 26.6.0+**.

## Compatibility

| Plugin Version | Keycloak Version | Java Version |
| :--- | :--- | :--- |
| `1.0.x` | `26.0.0` – `26.6.0+` | `21+` |

## Requirements

- **Java JDK 21+**
- **Apache Maven 3.8+**

## Build & Test

### Build Package
```bash
mvn clean package
```
The compiled JAR is output to `target/ldap-transformation-mapper-1.0.0.jar`.

### Run Unit Tests & Coverage Report
```bash
mvn clean verify
```
Test reports and JaCoCo code coverage results are generated at `target/site/jacoco/index.html`.

## Deployment

1. Copy `target/ldap-transformation-mapper-1.0.0.jar` into your Keycloak installation `providers/` directory (or volume mount for containerized Keycloak).
2. Restart Keycloak.
3. In Keycloak Admin Console:
   - For **OIDC**: Go to **Client Scope** or **Client** → **Mappers** → **Add mapper** → **By Configuration** → Select **LDAP ID Transformation Mapper**.
   - For **SAML**: Go to **Client Scope** or **Client** → **Mappers** → **Add mapper** → **By Configuration** → Select **LDAP ID Transformation Mapper**.

## CI/CD & Quality Checks

This repository includes:
- **GitHub Actions (`.github/workflows/ci.yml`)**: Automated JDK 21 build, testing, and JaCoCo coverage report artifacts.
- **Release Automation (`.github/workflows/release.yml`)**: Automatically packages and publishes `.jar` artifacts to GitHub Releases on tag pushes (`v*`).
- **Dependabot (`.github/dependabot.yml`)**: Weekly updates for Maven dependencies and GitHub Actions.