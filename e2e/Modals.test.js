const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');
const Android = require('./AndroidUtils');

const { elementByLabel, elementById, tapDeviceBackAndroid } = Utils;

describe('modal', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test('show modal', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementById(testIDs.MODAL_SCREEN)).toBeVisible();
  });

  test('dismiss modal', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementById(testIDs.MODAL_SCREEN)).toBeVisible();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('unmount modal when dismissed', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementById(testIDs.MODAL_SCREEN)).toBeVisible();
    await elementById(testIDs.MODAL_LIFECYCLE_BUTTON).tap();
    await expect(elementByLabel('didAppear')).toBeVisible();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementByLabel('componentWillUnmount')).toBeVisible();
    await elementByLabel('OK').atIndex(0).tap();
    await expect(elementByLabel('didDisappear')).toBeVisible();
    await elementByLabel('OK').atIndex(0).tap();
  });

  test('show multiple modals', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('dismiss unknown screen id', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(testIDs.DISMISS_UNKNOWN_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('dismiss modal by id which is not the top most', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(testIDs.DISMISS_PREVIOUS_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test(
    'dismiss all previous modals by id when they are below top presented modal',
    async () => {
      await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
      await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
      await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
      await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
      await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
      await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

      await elementById(testIDs.DISMISS_ALL_PREVIOUS_MODAL_BUTTON).tap();
      await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

      await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
      await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
    }
  );

  test('dismiss some modal by id deep in the stack', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementById(testIDs.DISMISS_FIRST_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();

    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('dismissAllModals', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementById(testIDs.DISMISS_ALL_MODALS_BUTTON).tap();
    await expect(elementById(testIDs.WELCOME_SCREEN_HEADER)).toBeVisible();
  });

  test('push into modal', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  test(':android: push into modal', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    await elementById(testIDs.PUSH_BUTTON).tap();
    Android.pressBack();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
  });

  test('present modal multiple times', async () => {
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await elementById(testIDs.SHOW_MODAL_BUTTON).tap();
    await elementById(testIDs.DISMISS_MODAL_BUTTON).tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
  });
});
