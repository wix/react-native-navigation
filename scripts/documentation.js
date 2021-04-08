const shellUtils = require('shell-utils');
const exec = shellUtils.exec;
const fs = require('fs');
const includes = require('lodash/includes');

const docsPath = `${process.cwd()}/website`;
const docsVersionsJsonPath = `${docsPath}/versions.json`;

function release(version, removeVersion) {
  console.log(`Building documentation version: ${version}`);
  if (_versionExists(removeVersion)) _removeDocsVersion(removeVersion);
  exec.execSync(`npm --prefix ${docsPath} install`);
  exec.execSync(`npm --prefix ${docsPath} run docusaurus docs:version ${version}`);
  exec.execSync(`git add website`);
}

function _removeDocsVersion(version) {
  console.log(`Removing documentation version: ${version}`);
  exec.execSync(`rm -rf ${docsPath}/versioned_docs/version-${version}`);
  exec.execSync(`rm -f ${docsPath}/versioned_sidebars/version-${version}-sidebars.json`);
  const docsVersionsJson = _readDocsVersionsJson();
  docsVersionsJson.splice(docsVersionsJson.indexOf(version), 1);
  _writeDocsVersionsJson(docsVersionsJson);
}

function _versionExists(version) {
  console.log(`check if version exists: ${version}`);
  return version !== '' && includes(_readDocsVersionsJson(), version);
}

function _readDocsVersionsJson() {
  return JSON.parse(fs.readFileSync(docsVersionsJsonPath));
}

function _writeDocsVersionsJson(versionsJson) {
  fs.writeFileSync(docsVersionsJsonPath, JSON.stringify(versionsJson, null, 2));
}

module.exports = {
  release
}