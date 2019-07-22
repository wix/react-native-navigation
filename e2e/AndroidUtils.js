const exec = require('shell-utils').exec;

module.exports = {
  pressBack: () => {
    exec.execSync('adb shell input keyevent 4');
  },
  pressMenu: () => {
    exec.execSync('adb shell input keyevent 82');
  },
  pressEnter: (keyCode) => {
    exec.execSync(`adb shell input keyevent 66`);
  },
  pressLockButton: () => {
    exec.execSync(`adb shell input keyevent 26`);
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
  },
  enterText: (text) => {
    exec.execSync(`adb shell input text ${text}`);
  },
  swipeUp: () => {
    exec.execSync('adb shell input touchscreen swipe 300 1200 500 0 100');
  }
};
