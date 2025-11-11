"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Constants = void 0;
class Constants {
    static async get(nativeCommandSender) {
        const constants = await nativeCommandSender.getNavigationConstants();
        return new Constants(constants);
    }
    static getSync(nativeCommandSender) {
        const constants = nativeCommandSender.getNavigationConstantsSync();
        return new Constants(constants);
    }
    statusBarHeight;
    backButtonId;
    topBarHeight;
    bottomTabsHeight;
    constructor(constants) {
        this.statusBarHeight = constants.statusBarHeight;
        this.topBarHeight = constants.topBarHeight;
        this.backButtonId = constants.backButtonId;
        this.bottomTabsHeight = constants.bottomTabsHeight;
    }
}
exports.Constants = Constants;
