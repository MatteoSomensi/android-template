# Android Template

[![CI](https://github.com/MatteoSomensi/android-template/actions/workflows/ci.yml/badge.svg)](https://github.com/MatteoSomensi/android-template/actions/workflows/ci.yml)

An opinionated, production-oriented Android starter for scalable Compose applications. It combines
a granular multi-module architecture, deterministic offline-first reference feature, adaptive
Navigation 3 UI, testing infrastructure, and guarded CI/CD.

## Create a project

Use this repository as a GitHub Template, clone the generated repository, then run:

```bash
./gradlew bootstrapTemplate --no-configuration-cache
```

The wizard asks for the app name, package, repository, deep-link scheme, and capability preset. It
validates the inputs, renames source packages and delivery metadata, removes unselected modules,
checks for stale placeholders, and removes itself. Review the resulting diff and run:

```bash
./gradlew qualityGate
```

For automation, pass properties explicitly:

```bash
./gradlew bootstrapTemplate --no-configuration-cache \
  -Ptemplate.appName="My App" \
  -Ptemplate.packageName=com.example.myapp \
  -Ptemplate.repository=owner/my-app \
  -Ptemplate.deepLinkScheme=myapp \
  -Ptemplate.preset=standard
```

Presets are `minimal`, `standard`, and `full`; `template.features` accepts an explicit comma-separated
set from `firebase,auth,sync,widget,appfunctions,benchmark,roborazzi`. Authentication requires Firebase.
Use `-Ptemplate.dryRun=true` to validate and preview a configuration without writing files.

## Architecture

The included Catalog feature is intentionally small but complete: fixture network source, Room cache,
Paging `RemoteMediator`, domain contract, Hilt binding, ViewModel, adaptive Compose list/detail UI,
deep link, screenshots, and an instrumented journey. Replace the domain while preserving the module
boundaries and testing seams.

See [architecture](docs/architecture.md), [testing](docs/testing.md), and [delivery](docs/delivery.md).

## Main commands

```bash
./gradlew testDebugUnitTest
./gradlew :feature:catalog:verifyRoborazziDebug
./gradlew verifyModuleBoundaries ktlintCheck detekt lintDebug
./gradlew qualityGate
```

Firebase, Credential Manager auth, WorkManager sync, Glance, AppFunctions, Macrobenchmark, Baseline
Profiles, SBOM generation, Renovate, dependency review, artifact attestations, GitHub Releases, and
Play Internal delivery are included without requiring credentials for normal development.

## License

Apache License 2.0. See [LICENSE](LICENSE).
