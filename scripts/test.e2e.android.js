/* eslint-disable no-console */
const _ = require('lodash');
const exec = require('shell-utils').exec;

// const avdName = 'pixel';
// const sdk = 'android-24';
// const apis = 'default';
// const abi = 'armeabi-v7a';
// const packageName = `system-images;${sdk};${apis};${abi}`;

const release = _.includes(process.argv, 'release');

// Run just a single test, e.g. npm test-e2e-android -- just com.MyClass#myMethod
const filter = _(process.argv).dropWhile((a) => a !== 'just').take(2).last();

run();

function run() {
  if (process.env.CI) {
    console.log(`android e2e is disabled on CI`);
  } else {
    runTests();
  }
}

function runTests() {
  exec.execSync(`npm run uninstall-android`);
  exec.execSync(`npm run install-android ${release ? '-- release' : ''}`);
  const filterParam = filter ? '-Pandroid.testInstrumentationRunnerArguments.class=' + filter : '';
  exec.execSync(`cd AndroidE2E && ./gradlew ${filterParam} connectedDebugAndroidTest`);
}

// function installEmulator() {
//   exec.execSync(`sdkmanager "emulator"`);
//   exec.execSync(`sdkmanager "${packageName}"`);
//   exec.execSync(`echo no | avdmanager create avd --force --name "${avdName}" --abi "${apis}/${abi}" --package "${packageName}" --device "pixel"`);
//   exec.execSync(`avdmanager list avd`);
// }

// function launchEmulator() {
//   console.log(`Launching Android Emulator`);
//   exec.execSync(`cd $ANDROID_HOME/tools && ./emulator -skin 1080x1920 -gpu host -no-audio @${avdName}`);
//   exec.execSync(`./scripts/waitForAndroidEmulator.sh`);
// }

// function killEmulators() {
//   exec.execSync(`adb devices | grep emulator | cut -f1 | while read line; do adb -s $line emu kill; done`);
// }
