# Repository Governance Design

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Keep one stable Android scaffold on `main`, use dedicated upgrade branches to validate new Gradle and library versions, and avoid unnecessary module fragmentation.

**Architecture:** The repository stays intentionally small with one application module as the default validation surface. Version upgrades are isolated by Git branch instead of by submodule, because Gradle wrapper and plugin resolution are repository-level concerns. Additional sample modules are introduced only when the library matrix becomes large enough that a single app module no longer keeps examples isolated.

**Tech Stack:** Android Gradle Plugin, Gradle Wrapper, Kotlin DSL, version catalog, Android application module

---

## Problem Statement

This repository is used as a scaffold for testing third-party Android libraries and build tooling changes. The project now needs a clear operating model for:

- keeping one stable baseline on `main`
- validating Gradle and AGP upgrades without disrupting the baseline
- deciding whether new examples belong in the existing app module or in additional modules
- documenting a consistent workflow for future contributors and agent runs

## Design Principles

- Keep the stable path simple and reproducible.
- Treat Gradle version changes as repository-wide experiments.
- Prefer branch isolation over nested repositories.
- Add modules only when they provide real isolation benefits for examples.
- Avoid Git submodule complexity for build-tool experimentation.

## Recommended Structure

### 1. Main branch as the stable baseline

`main` always tracks the most stable, known-good configuration for this scaffold. It should contain:

- one `app` module
- the current pinned Gradle and AGP versions
- the current preferred dependency catalog
- the repository defaults used for validating common third-party libraries

This branch is the reference point for all future upgrades.

### 2. Upgrade branches for Gradle and AGP changes

Use dedicated branches for build-tool experiments, for example:

- `upgrade/gradle-9.6`
- `upgrade/gradle-9.7`
- `upgrade/agp-9.2`

Each branch may change:

- `gradle/wrapper/gradle-wrapper.properties`
- `gradle/libs.versions.toml`
- `settings.gradle.kts`
- related build files required by the upgrade

Upgrade branches are disposable until the change is proven and merged back into `main`.

### 3. One app module by default

The repository should keep a single `app` module as the primary validation surface. That module is enough for:

- ViewModel and lifecycle validation
- Compose UI validation
- Koin integration
- Retrofit and networking checks
- local persistence checks
- dependency and plugin resolution checks

This keeps the repository easy to understand and fast to sync.

### 4. Add sample modules only when needed

Introduce extra modules only if examples begin to interfere with each other. A good trigger is:

- a library needs a dedicated runtime example
- two examples cannot coexist in one app without confusing code paths
- build times or dependency graphs become harder to reason about

When that happens, add a `samples/` or `examples/` style module group, not a separate Git repository.

## Why Not Git Submodule

Git submodules are a poor fit here because the problem is not shared source code ownership. The problem is build-tool and dependency validation. Submodules make branch management, syncing, and local development more difficult without solving the actual Gradle compatibility issue.

## Validation Workflow

Use the narrowest task that proves the current change:

1. `./gradlew help` to validate configuration and plugin resolution.
2. `./gradlew :app:assembleDebug` to validate app build wiring.
3. `./gradlew :app:testDebugUnitTest` to validate JVM-side behavior.
4. `./gradlew :app:connectedDebugAndroidTest` only when device or emulator behavior matters.

Before merging an upgrade branch back to `main`, the branch must restore a clean baseline and pass the smallest relevant Gradle tasks.

## Module Strategy Decision Rule

Use this rule when deciding whether to add a new module:

- **Keep it in `app`** if the example is a single feature or a small library proof.
- **Add a sample module** if the example needs its own entry point or would create clutter inside `app`.
- **Do not use a submodule** for Gradle version experiments or library validation.

## Repository Policies

- `main` remains the most stable version line.
- Build-tool upgrades happen in branches, not in hidden local state.
- Dependency changes should stay visible in `gradle/libs.versions.toml`.
- Repository-level build changes should stay in `settings.gradle.kts` and root Gradle files.
- Documentation should explain why a version or module was added.

## Open Expansion Path

If the library matrix grows, the repository can evolve in this order:

1. keep the single `app` module
2. add a small `samples/` module group
3. keep one branch per Gradle or AGP upgrade line
4. preserve `main` as the stable reference

This lets the repository grow without losing its role as a lightweight validation scaffold.
