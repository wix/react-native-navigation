const exec = require('shell-utils').exec;

module.exports = {
  pressBack: () => {
    exec.execSync('adb shell input keyevent 4');
  },
  pressMenu: () => {
    exec.execSync('adb shell input keyevent 82');
  },
  pressKeyCode: (keyCode) => {
    exec.execSync(`adb shell input keyevent ${keyCode}`);
  },
  grantPermission: () => {
    exec.execSync('adb shell pm grant com.reactnativenavigation.playground android.permission.READ_PHONE_STATE');
  },
  revokePermission: () => {
    exec.execSync('adb shell pm revoke com.reactnativenavigation.playground android.permission.READ_PHONE_STATE');
  },
  openActivity: () => {
    exec.execSync('adb shell am start -n com.reactnativenavigation.playground/.MainActivity');
  },
  executeShellCommand: (command) => {
    exec.execSync(`adb shell ${command}`);
  }
};
