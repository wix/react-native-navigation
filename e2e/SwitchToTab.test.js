const Utils = require('./Utils');
const elementByLabel = Utils.elementByLabel;

describe('switch to tab', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('switchToTab', async () => {
    await elementByLabel('Switch to tab based app').tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await elementByLabel('Switch Tab').tap();
    await expect(elementByLabel('This is tab 2')).toBeVisible();
  });

  it('switchToTab in deep stack', async () => {
    await elementByLabel('Switch to tab based app').tap();
    await expect(elementByLabel('This is tab 1')).toBeVisible();
    await elementByLabel('Push').tap();
    await elementByLabel('Switch Tab').tap();
    await expect(elementByLabel('This is tab 2')).toBeVisible();
  });
});
