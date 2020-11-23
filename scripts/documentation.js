const shellUtils = require('shell-utils');
const exec = shellUtils.exec;
const fs = require('fs');

const docsVersionsJsonPath = `${_docsPath}/versions.json`;

export function release(version) {
  if (_versionExists(version)) _removeDocsVersion(version);
  exec.execSync(`npm --prefix ${_docsPath()} run docusaurus docs:version ${version}`);
  exec.execSync(`git add website`);
}

function _removeDocsVersion(version) {
  exec.execSync(`rm -rf ${_docsPath()}/versioned_docs/version-${version}`);
  exec.execSync(`rm -f ${_docsPath()}/versioned_sidebars/version-${version}-sidebars.json`);
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

function _docsPath() {
  return `${process.cwd()}/website`;
}