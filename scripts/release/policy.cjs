const adapter = require('./rnn.cjs');

function check(condition, message) {
  if (!condition) throw new Error(message);
}

function version(value) {
  check(typeof value === 'string' && value.length <= 128, 'Version must be a short semver string');
  // Build metadata is intentionally unsupported: npm cannot distinguish versions
  // solely by build metadata, which would make release identity ambiguous.
  const match =
    /^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-([\da-zA-Z-]+(?:\.[\da-zA-Z-]+)*))?$/.exec(value);
  check(match, `Invalid version: ${value}`);
  const core = match.slice(1, 4).map(Number);
  check(core.every(Number.isSafeInteger), 'Version components exceed the safe integer range');
  const pre = match[4] ? match[4].split('.') : [];
  check(
    pre.every((part) => !/^0\d+$/.test(part)),
    'Numeric prerelease identifiers cannot have leading zeros'
  );
  return { core, pre };
}

function compareVersions(a, b) {
  const left = version(a);
  const right = version(b);
  const compare = (x, y) => (x === y ? 0 : x < y ? -1 : 1);
  for (let i = 0; i < 3; i++) {
    const result = compare(left.core[i], right.core[i]);
    if (result) return result;
  }
  if (!left.pre.length || !right.pre.length) return compare(!left.pre.length, !right.pre.length);
  for (let i = 0; i < Math.max(left.pre.length, right.pre.length); i++) {
    if (left.pre[i] === undefined) return -1;
    if (right.pre[i] === undefined) return 1;
    const x = left.pre[i];
    const y = right.pre[i];
    if (x === y) continue;
    const nx = /^\d+$/.test(x);
    const ny = /^\d+$/.test(y);
    return nx && ny ? compare(BigInt(x), BigInt(y)) : nx !== ny ? (nx ? -1 : 1) : compare(x, y);
  }
  return 0;
}

function assertReleaseContext(context) {
  check(context.repository === adapter.repository, 'Release must run in the upstream repository');
  check(context.ref === `refs/heads/${adapter.branch}`, 'Release must run from master');
  check(context.event === 'workflow_dispatch', 'Release requires workflow_dispatch');
  check(/^[a-f0-9]{40}$/.test(context.sha), 'Expected an immutable source SHA');
  check(
    /^[1-9]\d*$/.test(context.runId) && /^[1-9]\d*$/.test(context.runAttempt),
    'Invalid workflow run identity'
  );
  check(context.actor && context.triggeringActor, 'Missing workflow actor');
}

function isReleaseRunPath(path) {
  return [
    adapter.workflow,
    `${adapter.workflow}@${adapter.branch}`,
    `${adapter.workflow}@refs/heads/${adapter.branch}`,
  ].includes(path);
}

function assertNativeCI(statuses, sha) {
  check(/^[a-f0-9]{40}$/.test(sha), 'Native CI requires an exact source SHA');
  check(Array.isArray(statuses), 'Invalid commit statuses response');
  // GitHub lists statuses newest first. The first matching context governs even
  // when an older status was successful.
  const status = statuses.find((item) => item.context === adapter.nativeStatus);
  check(
    status &&
      status.state === 'success' &&
      status.target_url?.startsWith(adapter.nativeStatusUrl) &&
      status.creator?.id === adapter.nativeStatusCreator.id &&
      status.creator?.login === adapter.nativeStatusCreator.login,
    `Required native CI ${adapter.nativeStatus} has not succeeded for ${sha}`
  );
  return status;
}

