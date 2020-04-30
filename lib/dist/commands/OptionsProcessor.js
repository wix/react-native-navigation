"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const clone_1 = tslib_1.__importDefault(require("lodash/clone"));
const isEqual_1 = tslib_1.__importDefault(require("lodash/isEqual"));
const isObject_1 = tslib_1.__importDefault(require("lodash/isObject"));
const isArray_1 = tslib_1.__importDefault(require("lodash/isArray"));
const isString_1 = tslib_1.__importDefault(require("lodash/isString"));
const endsWith_1 = tslib_1.__importDefault(require("lodash/endsWith"));
const forEach_1 = tslib_1.__importDefault(require("lodash/forEach"));
class OptionsProcessor {
    constructor(store, uniqueIdProvider, colorService, assetService, deprecations) {
        this.store = store;
        this.uniqueIdProvider = uniqueIdProvider;
        this.colorService = colorService;
        this.assetService = assetService;
        this.deprecations = deprecations;
    }
    processOptions(options) {
        this.processObject(options, clone_1.default(options), (key, parentOptions) => {
            this.deprecations.onProcessOptions(key, parentOptions);
        });
    }
    processDefaultOptions(options) {
        this.processObject(options, clone_1.default(options), (key, parentOptions) => {
            this.deprecations.onProcessDefaultOptions(key, parentOptions);
        });
    }
    processObject(objectToProcess, parentOptions, onProcess) {
        forEach_1.default(objectToProcess, (value, key) => {
            this.processColor(key, value, objectToProcess);
            if (!value) {
                return;
            }
            this.processComponent(key, value, objectToProcess);
            this.processImage(key, value, objectToProcess);
            this.processButtonsPassProps(key, value);
            onProcess(key, parentOptions);
            if (!isEqual_1.default(key, 'passProps') && (isObject_1.default(value) || isArray_1.default(value))) {
                this.processObject(value, parentOptions, onProcess);
            }
        });
    }
    processColor(key, value, options) {
        if (isEqual_1.default(key, 'color') || endsWith_1.default(key, 'Color')) {
            options[key] = value === null ? 'NoColor' : this.colorService.toNativeColor(value);
        }
    }
    processImage(key, value, options) {
        if (isEqual_1.default(key, 'icon') ||
            isEqual_1.default(key, 'image') ||
            endsWith_1.default(key, 'Icon') ||
            endsWith_1.default(key, 'Image')) {
            options[key] = isString_1.default(value) ? value : this.assetService.resolveFromRequire(value);
        }
    }
    processButtonsPassProps(key, value) {
        if (endsWith_1.default(key, 'Buttons')) {
            forEach_1.default(value, (button) => {
                if (button.passProps && button.id) {
                    this.store.updateProps(button.id, button.passProps);
                    button.passProps = undefined;
                }
            });
        }
    }
    processComponent(key, value, options) {
        if (isEqual_1.default(key, 'component')) {
            value.componentId = value.id ? value.id : this.uniqueIdProvider.generate('CustomComponent');
            if (value.passProps) {
                this.store.updateProps(value.componentId, value.passProps);
            }
            options[key].passProps = undefined;
        }
    }
}
exports.OptionsProcessor = OptionsProcessor;
