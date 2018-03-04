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

// Temporary solution, #2809
function disableAndroidEmulatorAnimations() {
  exec.execAsync(`adb shell settings put global window_animation_scale 0.0`);
  exec.execAsync(`adb shell settings put global transition_animation_scale 0.0`);
  exec.execAsync(`adb shell settings put global animator_duration_scale 0.0`);
}
