import Utils from './Utils';
import Android from './AndroidUtils';
import TestIDs from '../playground/src/testIDs';
import includes from 'lodash/includes';

const { elementByLabel, elementById, sleep } = Utils;
const IS_RELEASE = includes(process.argv, '--release');
const KEY_CODE_R = 46;

describe.e2e('application lifecycle test', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
  });

  it('push a screen to ensure its not there after reload', async () => {
    await elementById(TestIDs.STACK_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
    await device.reloadReactNative();
    await expect(elementById(TestIDs.NAVIGATION_TAB)).toBeVisible();
  });

  it('relaunch from background', async () => {
    await elementById(TestIDs.STACK_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();

    await device.sendToHome();
    await device.launchApp({ newInstance: false });

    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  it(':android: relaunch after close with back button', async () => {
    await elementById(TestIDs.STACK_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();

    Android.pressBack();

    await device.launchApp({ newInstance: false });
    await expect(elementByLabel('Pushed Screen')).toBeNotVisible();
  });

  it('device orientation does not destroy activity', async () => {
    await elementById(TestIDs.STACK_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();

    await device.setOrientation('landscape');

    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  it(':android: relaunch after activity killed by system', async () => {
    await elementById(TestIDs.STACK_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
    await device.sendToHome();

    await togglePhonePermission();
    await sleep(5000);
    await device.launchApp({ newInstance: false });

    await expect(elementByLabel('Pushed Screen')).toBeNotVisible();
    await expect(elementById(TestIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  it(':android: pressing r twice with delay does nothing', async () => {
    if (!IS_RELEASE) {
      await elementById(TestIDs.STACK_BTN).tap();
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementByLabel('Pushed Screen')).toBeVisible();

      Android.pressKeyCode(KEY_CODE_R);
      await sleep(1000);
      Android.pressKeyCode(KEY_CODE_R);

      await expect(elementByLabel('Pushed Screen')).toBeVisible();
    }
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
      await elementById(TestIDs.PUSH_BTN).tap();
      await expect(elementByLabel('Pushed Screen')).toBeVisible();

      Android.pressKeyCode(KEY_CODE_R);
      Android.pressKeyCode(KEY_CODE_R);

      await sleep(300);
      await expect(elementByLabel('React Native Navigation!')).toBeVisible();
    }
  });

  xit(':android: sending reload broadcast reloads react native', async () => {
    if (!IS_RELEASE) {
      await elementById(TestIDs.PUSH_BTN).tap();
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