function createPlan(inputs, context, packageVersion, latest) {
  assertReleaseContext(context);
  check(['dry-run', 'publish'].includes(inputs.operation), 'Invalid preparation operation');
  check(['release', 'snapshot'].includes(inputs.kind), 'Invalid release kind');
  version(packageVersion);
  version(latest);
  let selected = inputs.version || '';
  if (inputs.kind === 'snapshot') {
    check(
      inputs.npmTag === 'snapshot' && !selected,
      'Snapshots require tag snapshot and an empty version input'
    );
    check(
      !inputs.documentationVersion && !inputs.removeDocumentationVersion,
      'Snapshots cannot change documentation'
    );
    const base = compareVersions(packageVersion, latest) > 0 ? packageVersion : latest;
    selected = `${version(base).core.join('.')}-snapshot.gha.${context.runId}`;
  } else {
    const parsed = version(selected);
    check(['latest', 'next'].includes(inputs.npmTag), 'Releases require latest or next');
    check(
      inputs.npmTag === 'next' ? parsed.pre.length > 0 : parsed.pre.length === 0,
      'Use latest for stable versions and next for prereleases'
    );
    check(compareVersions(selected, latest) > 0, 'A new release must be newer than npm latest');
  }
  const documentationVersion = inputs.documentationVersion || '';
  const removeDocumentationVersion = inputs.removeDocumentationVersion || '';
  for (const value of [documentationVersion, removeDocumentationVersion]) {
    check(
      !value || /^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)$/.test(value),
      'Documentation versions must be numeric x.y.z'
    );
  }
  check(
    !removeDocumentationVersion || documentationVersion,
    'Removing documentation requires a replacement version'
  );
  return {
    schema: 1,
    repository: adapter.repository,
    workflow: adapter.workflow,
    operation: inputs.operation,
    kind: inputs.kind,
    sourceSha: context.sha,
    runId: context.runId,
    runAttempt: context.runAttempt,
    actor: context.actor,
    documentationVersion,
    removeDocumentationVersion,
    packages: [{ name: adapter.packageName, version: selected, npmTag: inputs.npmTag }],
  };
}

function validateManifest(manifest, { publishing = false } = {}) {
  check(
    manifest.schema === 1 &&
      manifest.repository === adapter.repository &&
      manifest.workflow === adapter.workflow,
    'Artifact is not an RNN release manifest'
  );
  check(['dry-run', 'publish'].includes(manifest.operation), 'Invalid artifact operation');
  if (publishing)
    check(
      manifest.operation === 'publish',
      'A dry-run artifact cannot be published; start a publish run'
    );
  check(['release', 'snapshot'].includes(manifest.kind), 'Invalid artifact release kind');
  check(
    /^[a-f0-9]{40}$/.test(manifest.sourceSha) && /^[a-f0-9]{40}$/.test(manifest.releaseSha),
    'Invalid artifact commit'
  );
  check(
    /^[1-9]\d*$/.test(manifest.runId) && /^[1-9]\d*$/.test(manifest.runAttempt),
    'Invalid artifact run'
  );
  check(
    Array.isArray(manifest.packages) && manifest.packages.length === 1,
    'RNN releases exactly one package'
  );
  for (const pkg of manifest.packages) {
    check(pkg.name === adapter.packageName, 'Unexpected package in release artifact');
    const parsed = version(pkg.version);
    check(
      pkg.filename === `${pkg.name}-${pkg.version}.tgz`,
      'Unexpected package artifact filename'
    );
    check(/^sha512-[A-Za-z0-9+/]{86}==$/.test(pkg.integrity), 'Invalid package integrity');
    check(
      manifest.kind === 'snapshot'
        ? pkg.npmTag === 'snapshot' && pkg.version.endsWith(`-snapshot.gha.${manifest.runId}`)
        : (pkg.npmTag === 'latest' && !parsed.pre.length) ||
            (pkg.npmTag === 'next' && parsed.pre.length > 0),
      'Artifact version/channel mismatch'
    );
  }
  return manifest;
}

function publicationState(pkg, published) {
  if (published === null) return 'publish';
  check(
    published.name === pkg.name &&
      published.version === pkg.version &&
      published.dist?.integrity === pkg.integrity,
    `Conflicting npm package ${pkg.name}@${pkg.version}; refusing to republish or increment`
  );
  return 'complete';
}

module.exports = {
  check,
  version,
  compareVersions,
  assertReleaseContext,
  isReleaseRunPath,
  assertNativeCI,
  createPlan,
  validateManifest,
  publicationState,
};
