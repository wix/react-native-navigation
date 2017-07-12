const Utils = require('./Utils');
const elementByLabel = Utils.elementByLabel;

describe('screen style - dynamic', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('change title on container component', async () => {
    await elementByLabel('Push Options Screen').tap();
    await expect(element(by.label('Static Title').and(by.type('UILabel')))).toBeVisible();
    await elementByLabel('Dynamic Options').tap();
    await expect(element(by.label('Dynamic Title').and(by.type('UILabel')))).toBeVisible();
  });
});
