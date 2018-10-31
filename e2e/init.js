const detox = require('detox');
const config = require('../package.json').detox;
const exec = require('shell-utils').exec;
const adapter = require('detox/runners/jest/adapter');

jest.setTimeout(300000);
jasmine.getEnv().addReporter(adapter); // don't forget this line

beforeAll(async () => {
  await detox.init(config, {launchApp: false});
  disableAndroidEmulatorAnimations();
});

afterAll(async () => {
  await adapter.afterAll();
  await detox.cleanup();
});

beforeEach(async () => {
  await adapter.beforeEach();
});

function disableAndroidEmulatorAnimations() {
  if (device.getPlatform() === 'android') {
    const deviceId = device._deviceId;
    exec.execAsync(`adb -s ${deviceId} shell settings put global window_animation_scale 0.0`);
    exec.execAsync(`adb -s ${deviceId} shell settings put global transition_animation_scale 0.0`);
    exec.execAsync(`adb -s ${deviceId} shell settings put global animator_duration_scale 0.0`);
  }
}
