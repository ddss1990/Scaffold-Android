# Repository Governance Rollout Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Establish a stable `main` branch baseline, document upgrade branch conventions, and keep the repository centered on one app module unless sample modules become necessary.

**Architecture:** The repository stays single-module by default and uses Git branches to isolate Gradle and AGP upgrades. Governance lives in documentation so the rules are visible to humans and agents without adding build-time complexity. If the library matrix grows later, sample modules can be added without changing the branch strategy.

**Tech Stack:** Markdown docs, Git branch conventions, Gradle wrapper, Android application module, version catalog

---

### Task 1: Recording repository policy in the README

**Files:**
- Modify: `README.md`

- [ ] **Step 1: Confirm the current README already explains the scaffold purpose**

Run: `sed -n '1,220p' README.md`
Expected: README explains that the repository is a validation scaffold, not a product app.

- [ ] **Step 2: Ensure the README includes the integration order and library list**

The README should include the priority table for:
`ViewModel + Coroutines/Flow`, `Compose`, `Koin`, `Retrofit + OkHttp`, `Navigation Compose`, `Room`, `DataStore`, `Coil`, `Paging 3`, and `Timber`.

- [ ] **Step 3: Ensure the README states when to add extra modules**

Use this wording in the README:

```md
Add sample modules only if examples begin to interfere with each other. Use modules for runtime isolation, not for Gradle version experiments.
```

- [ ] **Step 4: Re-run the README review**

Run: `sed -n '1,220p' README.md`
Expected: README clearly separates library validation from repository governance.

### Task 2: Recording agent behavior in CLAUDE.md and AGENT.md

**Files:**
- Modify: `CLAUDE.md`
- Modify: `AGENT.md`

- [ ] **Step 1: Confirm both agent docs use the same policy text**

Run: `diff -u CLAUDE.md AGENT.md`
Expected: no diff after the policy block is aligned.

- [ ] **Step 2: Add branch and module rules to both files**

Use the same block in both files:

```md
## Governance rules

- Treat `main` as the stable baseline.
- Use `upgrade/*` branches for Gradle and AGP changes.
- Keep one `app` module by default.
- Add `samples/*` modules only when a single app no longer keeps examples isolated.
- Do not use Git submodules for version experiments.
```

- [ ] **Step 3: Re-run the comparison**

Run: `diff -u CLAUDE.md AGENT.md`
Expected: no diff.

### Task 3: Capturing the governance spec for future changes

**Files:**
- Modify: `docs/superpowers/specs/2026-05-27-repo-governance-design.md`
- Modify: `docs/superpowers/plans/2026-05-27-repo-governance-rollout.md` if the spec changes

- [ ] **Step 1: Review the governance spec for scope**

Run: `sed -n '1,220p' docs/superpowers/specs/2026-05-27-repo-governance-design.md`
Expected: the spec explains the branch strategy, module strategy, and why submodules are not used.

- [ ] **Step 2: Keep the spec focused on repository policy**

The spec should not introduce implementation tasks for app features or library integrations.

- [ ] **Step 3: Re-run the spec check**

Run: `grep -nE 'TBD|TODO|implement later|fill in details' docs/superpowers/specs/2026-05-27-repo-governance-design.md`
Expected: no matches.

### Task 4: Verifying the baseline remains stable

**Files:**
- No code changes expected

- [ ] **Step 1: Verify the baseline still configures**

Run: `./gradlew help`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 2: Verify the app module still builds**

Run: `./gradlew :app:assembleDebug`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Record the current stable branch policy**

Add a short note in the README or spec describing that `main` is the stable reference and upgrade work happens only on dedicated branches.

### Task 5: Reviewing future module expansion

**Files:**
- Modify: `README.md` if sample modules are introduced later

- [ ] **Step 1: Decide whether the library matrix has outgrown a single app**

Use a sample module only when two examples cannot coexist cleanly in `app`.

- [ ] **Step 2: Add `samples/` modules only when isolation is required**

Keep the default repository shape unchanged until there is a concrete isolation problem.

- [ ] **Step 3: Re-run the governance check**

Run: `sed -n '1,220p' README.md`
Expected: the module strategy still matches the repository policy.
