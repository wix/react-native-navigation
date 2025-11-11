"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.mockNativeComponents = exports.ApplicationMock = void 0;
const tslib_1 = require("tslib");
exports.ApplicationMock = require('./Application').Application;
tslib_1.__exportStar(require("./constants"), exports);
function mockNativeComponents() {
    const { NativeCommandsSender } = require('./mocks/NativeCommandsSender');
    const { NativeEventsReceiver } = require('./mocks/NativeEventsReceiver');
    const { AppRegistryService } = require('./mocks/AppRegistryService');
    const { Navigation } = require('react-native-navigation');
    Navigation.mockNativeComponents(new NativeCommandsSender(), new NativeEventsReceiver(), new AppRegistryService());
}
exports.mockNativeComponents = mockNativeComponents;
