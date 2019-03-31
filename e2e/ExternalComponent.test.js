const Utils = require('./Utils');
const TestIDs = require('../playground/src/testIDs');
const Android = require('./AndroidUtils');

const { elementByLabel, elementById } = Utils;

describe('External Component', () => {
  beforeEach(async () => {
    await device.relaunchApp();
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
});
