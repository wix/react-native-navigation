# Releasing React Native Navigation

The [Release workflow](https://github.com/wix/react-native-navigation/actions/workflows/release.yml)
is the only supported npm publisher. Repository admins start it manually on `master`.
Both regular releases and snapshots use npm trusted publishing with GitHub OIDC.
Buildkite runs native tests; it no longer versions or publishes packages. Its expired npm
credentials must not be renewed as a fallback.

## One-time configuration

A repository admin and an npm package administrator must complete these steps before
enabling publication. Creating or merging the workflow does not configure npm trust.

1. Create the GitHub environment `npm-release`. Under deployment branches and tags,
   select only the **branch** `master`. Protect `master` and review changes to release
   workflows and helpers. Required environment reviewers are optional; enabling them
   introduces approvals for the record, publish, and finalize jobs.
2. In the npm settings for `react-native-navigation`, add a GitHub Actions trusted publisher:

   | Field             | Value                     |
   | ----------------- | ------------------------- |
   | Organization      | `wix`                     |
   | Repository        | `react-native-navigation` |
   | Workflow filename | `release.yml`             |
   | Environment       | `npm-release`             |
   | Allowed action    | Direct `npm publish`      |

   Explicitly allow direct publication: a stage-only configuration cannot complete this
   workflow. Standalone `npm dist-tag` permission is not needed. See
   [npm trusted publishing](https://docs.npmjs.com/trusted-publishers/).

3. Check Wix's Actions policy permits the pinned checkout, setup-node, upload-artifact,
   and download-artifact actions. Allow workflow-created pull requests. Ensure existing
   repository rules permit version branches and release tags without a bypass.
4. Check the GitHub commit status `buildkite/react-native-navigation` represents all
   Android/iOS lanes in `.buildkite/pipeline.sh`. It must finish successfully on the
   exact `master` commit selected by the release run. The status creator must match
   the Buildkite app ID/login pinned in `scripts/release/rnn.cjs`. A failed, absent, or pending status
   blocks real publication, including snapshots. A dry run reports it without publishing.
5. Verify a dry run and inspect its artifact. Then set the repository Actions variable
   `RELEASE_PUBLISH_ENABLED` to `true`. Without it, publish and resume fail closed.
6. Run an intentional snapshot publication and verify the npm version, integrity,
   provenance, and `snapshot` dist-tag. Confirm that `latest` did not change.
7. Remove obsolete release-only npm secret injection and restrict traditional token
   publishing in npm's package settings. Do not renew expired tokens. A shared credential
   must not be revoked until its other consumers have been identified.

No npm write token is needed in GitHub. `GITHUB_TOKEN` performs repository bookkeeping;
only the publish job has `id-token: write`. Publishing runs on a GitHub-hosted Ubuntu
runner. The workflow uses Node 24.19.0, including npm 11.17.0; update this tested pair
together. Older npm releases can execute `prepare` during `npm pack --ignore-scripts`.

## Start a release

Open **Actions → Release → Run workflow**, select `master`, and supply:

| Input                          | Meaning                                                                        |
| ------------------------------ | ------------------------------------------------------------------------------ |
| `operation`                    | `dry-run` (default), `publish`, or `resume`                                    |
| `kind`                         | `release` or `snapshot`                                                        |
| `version`                      | Exact semver for releases; empty for snapshots                                 |
| `npm_tag`                      | `latest` for stable versions, `next` for prereleases, `snapshot` for snapshots |
| `documentation_version`        | Optional numeric `x.y.z` documentation snapshot                                |
| `remove_documentation_version` | Optional existing version to remove while generating its replacement           |
| `source_run_id`                | Original publishing run ID, for resume only                                    |

Examples:

- Stable: `kind=release`, `version=8.8.10`, `npm_tag=latest`.
- Prerelease: `kind=release`, `version=8.9.0-rc.1`, `npm_tag=next`.
- Snapshot: `kind=snapshot`, leave version empty, `npm_tag=snapshot`.

The selected version must be newer than npm `latest`. Build metadata such as `+build.1`
is intentionally unsupported. Snapshots use `<base>-snapshot.gha.<run_id>`, with the
base derived from the greater of the source package version and npm `latest`. The
run attempt is not part of the version. Snapshots do not create tags, GitHub releases,
version PRs, or documentation changes.

Users with write access may see GitHub's Run workflow button, but both the original
actor and the user rerunning/resuming the workflow must have repository-admin permission.
The workflow rechecks authorization before every job that changes external state.

## What the workflow does

1. Records the exact source SHA, chosen version/channel, and native CI result.
2. Installs the locked Yarn dependencies, changes release metadata, builds JS and
   declarations with Bob, and runs JavaScript and release-helper tests on Linux.
3. Optionally installs the locked website dependencies, generates the requested docs
   version, and builds the site. Removal requires an existing numeric version; replacing
   an existing version requires explicitly selecting that same version for removal.
4. Creates a local release commit containing only the version and optional versioned
   docs, then packs the already built package. Package contents and exported paths are
   checked. Android/iOS sources ship in the package; this job does not compile native apps.
5. Uploads a versioned manifest, the tarball, release notes, and a Git bundle as
   `release-<run_id>-<run_attempt>`. A dry run ends here with no external release mutations.
6. For real releases, persists `ci/update-version-<version>`, then publishes the verified
   tarball using OIDC. The publisher does not install project dependencies or rebuild it.
7. Verifies npm integrity, tags the release commit with the bare version (for example
   `8.8.10`), creates a GitHub release, and opens a version/documentation PR with the
   `release` label. An existing PR template is included with its checkboxes left for review.

Release notes include merged PRs from the preceding reachable stable tag through the
source commit. They preserve feature/enhancement/fix categories, platform groups, and
`skip-changelog` exclusions. No second `gren` invocation or timed retry is involved.

Review and merge the version PR after publication. Documentation deploys through the
existing documentation workflow after that merge. GitHub may require approval for checks
on an Actions-created PR; inspect the PR's checks before merging. The release workflow
does not invoke engine notification.

## Resume a partial release

Use **Re-run failed jobs** when the original run still has its preparation outputs, or
dispatch `operation=resume` with `source_run_id` set to the original **publish** run.
Other release inputs are ignored in resume mode. Do not enter a different version to
recover from a GitHub or network error.

Recovery validates the upstream workflow identity, source SHA, native CI, artifact
integrity, release commit, and package metadata. It downloads the saved artifacts and
does not rebuild against the current branch. If the version is still absent from npm and
`master` has advanced, re-run the original publishing run instead of dispatching a new
resume run: npm provenance must identify the source commit that built the package. A new
resume dispatch may finish GitHub bookkeeping for an already published matching tarball.

| State                                             | Result                                                                     |
| ------------------------------------------------- | -------------------------------------------------------------------------- |
| Version absent from npm                           | Publish the original tarball, provided its channel would not move backward |
| Same version and same integrity already published | Skip publication and finish GitHub steps                                   |
| Same version with different integrity             | Stop; investigate the conflicting publication                              |
| Registry failure or lost response                 | Fail without choosing another version; resume when healthy                 |
| Existing branch/tag/PR matches                    | Reuse it                                                                   |
| Existing branch/tag/PR conflicts                  | Stop; never force-push or move a tag                                       |
| Dry-run artifact selected                         | Reject; start a fresh publish run                                          |
| Artifact expired/deleted                          | Stop automatic recovery                                                    |

Artifacts request 90-day retention, subject to organization limits. Retain artifacts until
the version PR is merged and the release is verified. After artifact loss, an admin must
investigate the existing registry version and persisted release branch manually; do not
reconstruct and publish different bytes under the same version or automatically fall back
to the old pipeline. npm and GitHub do not provide a cross-service atomic transaction.

The concurrency group serializes all release attempts. GitHub may replace a pending run
when another dispatch is queued; it does not cancel the running publisher. Check each run's
state rather than assuming every click is queued indefinitely.

## Validate changes to the release tooling

With Node 24.19.0 and npm 11.17.0:

```sh
node --test scripts/release/*.test.cjs
node .yarn/releases/yarn-4.12.0.cjs install --immutable
node .yarn/releases/yarn-4.12.0.cjs prepare
node .yarn/releases/yarn-4.12.0.cjs test-js
RELEASE_DIR=/tmp/rnn-package node scripts/release/index.cjs pack-check
```

`pack-check` only prepares and inspects a local tarball. The read-only Release checks
workflow runs the tests and clean Linux package check on PRs. A real OIDC publication
still needs the default-branch workflow and the npm/GitHub configuration above.

## Removed release paths and retained consumers

The Buildkite release form, publishing job, legacy `scripts/release.js`, `gren` config,
and root/playground `github-release-notes` dependencies have been removed. Automatic
Buildkite snapshots are replaced by manual snapshots. Native Buildkite testing remains.

Outside the repository, remove obsolete release schedules/hooks and release-only
`NPM_TOKEN`/`NPM_EMAIL` injection. Inspect any old Jenkins configuration referenced by
historical setup; the old README badge alone does not prove an active publisher exists.

Keep the tracked registry-only `.npmrc`, documentation deployment's `GH_PAGES_DEPLOY`,
and any Git credential still used by `scripts/test-snapshot.js` to record reference images.
Do not delete shared secrets merely because the old publisher also used them.

For the next repository, use [the migration checklist](release-migration-checklist.md).
