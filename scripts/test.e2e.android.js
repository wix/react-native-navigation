const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, 'release');

function run() {
  if (process.env.CI) {
    console.log(`android e2e disabled on CI for now`); //eslint-disable-line
    return;
  }

  exec.execSync(`yarn run uninstall-android`);
  exec.execSync(`yarn run install-android ${release ? '-- release' : ''}`);
  exec.execSync(`cd AndroidE2E && ./gradlew connectedDebugAndroidTest`);
}

run();
