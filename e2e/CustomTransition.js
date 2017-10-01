
const Utils = require('./Utils');

const elementByLabel = Utils.elementByLabel;

describe('custom transition', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('sanity', async () => {
    await elementByLabel('Push Options Screen').tap();
    await elementByLabel('Custom Transition').tap();
    await expect(element(by.id('shared_image1'))).toExist();
    await element(by.id('shared_image1')).tap();
    await expect(element(by.id('shared_image2'))).toExist();
    await element(by.id('shared_image2')).tap();
    await expect(element(by.id('shared_image1'))).toExist();
  });
});
