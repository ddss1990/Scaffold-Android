# Agent Operating Guide

This repository is a minimal Android scaffold used to validate third-party libraries and build configuration changes.

## How to work here

- Inspect the existing Gradle files and app module before changing anything.
- Treat this repo as a test harness, not a product app.
- Make the smallest change that proves or fixes the dependency being tested.
- Prefer `gradle/libs.versions.toml` for versioned dependencies.
- Keep plugin and repository changes limited to `settings.gradle.kts` unless the issue clearly belongs elsewhere.

## Current project shape

- Root build uses Kotlin DSL.
- The app module is configured in `app/build.gradle.kts`.
- Plugin and dependency repositories are declared in `settings.gradle.kts`.
- The project currently has no application code beyond the generated scaffold.

## Governance

- Treat `main` as the stable baseline.
- Use `upgrade/*` branches for Gradle and AGP changes.
- Keep one `app` module by default.
- Add `samples/*` modules only when a single app no longer keeps examples isolated.
- Do not use Git submodules for version experiments.

## Validation workflow

Use the narrowest command that answers the question.

```bash
./gradlew help
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedDebugAndroidTest
```

When adding or changing a third-party library:

1. Confirm whether the failure is a version problem or a repository problem.
2. Update the version catalog first when the dependency is catalog-managed.
3. Add or adjust the implementation code only after the dependency resolves.
4. Re-run the smallest relevant Gradle task.

## Constraints

- Do not rewrite unrelated files.
- Do not use destructive git or shell commands.
- Do not claim success until the relevant Gradle task has completed cleanly.
- Keep documentation and build files in sync when the scaffold changes.

## Output expectations

Summarize:

- what changed
- why it changed
- which validation command was run
- whether anything remains blocked
