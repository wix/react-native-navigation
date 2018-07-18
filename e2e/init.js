const detox = require('detox');
const config = require('../package.json').detox;
const exec = require('shell-utils').exec;
jest.setTimeout(300000);

beforeAll(async () => {
  await detox.init(config, { launchApp: false });
  disableAndroidEmulatorAnimations();
});

afterAll(async () => {
  await detox.cleanup();
});

beforeEach(async function() {
    await detox.beforeEach(jasmine.testPath);
});

afterEach(async function() {
    await detox.afterEach(jasmine.testPath);
});

function disableAndroidEmulatorAnimations() {
  if (device.getPlatform() === 'android') {
    const deviceId = device._deviceId;
    exec.execAsync(`adb -s ${deviceId} shell settings put global window_animation_scale 0.0`);
    exec.execAsync(`adb -s ${deviceId} shell settings put global transition_animation_scale 0.0`);
    exec.execAsync(`adb -s ${deviceId} shell settings put global animator_duration_scale 0.0`);
  }
}
