# Testing strategy

- JVM unit tests cover ViewModels, repositories, mappers, paging orchestration, and bootstrap logic.
- Retrofit contracts use MockWebServer; product runtime never depends on a public demo endpoint.
- Room DAO and migration tests run against Android SQLite.
- Compose behavior tests run with Robolectric and prefer semantics over test tags.
- Roborazzi verifies the 3×3 canonical size matrix plus dark theme and 150% font scale.
- A small managed-device journey verifies app composition and navigation.
- Macrobenchmark and Baseline Profile generation remain separate from correctness checks.

```bash
./gradlew testDebugUnitTest
./gradlew :feature:catalog:verifyRoborazziDebug
./gradlew :core:database:pixel2Api35DebugAndroidTest :app:pixel2Api35DebugAndroidTest
./gradlew :macrobenchmark:connectedBenchmarkAndroidTest
./gradlew qualityGate
```

Screenshot references live in `feature/catalog/src/test/screenshots`. Record them only for an
intentional change, inspect the generated images, then commit the reviewed references.
