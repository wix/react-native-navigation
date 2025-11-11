"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.AppRegistryService = void 0;
const react_native_1 = require("react-native");
class AppRegistryService {
    registerComponent(appKey, getComponentFunc) {
        react_native_1.AppRegistry.registerComponent(appKey, getComponentFunc);
    }
}
exports.AppRegistryService = AppRegistryService;
