import { default as TestIDs, default as testIDs } from '../src/testIDs';
import { device } from 'detox';
import Utils from './Utils';
import kbdDriver from './drivers/androidKeyboard';

const { elementByLabel, elementById, sleep } = Utils;

const KBD_OBSCURED_TEXT = 'Keyboard Demo';

describe.e2e('Keyboard', () => {
  beforeAll(async () => {
    await kbdDriver.init();
    await kbdDriver.enableOnScreenKeyboard();

    if (device.getPlatform() === 'android') {
      // 1st-time Android keyboard appearance is laggy (Android's lazy init?)
      await device.launchApp({ newInstance: true });
      await elementById(TestIDs.KEYBOARD_SCREEN_BTN).tap();
      await elementById(TestIDs.TEXT_INPUT1).tap();
      await sleep(2000);
    }
  });

  afterAll(async () => {
    await kbdDriver.restoreOnScreenKeyboard();
  });

  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.KEYBOARD_SCREEN_BTN).tap();
  });

  it('Push - should close keyboard when Back clicked', async () => {
    await expect(elementByLabel(KBD_OBSCURED_TEXT)).toBeVisible();
    await elementById(TestIDs.TEXT_INPUT1).tap();
    await expect(elementByLabel(KBD_OBSCURED_TEXT)).not.toBeVisible();
    await elementById(TestIDs.BACK_BUTTON).tap();
    await expect(elementById(testIDs.MAIN_BOTTOM_TABS)).toBeVisible();
  });

  it('Modal - should close keyboard when close clicked', async () => {
    await elementById(TestIDs.MODAL_BTN).tap();
    await elementById(TestIDs.TEXT_INPUT1).tap();
    await expect(elementByLabel(KBD_OBSCURED_TEXT)).not.toBeVisible();
    await elementById(TestIDs.DISMISS_MODAL_TOPBAR_BTN).tap();
    await expect(elementById(testIDs.MAIN_BOTTOM_TABS)).toBeVisible();
  });

  it('focus keyboard continue to resize content', async () => {
    await elementById(TestIDs.TEXT_INPUT2).typeText("Hello");
    await elementById(TestIDs.TEXT_INPUT2).tapReturnKey();
    await expect(elementById(TestIDs.TEXT_INPUT1)).toBeFocused();
    await expect(elementById(TestIDs.TEXT_INPUT1)).toBeVisible();
  });

  it('focus keyboard on push', async () => {
    await elementById(TestIDs.PUSH_FOCUSED_KEYBOARD_SCREEN).tap();
    await expect(elementById(TestIDs.TEXT_INPUT1)).toBeFocused();
  });

  it('focus keyboard on show modal', async () => {
    await elementById(TestIDs.MODAL_FOCUSED_KEYBOARD_SCREEN).tap();
    await expect(elementById(TestIDs.TEXT_INPUT1)).toBeFocused();
  });

  it('doesnt focus keyboard on show modal', async () => {
    await elementById(TestIDs.MODAL_BTN).tap();
    await expect(elementById(TestIDs.TEXT_INPUT1)).not.toBeFocused();
  });

  it(':android: should respect UI with keyboard awareness', async () => {
    await elementById(TestIDs.PUSH_KEYBOARD_SCREEN_STICKY_FOOTER).tap();
    await elementById(TestIDs.TEXT_INPUT2).tap();
    await expect(elementByLabel(KBD_OBSCURED_TEXT)).toBeVisible();
  });
});
