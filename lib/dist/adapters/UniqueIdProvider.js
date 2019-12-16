"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const uniqueId_1 = tslib_1.__importDefault(require("lodash/uniqueId"));
class UniqueIdProvider {
    generate(prefix) {
        return uniqueId_1.default(prefix);
    }
}
exports.UniqueIdProvider = UniqueIdProvider;
