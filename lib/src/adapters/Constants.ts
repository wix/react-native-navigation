import { NativeModules } from 'react-native';

export interface NavigationConstants {
  statusBarHeight: number;
  backButtonId: string;
  topBarHeight: number;
  bottomTabsHeight: number;
}

export class Constants {
  static async get(): Promise<NavigationConstants> {
    if (!this.instance) {
      const constants: NavigationConstants = await NativeModules.RNNBridgeModule.getConstants();
      this.instance = new Constants(constants);
    }
    return this.instance;
  }

  private static instance: Constants;

  public readonly statusBarHeight: number;
  public readonly backButtonId: string;
  public readonly topBarHeight: number;
  public readonly bottomTabsHeight: number;

  private constructor(constants: NavigationConstants) {
    this.statusBarHeight = constants.statusBarHeight;
    this.topBarHeight = constants.topBarHeight;
    this.backButtonId = constants.backButtonId;
    this.bottomTabsHeight = constants.bottomTabsHeight;
  }
}
