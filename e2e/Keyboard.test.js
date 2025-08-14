import { default as TestIDs, default as testIDs } from '../playground/src/testIDs';
import { device } from 'detox';
import Utils from './Utils';

const { elementByLabel, elementById } = Utils;

const KBD_OBSCURED_TEXT = 'Keyboard Demo';

const enableOnScreenKeyboard = async (adb, adbName) =>
  adb.shell(adbName, 'settings put Secure show_ime_with_hard_keyboard 1');

const disableOnScreenKeyboard = async (adb, adbName) =>
  adb.shell(adbName, 'settings put Secure show_ime_with_hard_keyboard 0');

const driver = {
  async init() {
    const { id: adbName } = device;
    const { adb } = device.deviceDriver;

    if (!adb || !adbName) {
      throw new Error(`Keyboard driver init failed (id=${adbName}, hasADB=${!!adb})`);
    }

    this.adb = adb;
    this.adbName = adbName;
    this.kbdEnabled = await adb.shell(adbName, 'settings get Secure show_ime_with_hard_keyboard');

    if (!(this.kbdEnabled === '0') || !(this.kbdEnabled === '1')) {
      console.warn('[KeyboardDriver] Unable to get on-screen KBD setting, defaulting to false');
      this.kbdEnabled = '0'
    }
  },

  async enableOnScreenKeyboard() {
    await this.adb.shell(this.adbName, 'settings put Secure show_ime_with_hard_keyboard 1');
  },
  async restoreOnScreenKeyboard() {
    if (!this.adb) {
      // Not initialized
      return;
    }

    await this.adb.shell(this.adbName, `settings put Secure show_ime_with_hard_keyboard ${this.kbdEnabled}`);
  },
}

describe.e2e('Keyboard', () => {
  beforeAll(async () => {
    await driver.init();
    await driver.enableOnScreenKeyboard();
  });

  afterAll(async () => {
    await driver.restoreOnScreenKeyboard();
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
