import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementByLabel, elementById } = Utils;

describe.e2e('External Component', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.EXTERNAL_COMP_BTN).tap();
  });

  test('Push external component', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('This is an external component')).toBeVisible();
  });

  test('Show external component in deep stack in modal', async () => {
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('External component in deep stack')).toBeVisible();
  });

  test(':ios: Dismiss modal from external component should not crash', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('This is an external component')).toBeVisible();
    await elementById(TestIDs.EXTERNAL_DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_SCREEN)).toBeVisible();
  });

  test(':ios: Top bar buttons should be visible', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('This is an external component')).toBeVisible();
    await expect(elementById(TestIDs.EXTERNAL_TOP_BAR_RIGHT_BTN)).toBeVisible();
  });

  test(':ios: Dismiss modal from external component should not crash when registered to modalDismissed event', async () => {
    await elementById(TestIDs.REGISTER_MODAL_DISMISS_EVENT_BTN).tap();
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementByLabel('This is an external component')).toBeVisible();
    await elementById(TestIDs.EXTERNAL_DISMISS_MODAL_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_SCREEN)).toBeVisible();
  });

  test(':ios: External component should receive options', async () => {
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementByLabel('External Component')).toBeVisible();
  });

  test(':ios: Push and pop native component', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.PUSH_NATIVE_BTN).tap();
    await elementById(TestIDs.POP_NATIVE_BTN).tap();
    await expect(elementByLabel('This is an external component')).toBeVisible();
  });
});
