const detox_ = require('detox');
const exec = require('shell-utils').exec;
const detoxConfig = require('../package.json').detox;

const {
  DetoxCircusEnvironment,
  SpecReporter,
  WorkerAssignReporter,
} = require('detox/runners/jest-circus');

class CustomDetoxEnvironment extends DetoxCircusEnvironment {
  constructor(config) {
    super(config);

    // Can be safely removed, if you are content with the default value (=300000ms)
    this.initTimeout = 300000;

    // This takes care of generating status logs on a per-spec basis. By default, Jest only reports at file-level.
    // This is strictly optional.
    this.registerListeners({
      SpecReporter,
      WorkerAssignReporter,
    });
  }

  async setup() {
    await super.setup();
    await detox_.init(detoxConfig, { launchApp: false });
    disableAndroidEmulatorAnimations();
  }
}

function disableAndroidEmulatorAnimations() {
  if (detox_.device.getPlatform() === 'android') {
    const deviceId = detox_.device.id;
    exec.execAsync(`adb -s ${deviceId} shell settings put global window_animation_scale 0.0`);
    exec.execAsync(`adb -s ${deviceId} shell settings put global transition_animation_scale 0.0`);
    exec.execAsync(`adb -s ${deviceId} shell settings put global animator_duration_scale 0.0`);
  }
}

module.exports = CustomDetoxEnvironment;
