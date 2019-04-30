const _ = require('lodash');
const exec = require('shell-utils').exec;

const android = _.includes(process.argv, '--android');
const release = _.includes(process.argv, '--release');

function run() {
  if (android) {
    runAndroidUnitTests();
  } else {
    runIosUnitTests();
  }
}

function runAndroidUnitTests() {
  const conf = release ? 'testReactNative57_5ReleaseUnitTest' : 'testReactNative57_5DebugUnitTest';
  if (android && process.env.JENKINS_CI) {
    const sdkmanager = '/usr/local/share/android-sdk/tools/bin/sdkmanager';
    exec.execSync(`yes | ${sdkmanager} --licenses`);
    // exec.execSync(`echo y | ${sdkmanager} --update && echo y | ${sdkmanager} --licenses`);
  }
  exec.execSync(`cd lib/android && ./gradlew ${conf}`);
}

function runIosUnitTests() {
  const conf = release ? `Release` : `Debug`;

  exec.execSync(`cd ./playground/ios &&
            RCT_NO_LAUNCH_PACKAGER=true
            xcodebuild build build-for-testing
            -scheme "ReactNativeNavigation"
            -project playground.xcodeproj
            -sdk iphonesimulator
            -configuration ${conf}
            -derivedDataPath ./DerivedData/playground
            -quiet
            -UseModernBuildSystem=NO
            ONLY_ACTIVE_ARCH=YES`);

  exec.execSync(`cd ./playground/ios &&
            RCT_NO_LAUNCH_PACKAGER=true
            xcodebuild test-without-building
            -scheme "ReactNativeNavigation"
            -project playground.xcodeproj
            -sdk iphonesimulator
            -configuration ${conf}
            -destination 'platform=iOS Simulator,name=iPhone X'
            -derivedDataPath ./DerivedData/playground
            ONLY_ACTIVE_ARCH=YES`);
}

run();
