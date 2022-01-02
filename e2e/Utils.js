import {execSync} from "shell-utils/src/exec";
import {readFileSync} from "fs";

const utils = {
   setDemoMode:()=> {
        if (device.getPlatform()  === 'ios') {
            execSync(
                'xcrun simctl status_bar "iPhone 11" override --time "12:00" --batteryState charged --batteryLevel 100 --wifiBars 3 --cellularMode active --cellularBars 4'
            );
        } else {
            // enter demo mode
            execSync('adb shell settings put global sysui_demo_allowed 1');
            // display time 12:00
            execSync('adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1200');
            // Display full mobile data with 4g type and no wifi
            execSync(
                'adb shell am broadcast -a com.android.systemui.demo -e command network -e mobile show -e level 4 -e datatype 4g -e wifi false'
            );
            // Hide notifications
            execSync('adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false');
            // Show full battery but not in charging state
            execSync('adb shell am broadcast -a com.android.systemui.demo -e command battery -e plugged false -e level 100');
        }
    },
  elementByLabel: (label) => {
    return element(by.text(label));
  },
  elementById: (id) => {
    return element(by.id(id));
  },
  elementByTraits: (traits) => {
    return element(by.traits(traits));
  },
  tapBackIos: () => {
    try {
      return element(by.traits(['button']).and(by.label('Back')))
        .atIndex(0)
        .tap();
    } catch (err) {
      return element(by.type('_UIModernBarButton').and(by.label('Back'))).tap();
    }
  },
  sleep: (ms) => new Promise((res) => setTimeout(res, ms)),
  expectBitmapsToBeEqual:(imagePath, expectedImagePath)=> {
    const bitmapBuffer = readFileSync(imagePath);
    const expectedBitmapBuffer = readFileSync(expectedImagePath);
    if (!bitmapBuffer.equals(expectedBitmapBuffer)) {
        throw new Error(`Expected image at ${imagePath} to be equal to image at ${expectedImagePath}, but it was different!`);
    }
},
    expectBitmapsToBeNotEqual:(imagePath, expectedImagePath)=> {
        const bitmapBuffer = readFileSync(imagePath);
        const expectedBitmapBuffer = readFileSync(expectedImagePath);
        if (bitmapBuffer.equals(expectedBitmapBuffer)) {
            throw new Error(`Expected image at ${imagePath} to be not equal to image at ${expectedImagePath}, but it was different!`);
        }
    }
};

export default utils;
