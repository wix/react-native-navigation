const Utils = require('./Utils');
const TestIDs = require('../playground/src/testIDs');
const { elementByLabel, elementById, sleep } = Utils;
const Android = require('./AndroidUtils');

describe('Lazy Registration', () => {
  beforeEach(async () => {
    await device.relaunchApp();
    await elementById(TestIDs.STACK_BTN).tap();
  });

  it('push and pop lazily registered screen', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await expect(elementById(TestIDs.LAZILY_REGISTERED_SCREEN_HEADER)).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
  });

});
