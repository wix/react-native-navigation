import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementById } = Utils;

describe('Stack SetRoot', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
  });

  it.e2e('SetStackRoot on a non created tab should work', async () => {
    await elementById(TestIDs.SET_ROOT_NAVIGATION_TAB).tap();
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.BACK_BUTTON).tap();
    await expect(elementById(TestIDs.NAVIGATION_SCREEN)).toBeVisible();
  });

});
