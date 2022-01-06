import { execSync } from 'shell-utils/src/exec';

const utils = {
  setDemoMode: () => {
    execSync(
      'xcrun simctl status_bar "iPhone 11" override --time "12:00" --batteryState charged --batteryLevel 100 --wifiBars 3 --cellularMode active --cellularBars 4'
    );
  },
};

export default utils;
