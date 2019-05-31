const Utils = require('./Utils');
const Android = require('./AndroidUtils');

const { elementByLabel } = Utils;

describe('application lifecycle test', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('launch from locked screen', async () => {
    await device.terminateApp();
    await Android.pressKeyCode('26')
    await Android.pressKeyCode('26')
    await device.launchApp();
    await Android.swipeUp()
    await Android.unlockPhoneByPin('1234')
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  test('launch app from unlocked screen', async () => {
    await device.terminateApp();
    await Android.pressKeyCode('26')
    await Android.pressKeyCode('26')
    await Android.swipeUp()
    await Android.unlockPhoneByPin('1234')
    await device.launchApp();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });
});
