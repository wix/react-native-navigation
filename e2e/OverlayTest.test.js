const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');
const { elementByLabel, elementById } = Utils;

describe('Overlay', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('show and dismiss overlay', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.SHOW_OVERLAY_BUTTON).tap();
    await expect(elementById(testIDs.DIALOG_HEADER)).toBeVisible();
    await elementById(testIDs.OK_BUTTON).tap();
    await expect(elementById(testIDs.DIALOG_HEADER)).toBeNotVisible();
  });

  it('overlay pass touches - true', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.SHOW_TOUCH_THROUGH_OVERLAY_BUTTON).tap();
    await expect(elementById(testIDs.DIALOG_HEADER)).toBeVisible();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
    await elementById(testIDs.HIDE_TOP_BAR_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeNotVisible();
  });

  it('overlay pass touches - false', async () => {
    await elementById(testIDs.PUSH_OPTIONS_BUTTON).tap();
    await elementById(testIDs.SHOW_OVERLAY_BUTTON).tap();
    await expect(elementById(testIDs.DIALOG_HEADER)).toBeVisible();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
    await elementById(testIDs.HIDE_TOP_BAR_BUTTON).tap();
    await expect(elementById(testIDs.TOP_BAR_ELEMENT)).toBeVisible();
  });
});
