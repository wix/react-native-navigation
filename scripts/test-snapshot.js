const { execFileSync } = require('child_process');
const fs = require('fs');
const path = require('path');

function run() {
  if (process.argv.includes('--android')) {
    throw new Error('Android snapshots are verified by the Detox E2E suites.');
  }
  const root = path.resolve(__dirname, '..');
  const ios = path.join(root, 'playground/ios');
  const record = process.env.RECORD === 'true';
  const options = { cwd: root, stdio: 'inherit' };
  execFileSync('yarn', ['prepare'], options);
  execFileSync('yarn', ['pod-install'], options);

  const args = [
    '-scheme',
    record ? 'SnapshotRecordTests' : 'SnapshotTests',
    '-workspace',
    'playground.xcworkspace',
    '-sdk',
    'iphonesimulator',
    '-configuration',
    process.argv.includes('--release') ? 'Release' : 'Debug',
    '-destination',
    `platform=iOS Simulator,name=${process.env.IOS_TEST_DEVICE || 'iPhone 13'},OS=${
      process.env.IOS_TEST_OS || 'latest'
    }`,
    '-derivedDataPath',
    './DerivedData/playground',
    'DEAD_CODE_STRIPPING=NO',
    'ONLY_ACTIVE_ARCH=YES',
  ];
  const buildOptions = {
    cwd: ios,
    stdio: 'inherit',
    env: { ...process.env, RCT_NO_LAUNCH_PACKAGER: 'true' },
  };
  try {
    execFileSync('xcodebuild', ['build-for-testing', ...args], buildOptions);
    execFileSync('xcodebuild', ['test-without-building', ...args], buildOptions);
  } catch (error) {
    const diffs = path.join(ios, 'SnapshotTests/FailureDiffs');
    if (fs.existsSync(diffs)) {
      fs.cpSync(diffs, path.join(root, 'artifacts/ios-snapshot-diffs'), { recursive: true });
    }
    throw error;
  } finally {
    if (record) {
      console.log(
        'Recorded snapshots remain local. Review the image changes before committing them.'
      );
    }
  }
}

if (require.main === module) run();

module.exports = { run };
