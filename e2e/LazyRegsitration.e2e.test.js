import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementById } = Utils;

describe.e2e('Lazy Registration', () => {
  beforeEach(async () => {
    await device.relaunchApp();
    await elementById(TestIDs.STACK_BTN).tap();
  });

  it('push and pop lazily registered screen', async () => {
    await elementById(TestIDs.PUSH_LAZY_BTN).tap();
    await expect(elementById(TestIDs.LAZILY_REGISTERED_SCREEN_HEADER)).toBeVisible();
    await expect(elementById(TestIDs.LAZY_TOP_PAR)).toBeVisible();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementById(TestIDs.STACK_SCREEN_HEADER)).toBeVisible();
  });
});
