import { NativeModules } from 'react-native';

export interface NavigationConstants {
  statusBarHeight: number;
  backButtonId: string;
  nonceString: string;
  topBarHeight: number;
  bottomTabsHeight: number;
}

export class Constants {
  static async get(): Promise<NavigationConstants> {
    const constants: NavigationConstants = await NativeModules.RNNBridgeModule.getNavigationConstants();
    return new Constants(constants);
  }

  public readonly statusBarHeight: number;
  public readonly backButtonId: string;
  public readonly topBarHeight: number;
  public readonly nonceString: string;
  public readonly bottomTabsHeight: number;

  private constructor(constants: NavigationConstants) {
    this.statusBarHeight = constants.statusBarHeight;
    this.topBarHeight = constants.topBarHeight;
    this.nonceString = constants.nonceString;
    this.backButtonId = constants.backButtonId;
    this.bottomTabsHeight = constants.bottomTabsHeight;
  }
}
