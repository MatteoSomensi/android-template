# Delivery

## Continuous integration

Pull requests run independent jobs for architecture/static analysis, unit/build checks, screenshot
verification, Gradle Wrapper validation, dependency review, and bootstrap smoke tests. Managed-device
tests run weekly and on demand to keep the normal feedback loop bounded.

## Release setup

The release workflow is inert until the repository variable `RELEASE_ENABLED` is set to `true`.
Configure the protected `release` environment and these secrets first:

- `SIGNING_KEY`: base64-encoded Android keystore;
- `SIGNING_ALIAS`;
- `SIGNING_STORE_PASSWORD`;
- `SIGNING_KEY_PASSWORD`.

A `vX.Y.Z` tag supplies `versionName`; the GitHub run number supplies the monotonic `versionCode`.
The workflow verifies the project, signs the AAB, generates its SBOM and checksum, attests provenance,
and publishes a GitHub Release.

To deploy the same AAB to Play Internal, add `PLAY_SERVICE_ACCOUNT_JSON` and set
`PLAY_PUBLISH_ENABLED=true`. Keep environment approval enabled for production credentials.
