const cp = require('child_process');
const p = require('path');
const semver = require('semver');

function execSync(cmd) {
  cp.execSync(cmd, {stdio: ['inherit', 'inherit', 'inherit']});
}

function execSyncRead(cmd) {
  return String(cp.execSync(cmd, {stdio: ['inherit', 'pipe', 'inherit']})).trim();
}

function execSyncSilently(cmd) {
  cp.execSync(cmd, {stdio: ['ignore', 'ignore', 'ignore']});
}

function validateEnv() {
  if (!process.env.CI || !process.env.TRAVIS) {
    throw new Error(`releasing is only available from Travis CI`);
  }

  if (process.env.TRAVIS_BRANCH !== 'master') {
    console.error(`not publishing on branch ${process.env.TRAVIS_BRANCH}`);
    return false;
  }

  if (process.env.TRAVIS_PULL_REQUEST !== 'false') {
    console.error(`not publishing as triggered by pull request ${process.env.TRAVIS_PULL_REQUEST}`);
    return false;
  }

  return true;
}

function setupGit() {
  execSyncSilently(`git config --global push.default simple`);
  execSyncSilently(`git config --global user.email "${process.env.GIT_EMAIL}"`);
  execSyncSilently(`git config --global user.name "${process.env.GIT_USER}"`);
  const remoteUrl = new RegExp(`https?://(\\S+)`).exec(execSyncRead(`git remote -v`))[1];
  execSyncSilently(`git remote add deploy "https://${process.env.GIT_USER}:${process.env.GIT_TOKEN}@${remoteUrl}"`);
  execSync(`git checkout master`);
}

function calcNewVersion() {
  const nextTaggedVersion = execSyncRead(`npm view ${process.env.npm_package_name}@next version`);
  console.log(`next tagged version is: ${nextTaggedVersion}`);
  return semver.inc(nextTaggedVersion, 'prerelease');
}

function copyNpmRc() {
  const npmrcPath = p.resolve(`${__dirname}/.npmrc`);
  execSync(`cp -rf ${npmrcPath} .`);
}

function tagAndPublish(newVersion) {
  console.log(`new version is: ${newVersion}`);
  execSync(`npm version ${newVersion} -m "v${newVersion} [ci skip]"`);
  execSyncSilently(`git push deploy --tags`);
  execSync(`npm publish --tag next`);
}

function run() {
  if (!validateEnv()) {
    return;
  }
  setupGit();
  copyNpmRc();
  tagAndPublish(calcNewVersion());
}

run();
