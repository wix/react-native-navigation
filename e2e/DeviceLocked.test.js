const Utils = require('./Utils');
const Android = require('./AndroidUtils');
const testIDs = require('../playground/src/testIDs');
const exec = require('shell-utils').exec;
const _ = require('lodash');

const { elementByLabel, elementById, sleep } = Utils;
const IS_RELEASE = _.includes(process.argv, '--release');
const KEY_CODE_R = 46;

describe('application lifecycle test', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('launch from locked screen', async () => {
    await device.terminateApp();
    await Android.pressKeyCode('26')
    await Android.pressKeyCode('26')
    await device.launchApp();
    await Android.executeShellCommand(`input touchscreen swipe 300 1200 500 0 100`)
    await Android.unlockPhoneByPin('1234')
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  test('launch app from unlocked screen', async () => {
    await device.terminateApp();
    await Android.pressKeyCode('26')
    await Android.pressKeyCode('26')
    await Android.executeShellCommand(`input touchscreen swipe 300 1200 500 0 100`)
    await Android.unlockPhoneByPin('1234')
    await device.launchApp();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });
});
