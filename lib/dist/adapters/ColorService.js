"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
class ColorService {
    toNativeColor(inputColor) {
        return react_native_1.processColor(inputColor);
    }
}
exports.ColorService = ColorService;
