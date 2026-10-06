# Migrating another repository to trusted publishing

RNN establishes a release contract: admin dispatch, explicit versions, immutable build
artifacts, OIDC publication, and recovery of the same package bytes. Copy the contract
before extracting shared code. Repository-specific build and package policy lives in
`scripts/release/rnn.cjs`; the current workflow and manifest validator intentionally accept
only RNN. Another repository must implement and test its own adapter and authorization.

## Inventory before changing a publisher

- Read the target repository's instructions and release tests.
- List every publishable package, its source directory, version authority, dist-tags,
  Git tag convention, and dependency relationship to the other packages.
- Trace release-trigger inputs, lifecycle hooks, generated artifacts, documentation,
  release notes, PRs, and any native packaging commands.
- Identify native CI evidence by exact commit. Distinguish the tools needed to produce
  the npm package from tools needed only for testing.
- Inventory CI schedules, webhooks, release credentials, and other consumers of shared
  credentials. A release script deletion alone does not remove an external trigger.

## Implement the repository adapter

- Keep a stable repository-local `release.yml` entry point and protected environment.
- Configure allowed branches, admin authorization, packages, runners, build commands,
  expected tarball contents, version/dependency rewrites, and tag naming.
- Resolve the complete package/version plan before the first publish. Use an ordered
  package list and store each package's integrity in the manifest.
- Build without npm publishing credentials. Native preparation can use separate runner
  jobs; the OIDC publisher must run on a supported GitHub-hosted runner.
- Preserve artifacts and verify their original workflow/source identity during recovery.
- Publish each package only if absent. If already present, require matching integrity.
  Never bump all versions after a partial publish. Multi-package publication is not atomic.
- Register npm trust separately for every package. If introducing reusable workflows,
  check npm's caller-workflow identity rules and grant the required OIDC permissions at
  the caller/callee boundaries. Pin shared implementations to reviewed commit SHAs.

## Known differences from RNN

| Repository              | Migration requirement                                                                                                                                           |
| ----------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| react-native-navigation | One package, Bob JS/types build, native sources, bare version Git tags                                                                                          |
| react-native-ui-lib     | Separate `uilib-native` and `react-native-ui-lib` versions; native package first when selected; rewrite workspace dependencies; preserve package-qualified tags |
| Detox                   | Discover and validate publishable workspaces; preserve synchronized versions; trace Android and iOS native artifact packaging before choosing runners           |

UI-lib's starting points are `scripts/release/release.js`, `releaseUtils.js`, and the
existing release-script tests. Its dependency rewrite must use the planned or verified
published native version, including when only one package needs publication.

Detox's starting points are `scripts/ci.release.js`, `scripts/utils/publishNewVersion.js`,
`scripts/ci.android-release.js`, `scripts/ci.ios-release.sh`, and the Buildkite release
pipelines. Android packaging invokes the SDK and Gradle. Resolve the current iOS packaging
entry point before replacing its artifact download flow. Do not assume RNN's Linux-only
package preparation applies to Detox.

## Clean up and verify

- Delete obsolete publishing jobs, forms, entry points, credential-writing helpers,
  retry-by-version-increment code, dead dependencies, and stale instructions.
- Preserve native tests, useful history, documentation deployment, and shared credentials
  with remaining consumers.
- Test dry-run non-mutation, unauthorized dispatch/rerun, version/channel validation,
  source CI gating, package completeness, and lost-response/partial-publication recovery.
- Verify clean runner builds, then a deliberate prerelease/snapshot using real OIDC.
- Document one-time configuration, remaining infrastructure cleanup, and artifact-loss
  recovery. Expired token-based publishers are not an automatic fallback.

Extract the common publisher, authorization, and artifact-state helpers only after a
second adopter confirms their interface. Build commands, package graphs, documentation,
and version conventions should remain repository-specific.
