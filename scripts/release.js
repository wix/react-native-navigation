const cp = require('child_process');
const p = require('path');
const semver = require('semver');

function execSync(cmd) {
  cp.execSync(cmd, { stdio: ['inherit', 'inherit', 'inherit'] });
}

function execSyncRead(cmd) {
  return String(cp.execSync(cmd, { stdio: ['inherit', 'pipe', 'inherit'] })).trim();
}

function execSyncSilently(cmd) {
  cp.execSync(cmd, { stdio: ['ignore', 'ignore', 'ignore'] });
}

function validateEnv() {
  if (!process.env.CI) {
    throw new Error(`releasing is only available from CI`);
  }

  if (!process.env.JENKINS_MASTER) {
    console.log(`not publishing on a different build`);
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
  const latestVersion = execSyncRead(`npm view ${process.env.npm_package_name}@latest version`);
  console.log(`latest version is: ${latestVersion}`);
  console.log(`package version is: ${process.env.npm_package_version}`);
  if (semver.gt(process.env.npm_package_version, latestVersion)) {
    return semver.inc(process.env.npm_package_version, 'patch');
  } else {
    return semver.inc(latestVersion, 'patch');
  }
}

function copyNpmRc() {
  execSync(`rm -f package-lock.json`);
  const npmrcPath = p.resolve(`${__dirname}/.npmrc`);
  execSync(`cp -rf ${npmrcPath} .`);
}

function tagAndPublish(newVersion) {
  console.log(`new version is: ${newVersion}`);
  execSync(`npm version ${newVersion} -m "v${newVersion} [ci skip]"`);
  execSync(`npm publish --tag latest`);
  execSyncSilently(`git push deploy --tags`);
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
