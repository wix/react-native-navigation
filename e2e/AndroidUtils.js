const exec = require('shell-utils').exec;
const utils = {
  pressBack: () => utils.pressKeyCode(4),
  pressMenu: () => utils.pressKeyCode(82),
  pressKeyCode: (keyCode) => utils.executeShellCommand(`input keyevent ${keyCode}`),
  grantPermission: () =>
    utils.executeShellCommand(
      'pm grant com.reactnativenavigation.playground android.permission.READ_PHONE_STATE'
    ),
  revokePermission: () =>
    utils.executeShellCommand(
      'pm revoke com.reactnativenavigation.playground android.permission.READ_PHONE_STATE'
    ),
  executeShellCommand: (command) => {
    exec.execSync(`adb -s ${device.id} shell ${command}`);
  },
};

export default utils;
