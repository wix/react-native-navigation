/* eslint-disable no-console */
const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, '--release');

// Run just a single test, e.g. npm test-e2e-android -- --just com.MyClass#myMethod
const filter = _.chain(process.argv).dropWhile((a) => a !== '--just').take(2).last().value();

run();

function run() {
  if (process.env.CI) {
    console.log(`android e2e is disabled on CI until Travis will support x86 emulators or we migrate to our own solution`);
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
  const avdsRaw = exec.execSyncRead(`${process.env.ANDROID_HOME}/tools/bin/avdmanager list avd -c`);
  const avdName = /^.*package\.xml(\S+)$/m.exec(avdsRaw)[1];
  if (_.isEmpty(avdName)) {
    throw new Error(`Can't find avd to launch. Please create an avd first.`);
  }
  console.log(`found avd name: ${avdName}, Launching...`);
  exec.execAsyncSilent(`${process.env.ANDROID_HOME}/tools/emulator -gpu host -no-audio @${avdName}`);
  exec.execSync(`./scripts/waitForAndroidEmulator.sh`);
}

function runTests() {
  exec.execSync(`npm run uninstall-android`);
  exec.execSync(`npm run install-android ${release ? '-- --release' : ''}`);
  const filterParam = filter ? '-Pandroid.testInstrumentationRunnerArguments.class=' + filter : '';
  exec.execSync(`cd AndroidE2E && ./gradlew ${filterParam} connectedDebugAndroidTest`);
}
