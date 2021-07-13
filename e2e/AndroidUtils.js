const exec = require('shell-utils').exec;
const isCI = process.env.CI === 'true';

const utils = {
  pressBack: () => device.pressBack(),
  pressMenu: () => device.getUiDevice().pressMenu(),
  pressKeyCode: (keyCode) => device.getUiDevice().pressKeyCode(keyCode),
  grantPermission: () =>
    utils.executeShellCommand(
      'pm grant com.reactnativenavigation.playground android.permission.READ_PHONE_STATE'
    ),
  revokePermission: () =>
    utils.executeShellCommand(
      'pm revoke com.reactnativenavigation.playground android.permission.READ_PHONE_STATE'
    ),
  executeShellCommand: (command) => {
    exec.execSync(`adb -s ${isCI ? device.id.adbName : device.id} shell ${command}`);
  },
};

export default utils;
