const exec = require('shell-utils').exec;

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
    exec.execSync(`adb -s ${device.id} shell ${command}`);
  },
  setDemoMode: () => {
    // enter demo mode
    utils.executeShellCommand('settings put global sysui_demo_allowed 1');
    // display time 12:00
    utils.executeShellCommand(
      'am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1200'
    );
    // Display full mobile data with 4g type and no wifi
    utils.executeShellCommand(
      'am broadcast -a com.android.systemui.demo -e command network -e mobile show -e level 4 -e datatype 4g -e wifi false'
    );
    // Hide notifications
    utils.executeShellCommand(
      'am broadcast -a com.android.systemui.demo -e command notifications -e visible false'
    );
    // Disable pointer location
    utils.executeShellCommand('settings put system pointer_location 0');
    // Show full battery but not in charging state
    utils.executeShellCommand(
      'am broadcast -a com.android.systemui.demo -e command battery -e plugged false -e level 100'
    );
  },
};

export default utils;
