const detox = require('detox');
const config = require('../package.json').detox;
const exec = require('shell-utils').exec;

before(async () => {
  await detox.init(config, { launchApp: false });
  disableAndroidEmulatorAnimations();
});

after(async () => {
  await detox.cleanup();
});

function disableAndroidEmulatorAnimations() {
  if (device.getPlatform() === 'android') {
    const deviceId = device._deviceId;
    exec.execAsync(`adb -s ${deviceId} shell settings put global window_animation_scale 0.0`);
    exec.execAsync(`adb -s ${deviceId} shell settings put global transition_animation_scale 0.0`);
    exec.execAsync(`adb -s ${deviceId} shell settings put global animator_duration_scale 0.0`);
  }
}
