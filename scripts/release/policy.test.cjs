const { test } = require('node:test');
const assert = require('node:assert/strict');
const adapter = require('./rnn.cjs');
const {
  version,
  compareVersions,
  createPlan,
  validateManifest,
  publicationState,
  assertNativeCI,
  assertReleaseContext,
  isReleaseRunPath,
} = require('./policy.cjs');

const context = {
  repository: 'wix/react-native-navigation',
  ref: 'refs/heads/master',
  event: 'workflow_dispatch',
  sha: 'a'.repeat(40),
  runId: '12345',
  runAttempt: '1',
  actor: 'admin',
  triggeringActor: 'admin',
};
const inputs = { operation: 'publish', kind: 'release', version: '8.9.0', npmTag: 'latest' };

test('versions are strict semver, including prerelease ordering', () => {
  for (const invalid of ['v8.9.0', '08.9.0', '8.9', '8.9.0;echo x', '8.9.0-01', '8.9.0+build']) {
    assert.throws(() => version(invalid));
  }
  assert.equal(compareVersions('8.9.0-rc.2', '8.9.0-rc.10'), -1);
  assert.equal(compareVersions('8.9.0-rc.10', '8.9.0'), -1);
  assert.equal(compareVersions('8.9.0', '8.8.99'), 1);
});

test('a release is one exact version and cannot regress latest', () => {
  const plan = createPlan(inputs, context, '8.8.9', '8.8.9');
  assert.equal(plan.packages[0].version, '8.9.0');
  assert.equal(plan.packages[0].npmTag, 'latest');
  assert.throws(() => createPlan({ ...inputs, version: '8.8.9' }, context, '8.8.9', '8.8.9'));
  assert.throws(() => createPlan({ ...inputs, version: '8.9.0-rc.1' }, context, '8.8.9', '8.8.9'));
  assert.throws(() => createPlan({ ...inputs, npmTag: 'other' }, context, '8.8.9', '8.8.9'));
});

test('snapshots have a collision-free identity that survives a rerun', () => {
  const args = { operation: 'publish', kind: 'snapshot', npmTag: 'snapshot', version: '' };
  const first = createPlan(args, context, '8.8.9', '8.9.0');
  const rerun = createPlan(args, { ...context, runAttempt: '2' }, '8.8.9', '8.9.0');
  assert.equal(first.packages[0].version, '8.9.0-snapshot.gha.12345');
  assert.equal(first.packages[0].version, rerun.packages[0].version);
  assert.throws(() =>
    createPlan({ ...args, documentationVersion: '8.9.0' }, context, '8.8.9', '8.9.0')
  );
});

test('publishing context excludes forks, other events, and non-master branches', () => {
  assertReleaseContext(context);
  for (const change of [
    { repository: 'attacker/react-native-navigation' },
    { ref: 'refs/heads/feature' },
    { event: 'pull_request_target' },
    { sha: 'master' },
  ])
    assert.throws(() => assertReleaseContext({ ...context, ...change }));
});

test('release run path accepts only the workflow on master', () => {
  for (const path of [
    adapter.workflow,
    `${adapter.workflow}@master`,
    `${adapter.workflow}@refs/heads/master`,
  ]) {
    assert.equal(isReleaseRunPath(path), true);
  }
  for (const path of [
    `${adapter.workflow}@feature`,
    `${adapter.workflow}@refs/heads/feature`,
    `${adapter.workflow}@master-extra`,
    'elsewhere.yml@master',
  ]) {
    assert.equal(isReleaseRunPath(path), false);
  }
});

test('native CI requires the latest matching status from the verified Buildkite creator', () => {
  const status = {
    context: adapter.nativeStatus,
    state: 'success',
    target_url: `${adapter.nativeStatusUrl}2671`,
    creator: adapter.nativeStatusCreator,
  };
  assert.equal(assertNativeCI([status], context.sha), status);
  assert.throws(() => assertNativeCI([], context.sha));
  assert.throws(() => assertNativeCI([status], 'master'), /exact source SHA/);
  assert.throws(
    () => assertNativeCI({ statuses: [status] }, context.sha),
    /Invalid commit statuses response/
  );
  for (const state of ['pending', 'failure', 'error']) {
    assert.throws(() => assertNativeCI([{ ...status, state }, status], context.sha));
  }
  assert.throws(() =>
    assertNativeCI([{ ...status, creator: { ...status.creator, id: 1 } }, status], context.sha)
  );
  assert.throws(() =>
    assertNativeCI(
      [{ ...status, creator: { ...status.creator, login: 'impostor[bot]' } }, status],
      context.sha
    )
  );
  assert.throws(() =>
    assertNativeCI([{ ...status, target_url: 'https://example.com/builds/1' }], context.sha)
  );
});

test('recovery matches integrity and never chooses a different version', () => {
  const pkg = { name: 'react-native-navigation', version: '8.9.0', integrity: 'sha512-same' };
  assert.equal(publicationState(pkg, null), 'publish');
  assert.equal(
    publicationState(pkg, {
      name: pkg.name,
      version: pkg.version,
      dist: { integrity: pkg.integrity },
    }),
    'complete'
  );
  assert.throws(() =>
    publicationState(pkg, {
      name: pkg.name,
      version: pkg.version,
      dist: { integrity: 'sha512-other' },
    })
  );
  assert.throws(() =>
    publicationState(pkg, { name: pkg.name, version: '8.9.1', dist: { integrity: pkg.integrity } })
  );
});

test('resume rejects dry-run records, foreign packages, and unsafe artifact paths', () => {
  const manifest = {
    ...createPlan(inputs, context, '8.8.9', '8.8.9'),
    releaseSha: 'b'.repeat(40),
    packages: [
      {
        name: 'react-native-navigation',
        version: '8.9.0',
        npmTag: 'latest',
        filename: 'react-native-navigation-8.9.0.tgz',
        integrity: `sha512-${Buffer.alloc(64).toString('base64')}`,
      },
    ],
  };
  validateManifest(manifest);
  assert.throws(() =>
    validateManifest({ ...manifest, operation: 'dry-run' }, { publishing: true })
  );
  assert.throws(() =>
    validateManifest({ ...manifest, packages: [{ ...manifest.packages[0], name: 'other' }] })
  );
  assert.throws(() =>
    validateManifest({
      ...manifest,
      packages: [{ ...manifest.packages[0], filename: '../package.tgz' }],
    })
  );
});
