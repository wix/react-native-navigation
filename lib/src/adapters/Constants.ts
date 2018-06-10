import { NativeModules } from 'react-native';

export class Constants {
  static get(): Constants {
    if (!this.instance) {
      this.instance = new Constants();
    }
    return this.instance;
  }

  private static instance: Constants;

  public readonly statusBarHeight: number;
  public readonly backButtonId: string;

  private constructor() {
    const m = NativeModules.RNNBridgeModule;
    this.statusBarHeight = m.statusBarHeight;
    this.backButtonId = m.backButtonId;
  }
}
