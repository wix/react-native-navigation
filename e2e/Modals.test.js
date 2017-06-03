describe('modal', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('show modal', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Screen')).toBeVisible();
  });

  it('dismiss modal', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Screen')).toBeVisible();
    await elementByLabel('Dismiss Modal').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('show multiple modals', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementByLabel('Dismiss Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementByLabel('Dismiss Modal').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('dismiss unknown screen id', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementByLabel('Dismiss Unknown Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementByLabel('Dismiss Modal').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('dismiss modal by id which is not the top most', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementByLabel('Dismiss Previous Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementByLabel('Dismiss Modal').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('dismiss all previous modals by id when they are below top presented modal', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementByLabel('Dismiss ALL Previous Modals').tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementByLabel('Dismiss Modal').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('dismiss some modal by id deep in the stack', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementByLabel('Dismiss First In Stack').tap();
    await expect(elementByLabel('Modal Stack Position: 3')).toBeVisible();

    await elementByLabel('Dismiss Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();

    await elementByLabel('Dismiss Modal').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });

  it('dismissAllModals', async () => {
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 1')).toBeVisible();
    await elementByLabel('Show Modal').tap();
    await expect(elementByLabel('Modal Stack Position: 2')).toBeVisible();
    await elementByLabel('Dismiss All Modals').tap();
    await expect(elementByLabel('React Native Navigation!')).toBeVisible();
  });
});

function elementByLabel(label) {
  return element(by.label(label));
}
