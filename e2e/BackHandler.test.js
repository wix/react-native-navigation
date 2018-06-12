const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');
const Android = require('./AndroidUtils');

const { elementByLabel, elementById, sleep } = Utils;

describe(':android: screen stack', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('override hardware back button', async () => {
    await elementByLabel('BACK HANDLER').tap();
    await expect(elementByLabel('Back Handler Screen')).toBeVisible();

    await elementByLabel('ADD BACK HANDLER').tap();
    Android.pressBack();
    await sleep(100);
    await expect(elementByLabel('Back Handler Screen')).toBeVisible();

    await elementByLabel('REMOVE BACK HANDLER').tap();
    Android.pressBack();
    await sleep(100);
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  test('override hardware back button in modal with stack', async () => {
    await elementByLabel('BACK HANDLER').tap();
    await expect(elementByLabel('Back Handler Screen')).toBeVisible();

    await elementByLabel('SHOW MODAL WITH STACK').tap();
    await elementByLabel('ADD BACK HANDLER').tap();

    // Back is handled in Js
    Android.pressBack();
    await sleep(100);
    await expect(elementByLabel('Back button pressed!')).toBeVisible();

    // pop
    await elementByLabel('REMOVE BACK HANDLER').tap();
    Android.pressBack();
    await sleep(100);
    await expect(elementByLabel('Back Handler Screen')).toBeVisible();

    // modal dismissed
    Android.pressBack();
    await sleep(100);
    await expect(elementByLabel('Back Handler Screen')).toBeVisible();

    // main
    Android.pressBack();
    await sleep(100);
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });
});
