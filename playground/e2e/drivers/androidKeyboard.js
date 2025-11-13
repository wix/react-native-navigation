import { device } from 'detox';
import Utils from '../Utils';

const { retry, sleep } = Utils;

const androidKbdDriver = {
  async init() {
    if (device.getPlatform() !== 'android') {
      return;
    }

    await this._initADB();
    await this._initKbdState();
  },

  async enableOnScreenKeyboard() {
    if (!this.adb) {
      // Not initialized (iOS?)
      return;
    }
    await this._setOnscreenKeyboard(true);
  },

  async restoreOnScreenKeyboard() {
    if (!this.adb) {
      // Not initialized (iOS?)
      return;
    }
    await this._setOnscreenKeyboard(this.kbdEnabled);
  },

  _initADB() {
    const { id: adbName } = device;
    const { adb } = device.deviceDriver;

    if (!adb || !adbName) {
      throw new Error(`Keyboard driver init failed (id=${adbName}, hasADB=${!!adb})`);
    }

    this.adb = adb;
    this.adbName = adbName;
  },

  async _initKbdState() {
    this.kbdEnabled = await this.adb.shell(this.adbName, 'settings get Secure show_ime_with_hard_keyboard');

    if (!(this.kbdEnabled === '0' || this.kbdEnabled === '1')) {
      console.warn('[KbdDriver] Unable to get on-screen KBD setting, defaulting to false');
      this.kbdEnabled = '0';
    }
  },

  async _setOnscreenKeyboard(_value) {
    const value = (!!Number(_value) ? '1' : '0');

    await retry( { tries: 10 }, async () => {
      await this.adb.shell(this.adbName, `settings put Secure show_ime_with_hard_keyboard ${value}`);
      await sleep(1000);

      const result = await this.adb.shell(this.adbName, 'settings get Secure show_ime_with_hard_keyboard');
      return result === value;
    });
  }
}

module.exports = androidKbdDriver;
