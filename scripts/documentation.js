const childProcess = require('child_process');
const fs = require('fs');
const path = require('path');

const docsPath = path.resolve(__dirname, '../website');

function release(version, removeVersion, options = {}) {
  const targetDocsPath = options.docsPath || docsPath;
  const versionsPath = path.join(targetDocsPath, 'versions.json');

  _validateVersion(version, 'version');
  if (removeVersion !== undefined && removeVersion !== '') {
    _validateVersion(removeVersion, 'removeVersion');
  }

  const versions = _readDocsVersionsJson(versionsPath);
  const shouldRemoveVersion = removeVersion !== undefined && removeVersion !== '';

  if (shouldRemoveVersion && !versions.includes(removeVersion)) {
    throw new Error(`Documentation version does not exist: ${removeVersion}`);
  }
  if (versions.includes(version) && (!shouldRemoveVersion || removeVersion !== version)) {
    throw new Error(`Documentation version already exists: ${version}`);
  }

  if (shouldRemoveVersion) {
    _removeDocsVersion(targetDocsPath, versionsPath, versions, removeVersion);
  }

  console.log(`Building documentation version: ${version}`);
  childProcess.execFileSync('npm', ['run', 'docusaurus', '--', 'docs:version', version], {
    cwd: targetDocsPath,
    stdio: 'inherit',
  });
}

function _validateVersion(version, name) {
  if (typeof version !== 'string' || !/^\d+(?:\.\d+)+$/.test(version)) {
    throw new Error(`${name} must be a dotted numeric version`);
  }
}

function _removeDocsVersion(targetDocsPath, versionsPath, versions, version) {
  console.log(`Removing documentation version: ${version}`);
  fs.rmSync(path.join(targetDocsPath, 'versioned_docs', `version-${version}`), {
    recursive: true,
    force: true,
  });
  fs.rmSync(path.join(targetDocsPath, 'versioned_sidebars', `version-${version}-sidebars.json`), {
    force: true,
  });
  _writeDocsVersionsJson(
    versionsPath,
    versions.filter((existingVersion) => existingVersion !== version)
  );
}

function _readDocsVersionsJson(versionsPath) {
  const versions = JSON.parse(fs.readFileSync(versionsPath, 'utf8'));
  if (!Array.isArray(versions)) {
    throw new Error('Documentation versions must be an array');
  }
  return versions;
}

function _writeDocsVersionsJson(versionsPath, versions) {
  fs.writeFileSync(versionsPath, JSON.stringify(versions, null, 2));
}

module.exports = {
  release,
};
