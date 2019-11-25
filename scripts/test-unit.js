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
  const conf = release ? 'testReactNative60ReleaseUnitTest' : 'testReactNative60DebugUnitTest';
  if (android && process.env.JENKINS_CI) {
    const sdkmanager = '/usr/local/share/android-sdk/tools/bin/sdkmanager';
    exec.execSync(`yes | ${sdkmanager} --licenses`);
    // exec.execSync(`echo y | ${sdkmanager} --update && echo y | ${sdkmanager} --licenses`);
  }
  exec.execSync(`cd lib/android && ./gradlew ${conf}`);
}

function runIosUnitTests() {
  const conf = release ? `Release` : `Debug`;

  exec.execSync('npm run build');
  exec.execSync('npm run pod-install');
  exec.execSync(`cd ./playground/ios &&
            RCT_NO_LAUNCH_PACKAGER=true
            xcodebuild build build-for-testing
            -scheme "playground"
            -workspace playground.xcworkspace
            -sdk iphonesimulator
            -configuration ${conf}
            -derivedDataPath ./DerivedData/playground
            -quiet
            -UseModernBuildSystem=NO
            ONLY_ACTIVE_ARCH=YES`);

  exec.execSync(`cd ./playground/ios &&
            RCT_NO_LAUNCH_PACKAGER=true
            xcodebuild test-without-building
            -scheme "playground"
            -workspace playground.xcworkspace
            -sdk iphonesimulator
            -configuration ${conf}
            -destination 'platform=iOS Simulator,name=iPhone 11'
            -derivedDataPath ./DerivedData/playground
            ONLY_ACTIVE_ARCH=YES`);
}

run();
