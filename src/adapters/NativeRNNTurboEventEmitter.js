"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
let eventEmitter;
eventEmitter = react_native_1.TurboModuleRegistry.get('RNNTurboEventEmitter');
exports.default = eventEmitter;
