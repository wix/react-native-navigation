const includes = require('lodash/includes');
const exec = require('shell-utils').exec;

const android = includes(process.argv, '--android');
const release = includes(process.argv, '--release');
const BRANCH = process.env.BRANCH;
const RECORD = process.env.RECORD === 'true';

function run() {
  if (android) {
    runAndroidSnapshotTests();
  } else {
    runIosSnapshotTests();
  }
}

function runAndroidSnapshotTests() {}

function runIosSnapshotTests() {
  exec.execSync('npm run build');
  exec.execSync('npm run pod-install');
  testTarget(RECORD ? 'SnapshotRecordTests' : 'SnapshotTests', 'iPhone 11', '13.7');
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

  try {
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
  } catch (error) {
    if (!RECORD) {
      throw 'Snapshot tests failed';
    }
  } finally {
    if (RECORD) {
      pushSnapshots();
    }
  }
}

function pushSnapshots() {
  setupGit();
  exec.execSync(`git checkout ${BRANCH}`);
  exec.execSync(`git add ./playground/ios/SnapshotTests/ReferenceImages_64`);
  exec.execSync(`git commit -m "Update snapshots [ci skip]"`);
  exec.execSync(`git push deploy ${BRANCH}`);
}

function setupGit() {
  exec.execSyncSilent(`git config --global push.default simple`);
  exec.execSyncSilent(`git config --global user.email "${process.env.GIT_EMAIL}"`);
  exec.execSyncSilent(`git config --global user.name "${process.env.GIT_USER}"`);
  const remoteUrl = new RegExp(`https?://(\\S+)`).exec(exec.execSyncRead(`git remote -v`))[1];
  exec.execSyncSilent(
    `git remote add deploy "https://${process.env.GIT_USER}:${process.env.GIT_TOKEN}@${remoteUrl}"`
  );
}

run();
