const Utils = require('./Utils');
const Android = require('./AndroidUtils');

const { elementByLabel } = Utils;

xdescribe(':android: Android phone locked tests', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('launch from locked screen', async () => {
    await device.terminateApp();
    await Android.pressLockButton();
    await Android.pressLockButton();
    await device.launchApp();
    await Android.swipeUp();
    // The device should be locked using PIN 1234
    await Android.enterText('1234');
    await Android.pressEnter();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  test('launch app from unlocked screen', async () => {
    await device.terminateApp();
    await Android.pressLockButton();
    await Android.pressLockButton();
    await Android.swipeUp();
    // The device should be locked using PIN 1234
    await Android.enterText('1234');
    await Android.pressEnter();
    await device.launchApp();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });
});
