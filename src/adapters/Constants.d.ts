import { NativeCommandsSender } from './NativeCommandsSender';
export interface NavigationConstants {
    statusBarHeight: number;
    backButtonId: string;
    topBarHeight: number;
    bottomTabsHeight: number;
}
export declare class Constants {
    static get(nativeCommandSender: NativeCommandsSender): Promise<NavigationConstants>;
    static getSync(nativeCommandSender: NativeCommandsSender): NavigationConstants;
    readonly statusBarHeight: number;
    readonly backButtonId: string;
    readonly topBarHeight: number;
    readonly bottomTabsHeight: number;
    private constructor();
}
