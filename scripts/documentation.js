const shellUtils = require('shell-utils');
const exec = shellUtils.exec;
const fs = require('fs');

const docsPath = `${process.cwd()}/website`;
const docsVersionsJsonPath = `${docsPath}/versions.json`;

function release(version) {
  if (_versionExists(version)) _removeDocsVersion(version);
  exec.execSync(`npm --prefix ${docsPath} install`);
  exec.execSync(`npm --prefix ${docsPath} run docusaurus docs:version ${version}`);
  exec.execSync(`git add website`);
}

function _removeDocsVersion(version) {
  exec.execSync(`rm -rf ${docsPath}/versioned_docs/version-${version}`);
  exec.execSync(`rm -f ${docsPath}/versioned_sidebars/version-${version}-sidebars.json`);
  const docsVersionsJson = _readDocsVersionsJson();
  docsVersionsJson.splice(docsVersionsJson.indexOf(version), 1);
  _writeDocsVersionsJson(docsVersionsJson);
}

function _versionExists(version) {
  return _readDocsVersionsJson().indexOf(version) > 0;
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