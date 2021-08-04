/* tslint:disable: no-console */
const exec = require('shell-utils').exec;
const semver = require('semver');
const fs = require('fs');
const includes = require('lodash/includes');
const documentation = require('./documentation');

const packageJsonPath = `${process.cwd()}/package.json`;

// Workaround JS
const isRelease = process.env.RELEASE_BUILD === 'true';

const BUILD_DOCUMENTATION_VERSION = process.env.BUILD_DOCUMENTATION_VERSION;
const REMOVE_DOCUMENTATION_VERSION = process.env.REMOVE_DOCUMENTATION_VERSION;

const BRANCH = process.env.BRANCH;
let VERSION_TAG = process.env.NPM_TAG;
if (!VERSION_TAG) {
  VERSION_TAG = isRelease ? 'latest' : 'snapshot';
}
const VERSION_INC = 'patch';

function run() {
  if (!validateEnv()) {
    return;
  }
  setupGit();
  createNpmRc();
  versionTagAndPublish();
}

function validateEnv() {
  if (!process.env.JENKINS_CI) {
    throw new Error(`releasing is only available from CI`);
  }

  if (!process.env.JENKINS_MASTER) {
    console.log(`not publishing on a different build`);
    return false;
  }

  return true;
}

function setupGit() {
  exec.execSyncSilent(`git config --global push.default simple`);
  exec.execSyncSilent(`git config --global user.email "${process.env.GIT_EMAIL}"`);
  exec.execSyncSilent(`git config --global user.name "${process.env.GIT_USER}"`);
  const remoteUrl = new RegExp(`https?://(\\S+)`).exec(exec.execSyncRead(`git remote -v`))[1];
  exec.execSyncSilent(
    `git remote add deploy "https://${process.env.GIT_USER}:${process.env.GIT_TOKEN}@${remoteUrl}"`
  );
  // exec.execSync(`git checkout ${ONLY_ON_BRANCH}`);
}

function createNpmRc() {
  exec.execSync(`rm -f package-lock.json`);
  const content = `
email=\${NPM_EMAIL}
//registry.npmjs.org/:_authToken=\${NPM_TOKEN}
`;
  fs.writeFileSync(`.npmrc`, content);
}

function versionTagAndPublish() {
  const packageVersion = semver.clean(process.env.npm_package_version);
  console.log(`package version: ${packageVersion}`);

  const currentPublished = findCurrentPublishedVersion();
  console.log(`current published version: ${currentPublished}`);

  const version = isRelease
    ? process.env.VERSION
    : semver.gt(packageVersion, currentPublished)
      ? `${packageVersion}-snapshot.${process.env.BUILD_ID}`
      : `${currentPublished}-snapshot.${process.env.BUILD_ID}`;

  console.log(`Publishing version: ${version}`);

  tryPublishAndTag(version);
}

function findCurrentPublishedVersion() {
  return exec.execSyncRead(`npm view ${process.env.npm_package_name} dist-tags.latest`);
}

function tryPublishAndTag(version) {
  let theCandidate = version;
  for (let retry = 0; retry < 5; retry++) {
    try {
      tagAndPublish(theCandidate);
      console.log(`Released ${theCandidate}`);
      return;
    } catch (err) {
      const alreadyPublished = includes(
        err.toString(),
        'You cannot publish over the previously published version'
      );
      if (!alreadyPublished) {
        throw err;
      }
      console.log(`previously published. retrying with increased ${VERSION_INC}...`);
      theCandidate = semver.inc(theCandidate, VERSION_INC);
    }
  }
}

function tagAndPublish(newVersion) {
  console.log(`trying to publish ${newVersion}...`);
  exec.execSync(`npm --no-git-tag-version version ${newVersion}`);
  exec.execSync(`npm publish --tag ${VERSION_TAG}`);
  if (BUILD_DOCUMENTATION_VERSION && BUILD_DOCUMENTATION_VERSION !== '') documentation.release(BUILD_DOCUMENTATION_VERSION, REMOVE_DOCUMENTATION_VERSION);
  if (isRelease) {
    exec.execSync(`git tag -a ${newVersion} -m "${newVersion}"`);
    exec.execSyncSilent(`git push deploy ${newVersion} || true`);
    updatePackageJsonGit(newVersion);
  }
}

function writePackageJson(packageJson) {
  fs.writeFileSync(packageJsonPath, JSON.stringify(packageJson, null, 2));
}

function readPackageJson() {
  return JSON.parse(fs.readFileSync(packageJsonPath));
}

function updatePackageJsonGit(version) {
  exec.execSync(`git checkout ${BRANCH}`);
  const packageJson = readPackageJson();
  packageJson.version = version;
  writePackageJson(packageJson);
  exec.execSync(`git add package.json`);
  exec.execSync(`git commit -m"Update package.json version to ${version} [ci skip]"`);
  exec.execSync(`git push deploy ${BRANCH}`);
  draftGitRelease(version);
}

function draftGitRelease(version) {
  exec.execSync(`npx gren release --tags=${version}`);
  exec.execSync(`sleep 1m`);
  // For some unknown reason, gren release works well only when calling it twice. 
  exec.execSync(`npx gren release --tags=${version}`);
}

run();
