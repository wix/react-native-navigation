const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, '--release');

run();

function run() {
  try {
    startRecording();
    const conf = release ? `release` : `debug`;
    exec.execSync(`detox build --configuration ios.sim.${conf} && detox test --configuration ios.sim.${conf} ${process.env.CI ? '--cleanup' : ''}`);
  } catch (err) {
    stopRecording();
  }
}

function startRecording() {
  const json = JSON.parse(exec.execSyncRead(`applesimutils --list --byName "iPhone SE"`));
  const deviceId = json[0].udid;
  exec.execAsync(`xcrun simctl bootstatus ${deviceId} && xcrun simctl io booted recordVideo --type=mp4 video.mp4`);
}

function stopRecording() {
  const pid = exec.execSyncRead(`pgrep simctl`);
  exec.execSync(`kill -sigint ${pid}`);
  exec.execSync(`npm run release`);
}
