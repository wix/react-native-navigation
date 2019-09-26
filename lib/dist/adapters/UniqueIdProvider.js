"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const _ = require("lodash");
class UniqueIdProvider {
    generate(prefix) {
        return _.uniqueId(prefix);
    }
}
exports.UniqueIdProvider = UniqueIdProvider;
