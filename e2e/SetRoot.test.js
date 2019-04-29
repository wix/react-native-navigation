const Utils = require('./Utils');
const TestIDs = require('../playground/src/testIDs');
const { elementById } = Utils;

describe('SetRoot', () => {
  beforeEach(async () => {
    await device.relaunchApp();
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.SET_ROOT_BTN).tap();
  });

  it('set root multiple times with the same componentId', async () => {
    await elementById(TestIDs.SET_MULTIPLE_ROOTS_BTN).tap();
    await expect(elementById(TestIDs.PUSHED_SCREEN_HEADER)).toBeVisible();
  });
});
