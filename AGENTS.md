# Repository guidance

Architecture, testing, and delivery decisions are documented in:

- [docs/architecture.md](docs/architecture.md)
- [docs/testing.md](docs/testing.md)
- [docs/delivery.md](docs/delivery.md)

Before handing off a change, run the smallest relevant test followed by:

```bash
./gradlew qualityGate
```

Do not update Roborazzi references unless the visual change is intentional and reviewed. The
default app must remain deterministic and must not require Firebase credentials or a live backend.
