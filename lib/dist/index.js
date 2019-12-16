"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const Navigation_1 = require("./Navigation");
const navigationSingleton = new Navigation_1.NavigationRoot();
exports.Navigation = navigationSingleton;
tslib_1.__exportStar(require("./adapters/Constants"), exports);
tslib_1.__exportStar(require("./interfaces/Options"), exports);
