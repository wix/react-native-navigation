"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
class Constants {
    static async get() {
        if (!this.instance) {
            const constants = await react_native_1.NativeModules.RNNBridgeModule.getConstants();
            this.instance = new Constants(constants);
        }
        return this.instance;
    }
    constructor(constants) {
        this.statusBarHeight = constants.statusBarHeight;
        this.topBarHeight = constants.topBarHeight;
        this.backButtonId = constants.backButtonId;
        this.bottomTabsHeight = constants.bottomTabsHeight;
    }
}
exports.Constants = Constants;
