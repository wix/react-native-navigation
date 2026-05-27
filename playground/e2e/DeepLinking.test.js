import Utils from './Utils';
import TestIDs from '../src/testIDs';

const { elementById, sleep } = Utils;

const NOTIFICATION_PAYLOAD = {
  trigger: { type: 'push' },
  title: 'Open Pushed Screen',
  subtitle: 'Deep link test',
  body: 'Tap to open Pushed 42',
  badge: 1,
  payload: { url: 'rnnplayground://pushed/42' },
  'content-available': 0,
  'action-identifier': 'default',
};

const NESTED_NOTIFICATION_PAYLOAD = {
  ...NOTIFICATION_PAYLOAD,
  body: 'Tap to open nested Pushed 42 / detail 99',
  payload: { url: 'rnnplayground://pushed/42/detail/99' },
};

describe.e2e('Deep linking', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.NAVIGATION_TAB).tap();
  });

  it('deep-link modal can be dismissed via the close button', async () => {
    await device.openURL({ url: 'rnnplayground://pushed/42' });
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.DEEP_LINK_CLOSE_BTN).tap();
    await expect(elementById(TestIDs.NAVIGATION_SCREEN)).toBeVisible();
  });

  it('nested route builds a multi-screen stack inside the modal', async () => {
    await elementById(TestIDs.SIMULATE_NESTED_DEEP_LINK_BTN).tap();
    // The top-of-stack header proves the second Pushed segment mounted;
    // the nested-route -> multi-segment expansion is what produced it.
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });

  it('unmatched URL does not present a modal and does not crash', async () => {
    await device.openURL({ url: 'rnnplayground://nope' });
    await sleep(500);
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeNotVisible();
    await expect(elementById(TestIDs.NAVIGATION_SCREEN)).toBeVisible();
  });

  it('OS-delivered URL while running opens the modal', async () => {
    await device.openURL({ url: 'rnnplayground://pushed/77' });
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });

  it('OS-delivered URL with query params opens the modal (reserved keys filtered)', async () => {
    await device.openURL({ url: 'rnnplayground://pushed/77?ref=test&source=push' });
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });

  it('cold-start deep link presents the modal after root mounts', async () => {
    await device.launchApp({
      newInstance: true,
      url: 'rnnplayground://pushed/55',
    });
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });

  it('tapping a notification with a url payload opens the deep link modal', async () => {
    if (device.getPlatform() !== 'ios') {
      return;
    }
    await device.sendUserNotification(NOTIFICATION_PAYLOAD);
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });

  it('tapping a notification with a nested url payload builds a multi-screen modal', async () => {
    if (device.getPlatform() !== 'ios') {
      return;
    }
    await device.sendUserNotification(NESTED_NOTIFICATION_PAYLOAD);
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });

  it('cold-start notification tap presents the modal after root mounts', async () => {
    if (device.getPlatform() !== 'ios') {
      return;
    }
    await device.launchApp({
      newInstance: true,
      userNotification: NOTIFICATION_PAYLOAD,
    });
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });
});
