describe('screen stack', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('push and pop screen', async () => {
    await elementByLabel('Push').tap();
    await expect(elementByLabel('Pushed Screen')).toBeVisible();
    await elementByLabel('Pop').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('pop screen deep in the stack', async () => {
    await elementByLabel('Push').tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
    await elementByLabel('Push').tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementByLabel('Pop Previous').tap();
    await expect(elementByLabel('Stack Position: 2')).toBeVisible();
    await elementByLabel('Pop').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('pop to specific id', async () => {
    await elementByLabel('Push').tap();
    await elementByLabel('Push').tap();
    await elementByLabel('Push').tap();
    await expect(elementByLabel('Stack Position: 3')).toBeVisible();
    await elementByLabel('Pop To Stack Position 1').tap();
    await expect(elementByLabel('Stack Position: 1')).toBeVisible();
  });

  it('pop to root', async () => {
    await elementByLabel('Push').tap();
    await elementByLabel('Push').tap();
    await elementByLabel('Push').tap();
    await elementByLabel('Pop To Root').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });
});

function elementByLabel(label) {
  return element(by.label(label));
}
