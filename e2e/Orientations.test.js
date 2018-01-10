
const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const { elementById } = Utils;

describe('orientation', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  afterEach(async () => {
    await device.setOrientation('landscape');
    await device.setOrientation('portrait');
  });

  it('default allows all', async () => {
    await elementById(testIDs.ORIENTATION_BUTTON).tap();
    await elementById(testIDs.DEFAULT_ORIENTATION_BUTTON).tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
    await device.setOrientation('portrait');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await elementById(testIDs.DISMISS_BUTTON).tap();
  });

  it('landscape and portrait array', async () => {
    await elementById(testIDs.ORIENTATION_BUTTON).tap();
    await elementById(testIDs.LANDSCAPE_PORTRAIT_ORIENTATION_BUTTON).tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
    await device.setOrientation('portrait');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await elementById(testIDs.DISMISS_BUTTON).tap();
  });

  it('portrait only', async () => {
    await elementById(testIDs.ORIENTATION_BUTTON).tap();
    await elementById(testIDs.PORTRAIT_ORIENTATION_BUTTON).tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('portrait');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await elementById(testIDs.DISMISS_BUTTON).tap();
  });

  it('landscape only', async () => {
    await elementById(testIDs.ORIENTATION_BUTTON).tap();
    await elementById(testIDs.LANDSCAPE_ORIENTATION_BUTTON).tap();
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
    await device.setOrientation('portrait');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
    await elementById(testIDs.DISMISS_BUTTON).tap();
  });
});
