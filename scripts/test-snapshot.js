const includes = require('lodash/includes');
const exec = require('shell-utils').exec;

const android = includes(process.argv, '--android');
const release = includes(process.argv, '--release');

function run() {
  if (android) {
    runAndroidSnapshotTests();
  } else {
    runIosSnapshotTests();
  }
}

function runAndroidSnapshotTests() {

}

function runIosSnapshotTests() {
  exec.execSync('npm run build');
  exec.execSync('npm run pod-install');
  testTarget('SnapshotTests', 'iPhone 11');
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
  -UseModernBuildSystem=NO
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
