# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.1] - 2026-07-25

### Added
- Comprehensive JUnit 5 and Mockito unit tests for `LdapIdConverter` and `LdapTransformationMapperConfig`.
- GitHub Actions CI/CD workflows for automated build, test, and release creation.
- Dependabot configuration for Maven and GitHub Actions dependencies.
- JBoss Logger trace/debug and warning logs for attribute resolution and fallback handling.
- JaCoCo test coverage report plugin configuration.
- EditorConfig file for standard code formatting.

### Changed
- Standardized package architecture: aligned `LdapTransformationMapperConfig` package declaration with file directory path (`com.cc_acvi.keycloak`).
- Enforced `Locale.ROOT` in `LdapIdConverter` for locale-invariant `UPPERCASE` and `LOWERCASE` transformations.
- Improved error resilience in `LdapTransformationMapperConfig` to gracefully fallback to `UPPERCASE` when an invalid transformation mode string is configured.
