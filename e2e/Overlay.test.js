const Utils = require('./Utils');
const TestIDs = require('../playground/src/testIDs');
const { elementByLabel, elementById } = Utils;

describe('Overlay', () => {
  beforeEach(async () => {
    await device.relaunchApp();
    await elementById(TestIDs.NAVIGATION_TAB).tap();
    await elementById(TestIDs.OVERLAY_BTN).tap();
  });

  it('show and dismiss overlay', async () => {
    await elementById(TestIDs.SHOW_OVERLAY_BTN).tap();
    await expect(elementById(TestIDs.OVERLAY_ALERT_HEADER)).toBeVisible();
    await elementById(TestIDs.DISMISS_BTN).tap();
    await expect(elementById(TestIDs.OVERLAY_ALERT_HEADER)).toBeNotVisible();
  });

  it('overlay pass touches - true', async () => {
    await elementById(TestIDs.SHOW_TOUCH_THROUGH_OVERLAY_BTN).tap();
    await expect(elementById(TestIDs.SHOW_OVERLAY_BTN)).toBeVisible();
    await elementById(TestIDs.ALERT_BUTTON).tap();
    await expect(elementByLabel('Alert displayed')).toBeVisible();
  });

  it('overlay should redraw after orientation change', async () => {
    await elementById(TestIDs.SHOW_OVERLAY_BTN).tap();
    await device.setOrientation('landscape');
    await expect(elementById(TestIDs.OVERLAY_ALERT_HEADER)).toBeVisible();
  });

  it('setRoot should not remove overlay', async () => {
    await elementById(TestIDs.SHOW_TOUCH_THROUGH_OVERLAY_BTN).tap();
    await elementById(TestIDs.SET_ROOT_BTN).tap();
    await expect(elementById(TestIDs.OVERLAY_ALERT_HEADER)).toBeVisible();
  });

  xtest('overlay pass touches - false', async () => {
    await elementById(TestIDs.SHOW_OVERLAY_BUTTON).tap();
    await expect(elementById(TestIDs.SHOW_OVERLAY_BUTTON)).toBeVisible();
    await expect(elementById(TestIDs.TOP_BAR_ELEMENT)).toBeVisible();
    await elementById(TestIDs.HIDE_TOP_BAR_BUTTON).tap();
    await expect(elementById(TestIDs.TOP_BAR_ELEMENT)).toBeVisible();
  });
});
