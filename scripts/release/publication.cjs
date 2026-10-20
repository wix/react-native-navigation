const { check, compareVersions, publicationState } = require('./policy.cjs');

// Deliberately takes an ordered package list: multi-package adopters can resume
// after the first publish succeeds without incrementing every workspace version.
async function publishPackages(packages, { lookup, publish, report = () => {} }) {
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
      publicationState(pkg, await lookup(pkg.name, pkg.version)) === 'complete',
      `Registry has not confirmed ${pkg.name}@${pkg.version}; resume the same release once visible`
    );
    report(`Published ${pkg.name}@${pkg.version} with tag ${pkg.npmTag}.`);
  }
}

module.exports = { publishPackages };
