"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const _ = require("lodash");
class OptionsProcessor {
    constructor(store, uniqueIdProvider, colorService, assetService) {
        this.store = store;
        this.uniqueIdProvider = uniqueIdProvider;
        this.colorService = colorService;
        this.assetService = assetService;
    }
    processOptions(options) {
        this.processObject(options);
    }
    processObject(objectToProcess) {
        _.forEach(objectToProcess, (value, key) => {
            if (!value) {
                return;
            }
            this.processComponent(key, value, objectToProcess);
            this.processColor(key, value, objectToProcess);
            this.processImage(key, value, objectToProcess);
            this.processButtonsPassProps(key, value);
            if (!_.isEqual(key, 'passProps') && (_.isObject(value) || _.isArray(value))) {
                this.processObject(value);
            }
        });
    }
    processColor(key, value, options) {
        if (_.isEqual(key, 'color') || _.endsWith(key, 'Color')) {
            options[key] = this.colorService.toNativeColor(value);
        }
    }
    processImage(key, value, options) {
        if (_.isEqual(key, 'icon') ||
            _.isEqual(key, 'image') ||
            _.endsWith(key, 'Icon') ||
            _.endsWith(key, 'Image')) {
            options[key] = this.assetService.resolveFromRequire(value);
        }
    }
    processButtonsPassProps(key, value) {
        if (_.endsWith(key, 'Buttons')) {
            _.forEach(value, (button) => {
                if (button.passProps && button.id) {
                    this.store.setPropsForId(button.id, button.passProps);
                    button.passProps = undefined;
                }
            });
        }
    }
    processComponent(key, value, options) {
        if (_.isEqual(key, 'component')) {
            value.componentId = value.id ? value.id : this.uniqueIdProvider.generate('CustomComponent');
            if (value.passProps) {
                this.store.setPropsForId(value.componentId, value.passProps);
            }
            options[key].passProps = undefined;
        }
    }
}
exports.OptionsProcessor = OptionsProcessor;
