import { default as TestIDs, default as testIDs } from '../playground/src/testIDs';
import Android from './AndroidUtils';
import Utils from './Utils';

const { elementByLabel, elementById } = Utils;

describe('Keyboard', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.KEYBOARD_SCREEN_BTN).tap();
  });

  it('Push - should close keyboard when Back clicked', async () => {
    await elementById(TestIDs.TEXT_INPUT1).tap();
    await expect(elementByLabel("Keyboard Demo")).not.toBeVisible();
    await elementById(TestIDs.BACK_BUTTON).tap();
    await expect(elementById(testIDs.MAIN_BOTTOM_TABS)).toBeVisible();
  });

  it('Modal - should close keyboard when close clicked', async () => {
    await elementById(TestIDs.MODAL_BTN).tap();
    await elementById(TestIDs.TEXT_INPUT1).tap();
    await expect(elementByLabel("Keyboard Demo")).not.toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_TOPBAR_BTN).tap();
    await expect(elementById(testIDs.MAIN_BOTTOM_TABS)).toBeVisible();
  });

});
