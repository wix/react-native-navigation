const Utils = require('./Utils');
const elementByLabel = Utils.elementByLabel;

describe('orientation', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('orientation change from contained view controller', async () => {
    await elementByLabel('Push Options Screen').tap();
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
  });

  it('orientation should not change from portrait', async () => {
    await elementByLabel('Push').tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
  });

  it('orientation change in modal view', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');
  });

  it('orientation should not change in modal', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Landscape');

    await elementByLabel('Show Modal').tap();
    await device.setOrientation('landscape');
    await expect(element(by.id('currentOrientation'))).toHaveText('Portrait');
  });
});
