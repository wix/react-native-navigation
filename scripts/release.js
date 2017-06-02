/*eslint-disable no-console*/
const exec = require('shell-utils').exec;
const p = require('path');
const semver = require('semver');
const fs = require('fs');

const ONLY_ON_BRANCH = 'v2';
const VERSION_TAG = 'alpha';
const VERSION_INC = 'patch';

function validateEnv() {
  if (!process.env.CI || !process.env.TRAVIS) {
    throw new Error(`releasing is only available from Travis CI`);
  }

  if (process.env.TRAVIS_PULL_REQUEST !== 'false') {
    console.log(`not publishing as triggered by pull request ${process.env.TRAVIS_PULL_REQUEST}`);
    return false;
  }

  if (process.env.TRAVIS_BRANCH !== ONLY_ON_BRANCH) {
    console.log(`not publishing on branch ${process.env.TRAVIS_BRANCH}`);
    return false;
  }

  return true;
}

function setupGit() {
  exec.execSyncSilent(`git config --global push.default simple`);
  exec.execSyncSilent(`git config --global user.email "${process.env.GIT_EMAIL}"`);
  exec.execSyncSilent(`git config --global user.name "${process.env.GIT_USER}"`);
  const remoteUrl = new RegExp(`https?://(\\S+)`).exec(exec.execSyncRead(`git remote -v`))[1];
  exec.execSyncSilent(`git remote add deploy "https://${process.env.GIT_USER}:${process.env.GIT_TOKEN}@${remoteUrl}"`);
  exec.execSync(`git checkout ${ONLY_ON_BRANCH}`);
}

function calcNewVersion() {
  const currentVersion = exec.execSyncRead(`npm view ${process.env.npm_package_name} dist-tags.${VERSION_TAG}`);
  console.log(`${VERSION_TAG} version: ${currentVersion}`);
  const packageVersion = process.env.npm_package_version;
  console.log(`package version: ${packageVersion}`);
  const greater = semver.gt(currentVersion, packageVersion) ? currentVersion : packageVersion;
  return semver.inc(greater, VERSION_INC);
}

function createNpmRc() {
  const content = `
email=\${NPM_EMAIL}
//registry.npmjs.org/:_authToken=\${NPM_TOKEN}
`;
  fs.writeFileSync(`.npmrc`, content);
}

function tagAndPublish(newVersion) {
  console.log(`new version is: ${newVersion}`);
  exec.execSync(`npm version ${newVersion} -f -m "v${newVersion} [ci skip]"`);
  exec.execSync(`npm publish --tag ${VERSION_TAG}`);
  exec.execSyncSilent(`git push deploy --tags || true`);
}

function run() {
  if (!validateEnv()) {
    return;
  }
  setupGit();
  createNpmRc();
  tagAndPublish(calcNewVersion());
}

run();
