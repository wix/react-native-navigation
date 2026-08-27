const includes = require('lodash/includes');
const exec = require('shell-utils').exec;

const android = includes(process.argv, '--android');
const release = includes(process.argv, '--release');

function run() {
  if (android) {
    runAndroidUnitTests();
  } else {
    runIosUnitTests();
  }
}

function runAndroidUnitTests() {
  // AGP 9 only creates the library's debug unit-test variant by default.
  // The aggregate task runs every available RNN unit-test variant.
  const conf = ':react-native-navigation:test';
  if (android && process.env.JENKINS_CI) {
    const sdkmanager = '/usr/local/share/android-sdk/tools/bin/sdkmanager';
    exec.execSync(`yes | ${sdkmanager} --licenses`);
    // exec.execSync(`echo y | ${sdkmanager} --update && echo y | ${sdkmanager} --licenses`);
  }
  exec.execSync(`cd playground/android && ./gradlew ${conf}`);
  exec.execSync(
    `cd playground/android && ./gradlew :react-native-navigation:pixel3aapi34DebugAndroidTest`
  );
}

function runIosUnitTests() {
  exec.execSync('yarn run pod-install');
  testTarget(
    'playground',
    process.env.IOS_TEST_DEVICE || 'iPhone 13',
    process.env.IOS_TEST_OS || 'latest'
  );
}

function testTarget(scheme, device, OS = 'latest') {
  const conf = release ? `Release` : `Debug`;
  exec.execSync(`cd ./playground/ios &&
  RCT_NO_LAUNCH_PACKAGER=true
  xcodebuild build build-for-testing
  -scheme "${scheme}"
  -workspace playground.xcworkspace
  -sdk iphonesimulator
  -configuration ${conf}
  -derivedDataPath ./DerivedData/playground
  -UseModernBuildSystem=YES
  DEAD_CODE_STRIPPING=NO
  ONLY_ACTIVE_ARCH=YES`);

  exec.execSync(`cd ./playground/ios &&
  RCT_NO_LAUNCH_PACKAGER=true
  xcodebuild test-without-building
  -scheme "${scheme}"
  -workspace playground.xcworkspace
  -sdk iphonesimulator
  -configuration ${conf}
  -destination 'platform=iOS Simulator,name=${device},OS=${OS}'
  -derivedDataPath ./DerivedData/playground
  ONLY_ACTIVE_ARCH=YES`);
}

run();
