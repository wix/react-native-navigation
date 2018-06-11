
const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const { elementById } = Utils;

describe(':ios: orientation', () => {

  beforeEach(async () => {
    await device.relaunchApp();
    waitForDeviceToSettleAfterOrientationChangeAndroid = ms => new Promise(res => setTimeout(res, device.getPlatform() === 'ios' ? 0 : 400));
  });

  test('default allows all', async () => {
    await elementById(testIDs.ORIENTATION_BUTTON).tap();
    await elementById(testIDs.DEFAULT_ORIENTATION_BUTTON).tap();
    waitForDeviceToSettleAfterOrientationChangeAndroid();
    await expect(elementById(testIDs.PORTRAIT_ELEMENT)).toBeVisible();
    await device.setOrientation('landscape');
    waitForDeviceToSettleAfterOrientationChangeAndroid();
    await expect(elementById(testIDs.LANDSCAPE_ELEMENT)).toBeVisible();
    await device.setOrientation('portrait');
    waitForDeviceToSettleAfterOrientationChangeAndroid();
    await expect(elementById(testIDs.PORTRAIT_ELEMENT)).toBeVisible();
    await elementById(testIDs.DISMISS_BUTTON).tap();
  });

  test('landscape and portrait array', async () => {
    await elementById(testIDs.ORIENTATION_BUTTON).tap();
    await elementById(testIDs.LANDSCAPE_PORTRAIT_ORIENTATION_BUTTON).tap();
    await expect(element(by.id(testIDs.PORTRAIT_ELEMENT))).toBeVisible();
    await device.setOrientation('landscape');
    waitForDeviceToSettleAfterOrientationChangeAndroid();
    await expect(element(by.id(testIDs.LANDSCAPE_ELEMENT))).toBeVisible();
    await device.setOrientation('portrait');
    waitForDeviceToSettleAfterOrientationChangeAndroid();
    await expect(element(by.id(testIDs.PORTRAIT_ELEMENT))).toBeVisible();
    await elementById(testIDs.DISMISS_BUTTON).tap();
  });

  test(':ios: portrait only', async () => {
    await elementById(testIDs.ORIENTATION_BUTTON).tap();
    await elementById(testIDs.PORTRAIT_ORIENTATION_BUTTON).tap();
    await expect(elementById(testIDs.PORTRAIT_ELEMENT)).toBeVisible();
    await device.setOrientation('landscape');
    await expect(elementById(testIDs.PORTRAIT_ELEMENT)).toBeVisible();
    await device.setOrientation('portrait');
    await expect(elementById(testIDs.PORTRAIT_ELEMENT)).toBeVisible();
    await elementById(testIDs.DISMISS_BUTTON).tap();
  });

  test(':ios: landscape only', async () => {
    await elementById(testIDs.ORIENTATION_BUTTON).tap();
    await elementById(testIDs.LANDSCAPE_ORIENTATION_BUTTON).tap();
    await device.setOrientation('landscape');
    await expect(element(by.id(testIDs.LANDSCAPE_ELEMENT))).toBeVisible();
    await device.setOrientation('portrait');
    await expect(element(by.id(testIDs.LANDSCAPE_ELEMENT))).toBeVisible();
    await elementById(testIDs.DISMISS_BUTTON).tap();
  });
});
