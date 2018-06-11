const Utils = require('./Utils');
const testIDs = require('../playground/src/testIDs');

const { elementByLabel, elementById } = Utils;

describe('complex layout', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  test(':ios: shows external component in stack in modal', async () => {
    await elementById(testIDs.COMPLEX_LAYOUT_BUTTON).tap();
    await elementById(testIDs.EXTERNAL_COMPONENT_IN_STACK).tap();
    await expect(elementByLabel('External component in stack')).toBeVisible();
  });

  test(':ios: shows external component in deep stack in modal', async () => {
    await elementById(testIDs.COMPLEX_LAYOUT_BUTTON).tap();
    await elementById(testIDs.EXTERNAL_COMPONENT_IN_DEEP_STACK).tap();
    await expect(elementByLabel('External component in deep stack')).toBeVisible();
  });
});
