"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ColorService = void 0;
const react_native_1 = require("react-native");
class ColorService {
    toNativeColor(inputColor) {
        return (0, react_native_1.processColor)(inputColor);
    }
}
exports.ColorService = ColorService;
