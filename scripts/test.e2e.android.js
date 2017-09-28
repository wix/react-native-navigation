/* eslint-disable no-console */
const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, 'release');

// Run just a single test, e.g. npm test-e2e-android -- just com.MyClass#myMethod
const filter = _(process.argv).dropWhile((a) => a !== 'just').take(2).last();

run();

function run() {
  if (process.env.CI) {
    console.log(`android e2e is disabled on CI`);
  } else {
    assertEnv();
    if (!isDeviceRunning()) {
      startEmulator();
    }
    runTests();
  }
}

function assertEnv() {
  if (_.isEmpty(process.env.ANDROID_HOME)) {
    throw new Error(`$ANDROID_HOME is not defined`);
  }
}

function isDeviceRunning() {
  try {
    const response = exec.execSyncRead(`adb -e shell getprop init.svc.bootanim 2>&1`);
    return _.isEqual(response, `stopped`);
  } catch (err) {
    return false;
  }
}

function startEmulator() {
  console.log(`Looking for avd...`);
  const avds = exec.execSyncRead(`avdmanager list avd -c`);
  const avdName = /^.*package\.xml(\S+)$/.exec(avds)[1];
  if (_.isEmpty(avdName)) {
    throw new Error(`Launch an android emulator or connect a device`);
  }
  console.log(`found avd name: ${avdName}, Launching...`);
  exec.execAsyncSilent(`${process.env.ANDROID_HOME}/tools/emulator -gpu host -no-audio @${avdName}`);
  exec.execSync(`./scripts/waitForAndroidEmulator.sh`);
}

function runTests() {
  exec.execSync(`npm run uninstall-android`);
  exec.execSync(`npm run install-android ${release ? '-- release' : ''}`);
  const filterParam = filter ? '-Pandroid.testInstrumentationRunnerArguments.class=' + filter : '';
  exec.execSync(`cd AndroidE2E && ./gradlew ${filterParam} connectedDebugAndroidTest`);
}

// const avdName = 'pixel';
// const sdk = 'android-24';
// const apis = 'default';
// const abi = 'armeabi-v7a';
// const packageName = `system-images;${sdk};${apis};${abi}`;

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
