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

  test('push a screen to ensure its not there after reload', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
    await device.reloadReactNative();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('relaunch from background', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();

    await device.sendToHome();
    await device.launchApp();

    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  test(':android: relaunch after close with back button', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();

    Android.pressBack();

    await device.launchApp();
    await expect(elementByLabel('Pushed Screen')).toBeNotVisible();
  });

  test('device orientation does not destroy activity', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();

    await device.setOrientation('landscape');

    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  test(':android: relaunch after activity killed by system', async () => {
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
    await device.sendToHome();

    await togglePhonePermission();
    await device.launchApp();

    await expect(elementByLabel('Pushed Screen')).toBeNotVisible();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  xit(':android: pressing menu opens dev menu', async () => {
    if (!IS_RELEASE) {
      Android.pressMenu();
      await sleep(1000);
      await expect(elementByLabel('Reload')).toBeVisible();
    }
  });

  xit(':android: pressing r twice in succession reloads React Native', async () => {
    if (!IS_RELEASE) {
      await elementById(testIDs.PUSH_BUTTON).tap();
      await expect(elementByLabel('Pushed Screen')).toBeVisible();

      Android.pressKeyCode(KEY_CODE_R);
      Android.pressKeyCode(KEY_CODE_R);

      await sleep(300);
      await expect(elementByLabel('React Native Navigation!')).toBeVisible();
    }
  });

  test(':android: pressing r twice with delay does nothing', async () => {
    if (!IS_RELEASE) {
      await elementById(testIDs.PUSH_BUTTON).tap();
      await expect(elementByLabel('Pushed Screen')).toBeVisible();

      Android.pressKeyCode(KEY_CODE_R);
      await sleep(1000);
      Android.pressKeyCode(KEY_CODE_R);

      await expect(elementByLabel('Pushed Screen')).toBeVisible();
    }
  });

  xit(':android: sending reload broadcast reloads react native', async () => {
    if (!IS_RELEASE) {
      await elementById(testIDs.PUSH_BUTTON).tap();
      await expect(elementByLabel('Pushed Screen')).toBeVisible();

      Android.executeShellCommand('am broadcast -a com.reactnativenavigation.broadcast.RELOAD');

      await sleep(1000);
      await expect(elementByLabel('React Native Navigation!')).toBeVisible();
    }
  });

  async function togglePhonePermission() {
    await sleep(700);
    Android.revokePermission();
    await sleep(700);
    Android.grantPermission();
    await sleep(1000);
  }
});
