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
  const conf = release ? 'testReactNative63ReleaseUnitTest' : 'testReactNative63DebugUnitTest';
  if (android && process.env.JENKINS_CI) {
    const sdkmanager = '/usr/local/share/android-sdk/tools/bin/sdkmanager';
    exec.execSync(`yes | ${sdkmanager} --licenses`);
    // exec.execSync(`echo y | ${sdkmanager} --update && echo y | ${sdkmanager} --licenses`);
  }
  exec.execSync(`cd playground/android && ./gradlew ${conf}`);
}

function runIosUnitTests() {
  exec.execSync('npm run build');
  exec.execSync('npm run pod-install');
  testTarget('playground', 'iPhone 11');
  testTarget('playgroundIOS12', 'iPhone X', '12.4');
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
  -quiet
  -UseModernBuildSystem=YES
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
