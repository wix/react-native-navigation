const { setTimeout: delay } = require('node:timers/promises');
const { check, compareVersions, publicationState } = require('./policy.cjs');

// npm scans new versions before exposing them, so the registry answers 404 for a while after a
// successful publish. Usually minutes, with no documented upper bound.
const VISIBILITY_POLL = { intervalMs: 30_000, attempts: 60, sleep: delay };

async function waitUntilVisible(pkg, lookup, { intervalMs, attempts, sleep } = VISIBILITY_POLL) {
  for (let attempt = 1; ; attempt++) {
    const published = await lookup(pkg.name, pkg.version);
    if (published !== null) return publicationState(pkg, published);
    if (attempt >= attempts) return 'publish';
    await sleep(intervalMs);
  }
}

// Deliberately takes an ordered package list: multi-package adopters can resume
// after the first publish succeeds without incrementing every workspace version.
async function publishPackages(packages, { lookup, publish, report = () => {}, poll }) {
  for (const pkg of packages) {
    if (publicationState(pkg, await lookup(pkg.name, pkg.version)) === 'complete') {
      report(`${pkg.name}@${pkg.version} already matches the artifact; skipping npm publish.`);
      continue;
    }
    const channel = await lookup(pkg.name, pkg.npmTag);
    if (channel) {
      check(
        compareVersions(pkg.version, channel.version) > 0,
        `Refusing to move ${pkg.npmTag} backward from ${channel.version}`
      );
    }
    await publish(pkg);
    check(
      (await waitUntilVisible(pkg, lookup, poll)) === 'complete',
      `Registry has not confirmed ${pkg.name}@${pkg.version}; resume the same release once visible`
    );
    report(`Published ${pkg.name}@${pkg.version} with tag ${pkg.npmTag}.`);
  }
}

module.exports = { publishPackages, waitUntilVisible };
