import { NativeCommandsSender } from './NativeCommandsSender';

export interface NavigationConstants {
  statusBarHeight: number;
  backButtonId: string;
  topBarHeight: number;
  bottomTabsHeight: number;
}

export class Constants {
  static async get(nativeCommandSender: NativeCommandsSender): Promise<NavigationConstants> {
    const constants: NavigationConstants = await nativeCommandSender.getNavigationConstants();
    return new Constants(constants);
  }

  static getSync(nativeCommandSender: NativeCommandsSender): NavigationConstants {
    const constants: NavigationConstants = nativeCommandSender.getNavigationConstantsSync();
    return new Constants(constants);
  }

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
