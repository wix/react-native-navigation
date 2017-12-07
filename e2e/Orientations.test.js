
const Utils = require('./Utils');

const elementByLabel = Utils.elementByLabel;

describe('orientation', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  afterEach(async () => {
    await device.setOrientation('landscape');
    await device.setOrientation('portrait');
  });

  it('default allows all', async () => {
    await elementByLabel('Orientation').tap();
    await elementByLabel('default').tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
    await device.setOrientation('portrait');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await elementByLabel('Dismiss').tap();
  });

  it('landscape and portrait array', async () => {
    await elementByLabel('Orientation').tap();
    await elementByLabel('landscape and portrait').tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
    await device.setOrientation('portrait');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await elementByLabel('Dismiss').tap();
  });

  it('portrait only', async () => {
    await elementByLabel('Orientation').tap();
    await elementByLabel('portrait only').tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('portrait');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await elementByLabel('Dismiss').tap();
  });

  it('landscape only', async () => {
    await elementByLabel('Orientation').tap();
    await elementByLabel('landscape only').tap();
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
    await device.setOrientation('portrait');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
    await elementByLabel('Dismiss').tap();
  });
});
