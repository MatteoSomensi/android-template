# Android Template

[![CI](https://github.com/MatteoSomensi/android-template/actions/workflows/ci.yml/badge.svg)](https://github.com/MatteoSomensi/android-template/actions/workflows/ci.yml)

An opinionated, production-oriented Android starter for scalable Compose applications. It combines
a granular multi-module architecture, deterministic offline-first reference feature, adaptive
Navigation 3 UI, testing infrastructure, and guarded CI/CD.

<!-- TEMPLATE_BOOTSTRAP_START -->
## Create a project

1. Select **Use this template** and create a new repository, or use GitHub CLI:

   ```bash
   gh repo create owner/my-app \
     --template MatteoSomensi/android-template \
     --private \
     --clone
   cd my-app
   ```

2. Ensure JDK 21 and an Android SDK are available, then run the one-time bootstrap:

   ```bash
   ./gradlew bootstrapTemplate --no-configuration-cache
   ```

The wizard asks for the app name, package, repository, deep-link scheme, and capability preset. It
validates the inputs, renames source packages and delivery metadata, removes unselected modules,
checks for stale placeholders, and removes itself.

3. Review the generated diff and run the first verification build:

   ```bash
   ./gradlew qualityGate
   ./gradlew :app:bundleRelease
   ```

4. Commit the generated project and push it to the repository created in step 1. The first push to
   `main` starts CI; run **Device tests** manually from GitHub Actions before accepting the baseline.

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
<!-- TEMPLATE_BOOTSTRAP_END -->

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
