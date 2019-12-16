"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
class AssetService {
    resolveFromRequire(value) {
        return react_native_1.Image.resolveAssetSource(value);
    }
}
exports.AssetService = AssetService;
