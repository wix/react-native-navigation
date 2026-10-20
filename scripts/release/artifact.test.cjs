const { test } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const adapter = require('./rnn.cjs');
const { exec, integrity, validatePackageFiles, verifyReleaseCommit } = require('./artifact.cjs');

const files = [...adapter.requiredFiles, 'ios/Navigation.mm', 'android/src/Navigation.java'];
const pkg = {
  name: adapter.packageName,
  repository: { url: `https://github.com/${adapter.repository}.git` },
  main: './lib/module/index.js',
  types: './lib/typescript/index.d.ts',
  'react-native': './src/index.ts',
  bin: { 'rnn-link': './autolink/postlink/run.js' },
  exports: {
    './Mock': { default: './lib/module/Mock/index.js', types: './lib/typescript/Mock/index.d.ts' },
  },
};

test('package checks include exported paths, native sources, and accidental credentials', () => {
  validatePackageFiles(files, pkg);
  assert.throws(
    () =>
      validatePackageFiles(
        files.filter((f) => !f.startsWith('ios/')),
        pkg
      ),
    /iOS/
  );
  assert.throws(
    () => validatePackageFiles(files, { ...pkg, types: './missing.d.ts' }),
    /Unresolvable/
  );
  for (const bad of [
    'android/.gradle/cache',
    '.npmrc',
    'ios/key.keystore',
    'playground/index.js',
    '../escape',
  ]) {
    assert.throws(() => validatePackageFiles([...files, bad], pkg));
  }
});

test('integrity detects changes to saved package bytes', () => {
  const folder = fs.mkdtempSync(path.join(os.tmpdir(), 'rnn-integrity-'));
  try {
    const file = path.join(folder, 'package.tgz');
    fs.writeFileSync(file, 'original');
    const first = integrity(file);
    fs.writeFileSync(file, 'changed');
    assert.notEqual(integrity(file), first);
  } finally {
    fs.rmSync(folder, { recursive: true, force: true });
  }
});

test('real git bundles permit only the recorded version change on the validated source', () => {
  const previousCwd = process.cwd();
  const folder = fs.mkdtempSync(path.join(os.tmpdir(), 'rnn-release-git-'));
  try {
    process.chdir(folder);
    exec('git', ['init', '-q']);
    exec('git', ['config', 'user.name', 'Release test']);
    exec('git', ['config', 'user.email', 'release@example.test']);
    fs.writeFileSync(
      'package.json',
      JSON.stringify({ name: adapter.packageName, version: '1.0.0' })
    );
    exec('git', ['add', 'package.json']);
    exec('git', ['commit', '-qm', 'source']);
    const sourceSha = exec('git', ['rev-parse', 'HEAD']).trim();
    fs.writeFileSync(
      'package.json',
      JSON.stringify({ name: adapter.packageName, version: '1.0.1' })
    );
    exec('git', ['commit', '-qam', 'version']);
    const releaseSha = exec('git', ['rev-parse', 'HEAD']).trim();
    exec('git', ['bundle', 'create', 'release.bundle', 'HEAD', `^${sourceSha}`]);
    verifyReleaseCommit({ sourceSha, releaseSha, packages: [{ version: '1.0.1' }] }, folder);
    fs.writeFileSync('runtime.js', 'unexpected code');
    exec('git', ['add', 'runtime.js']);
    exec('git', ['commit', '--amend', '-qm', 'unexpected mutation']);
    const changedSha = exec('git', ['rev-parse', 'HEAD']).trim();
    exec('git', ['bundle', 'create', 'release.bundle', 'HEAD', `^${sourceSha}`]);
    assert.throws(
      () =>
        verifyReleaseCommit(
          { sourceSha, releaseSha: changedSha, packages: [{ version: '1.0.1' }] },
          folder
        ),
      /Unexpected release source change/
    );
  } finally {
    process.chdir(previousCwd);
    fs.rmSync(folder, { recursive: true, force: true });
  }
});
