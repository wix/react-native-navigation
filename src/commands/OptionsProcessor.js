"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.OptionsProcessor = void 0;
const tslib_1 = require("tslib");
const clone_1 = tslib_1.__importDefault(require("lodash/clone"));
const isEqual_1 = tslib_1.__importDefault(require("lodash/isEqual"));
const isObject_1 = tslib_1.__importDefault(require("lodash/isObject"));
const isArray_1 = tslib_1.__importDefault(require("lodash/isArray"));
const isString_1 = tslib_1.__importDefault(require("lodash/isString"));
const endsWith_1 = tslib_1.__importDefault(require("lodash/endsWith"));
const forEach_1 = tslib_1.__importDefault(require("lodash/forEach"));
const has_1 = tslib_1.__importDefault(require("lodash/has"));
const react_native_1 = require("react-native");
class OptionsProcessor {
    store;
    uniqueIdProvider;
    optionProcessorsRegistry;
    colorService;
    assetService;
    deprecations;
    constructor(store, uniqueIdProvider, optionProcessorsRegistry, colorService, assetService, deprecations) {
        this.store = store;
        this.uniqueIdProvider = uniqueIdProvider;
        this.optionProcessorsRegistry = optionProcessorsRegistry;
        this.colorService = colorService;
        this.assetService = assetService;
        this.deprecations = deprecations;
    }
    processOptions(commandName, options, props) {
        if (options) {
            this.processObject(options, (0, clone_1.default)(options), (key, parentOptions) => {
                this.deprecations.onProcessOptions(key, parentOptions, commandName);
                this.deprecations.checkForDeprecatedOptions(parentOptions);
            }, commandName, props);
        }
    }
    processDefaultOptions(options, commandName) {
        this.processObject(options, (0, clone_1.default)(options), (key, parentOptions) => {
            this.deprecations.onProcessDefaultOptions(key, parentOptions);
        }, commandName);
    }
    processObject(objectToProcess, parentOptions, onProcess, commandName, props, parentPath) {
        (0, forEach_1.default)(objectToProcess, (value, key) => {
            const objectPath = this.resolveObjectPath(key, parentPath);
            this.processWithRegisteredProcessor(key, value, objectToProcess, objectPath, commandName, props);
            this.processColor(key, value, objectToProcess);
            if (!value) {
                return;
            }
            this.processComponent(key, value, objectToProcess);
            this.processImage(key, value, objectToProcess);
            this.processButtonsPassProps(key, value);
            this.processSearchBar(key, value, objectToProcess);
            this.processInterpolation(key, value, objectToProcess);
            this.processAnimation(key, value, objectToProcess);
            onProcess(key, parentOptions);
            const processedValue = objectToProcess[key];
            if (!(0, isEqual_1.default)(key, 'passProps') && ((0, isObject_1.default)(processedValue) || (0, isArray_1.default)(processedValue))) {
                this.processObject(processedValue, parentOptions, onProcess, commandName, props, objectPath);
            }
        });
    }
    resolveObjectPath(key, path) {
        if (!path)
            path = key;
        else
            path += `.${key}`;
        return path;
    }
    processColor(key, value, options) {
        if (((0, isEqual_1.default)(key, 'color') || (0, endsWith_1.default)(key, 'Color')) && !(0, isEqual_1.default)(key, 'bkgColor')) {
            if (react_native_1.Platform.OS === 'ios')
                this.processColorIOS(key, value, options);
            else
                this.processColorAndroid(key, value, options);
        }
    }
    processColorIOS(key, value, options) {
        if (value !== undefined) {
            if (value === null) {
                options[key] = 'NoColor';
            }
            else if (value instanceof Object) {
                if ('semantic' in value) {
                    options[key] = value;
                }
                else if ('dynamic' in value) {
                    options[key] = (0, react_native_1.DynamicColorIOS)({
                        light: this.colorService.toNativeColor(value.dynamic.light),
                        dark: this.colorService.toNativeColor(value.dynamic.dark),
                    });
                }
                else {
                    options[key] = (0, react_native_1.DynamicColorIOS)({
                        light: this.colorService.toNativeColor(value.light),
                        dark: this.colorService.toNativeColor(value.dark),
                    });
                }
            }
            else {
                options[key] = this.colorService.toNativeColor(value);
            }
        }
    }
    processColorAndroid(key, value, options) {
        if (value !== undefined) {
            const newColorObj = { dark: 'NoColor', light: 'NoColor' };
            if (value === null) {
                options[key] = newColorObj;
            }
            else if (value instanceof Object) {
                if ('semantic' in value || 'resource_paths' in value) {
                    options[key] = value;
                    return;
                }
                else {
                    for (let keyColor in value) {
                        newColorObj[keyColor] = this.colorService.toNativeColor(value[keyColor]);
                    }
                    options[key] = newColorObj;
                }
            }
            else {
                let parsedColor = this.colorService.toNativeColor(value);
                newColorObj.light = parsedColor;
                newColorObj.dark = parsedColor;
                options[key] = newColorObj;
            }
        }
    }
    processWithRegisteredProcessor(key, value, options, path, commandName, passProps) {
        const registeredProcessors = this.optionProcessorsRegistry.getProcessors(path);
        if (registeredProcessors) {
            registeredProcessors.forEach((processor) => {
                options[key] = processor(value, commandName, passProps);
            });
        }
    }
    processImage(key, value, options) {
        if ((0, isEqual_1.default)(key, 'icon') ||
            (0, isEqual_1.default)(key, 'image') ||
            (0, endsWith_1.default)(key, 'Icon') ||
            (0, endsWith_1.default)(key, 'Image')) {
            options[key] = (0, isString_1.default)(value) ? value : this.assetService.resolveFromRequire(value);
        }
    }
    processButtonsPassProps(key, value) {
        if ((0, endsWith_1.default)(key, 'Buttons')) {
            (0, forEach_1.default)(value, (button) => {
                if (button.passProps && button.id) {
                    this.store.setPendingProps(button.id, button.passProps);
                    button.passProps = undefined;
                }
            });
        }
    }
    processComponent(key, value, options) {
        if ((0, isEqual_1.default)(key, 'component')) {
            value.componentId = value.id ? value.id : this.uniqueIdProvider.generate('CustomComponent');
            this.store.ensureClassForName(value.name);
            if (value.passProps) {
                this.store.setPendingProps(value.componentId, value.passProps);
            }
            options[key].passProps = undefined;
        }
    }
    processSearchBar(key, value, options) {
        if (key !== 'searchBar') {
            return;
        }
        const deprecatedSearchBarOptions = {
            visible: false,
            hideOnScroll: options.searchBarHiddenWhenScrolling ?? false,
            hideTopBarOnFocus: options.hideNavBarOnFocusSearchBar ?? false,
            obscuresBackgroundDuringPresentation: false,
            backgroundColor: options.searchBarBackgroundColor,
            tintColor: options.searchBarTintColor,
            placeholder: options.searchBarPlaceholder ?? '',
        };
        if (typeof value === 'boolean') {
            // Deprecated
            this.deprecations.onProcessOptions(key, options, '');
            options[key] = {
                ...deprecatedSearchBarOptions,
                visible: value,
            };
        }
        else {
            options[key] = {
                ...deprecatedSearchBarOptions,
                ...value,
            };
        }
    }
    processInterpolation(key, value, options) {
        if ((0, isEqual_1.default)(key, 'interpolation')) {
            if (typeof value === 'string') {
                this.deprecations.onProcessOptions(key, options, '');
                options[key] = {
                    type: options[key],
                };
            }
        }
    }
    processAnimation(key, value, options) {
        this.processSetRootAnimation(key, value, options);
        this.processPush(key, value, options);
        this.processPop(key, value, options);
        this.processSetStackRoot(key, value, options);
        this.processShowModal(key, value, options);
        this.processDismissModal(key, value, options);
    }
    processSetStackRoot(key, animation, parentOptions) {
        if (key !== 'setStackRoot')
            return;
        if (this.isNewStackAnimationApi(animation))
            return;
        this.convertDeprecatedViewAnimationApiToNewStackAnimationApi(animation, parentOptions);
    }
    isNewStackAnimationApi(animation) {
        return (0, has_1.default)(animation, 'content') || (0, has_1.default)(animation, 'topBar') || (0, has_1.default)(animation, 'bottomTabs');
    }
    convertDeprecatedViewAnimationApiToNewStackAnimationApi(animation, parentOptions) {
        if (!(0, has_1.default)(animation, 'content.enter') && !(0, has_1.default)(animation, 'content.exit')) {
            parentOptions.setStackRoot = {
                content: {
                    enter: animation,
                },
            };
            if ((0, has_1.default)(animation, 'enabled')) {
                parentOptions.setStackRoot.enabled = animation.enabled;
            }
            if ((0, has_1.default)(animation, 'waitForRender')) {
                parentOptions.setStackRoot.waitForRender = animation.waitForRender;
            }
        }
    }
    processPop(key, animation, parentOptions) {
        if (key !== 'pop')
            return;
        if (animation.content && !(0, has_1.default)(animation, 'content.enter') && !(0, has_1.default)(animation, 'content.exit')) {
            parentOptions.pop.content = {
                exit: animation.content,
            };
        }
        if (animation.topBar && !(0, has_1.default)(animation, 'topBar.enter') && !(0, has_1.default)(animation, 'topBar.exit')) {
            parentOptions.pop.topBar = {
                exit: animation.topBar,
            };
        }
        if (animation.bottomTabs &&
            !(0, has_1.default)(animation, 'bottomTabs.enter') &&
            !(0, has_1.default)(animation, 'bottomTabs.exit')) {
            parentOptions.pop.bottomTabs = {
                exit: animation.bottomTabs,
            };
        }
    }
    processSetRootAnimation(key, animation, parentOptions) {
        if (key !== 'setRoot')
            return;
        if (react_native_1.Platform.OS === 'android' && !('enter' in animation)) {
            parentOptions.setRoot = {
                enter: animation,
            };
        }
        else if (react_native_1.Platform.OS === 'ios' && 'enter' in animation) {
            parentOptions.setRoot = animation;
        }
    }
    processShowModal(key, animation, parentOptions) {
        if (key !== 'showModal')
            return;
        if (!('enter' in animation)) {
            const elementTransitions = animation.elementTransitions;
            const sharedElementTransitions = animation.sharedElementTransitions;
            const enter = { ...animation };
            delete enter.sharedElementTransitions;
            delete enter.elementTransitions;
            parentOptions.showModal = {
                enter,
                sharedElementTransitions,
                elementTransitions,
            };
        }
    }
    processDismissModal(key, animation, parentOptions) {
        if (key !== 'dismissModal')
            return;
        if (!('exit' in animation)) {
            const elementTransitions = animation.elementTransitions;
            const sharedElementTransitions = animation.sharedElementTransitions;
            const exit = { ...animation };
            delete exit.sharedElementTransitions;
            delete exit.elementTransitions;
            parentOptions.dismissModal = {
                exit,
                sharedElementTransitions,
                elementTransitions,
            };
        }
    }
    processPush(key, animation, parentOptions) {
        if (key !== 'push')
            return;
        if (animation.content && !(0, has_1.default)(animation, 'content.enter') && !(0, has_1.default)(animation, 'content.exit')) {
            parentOptions.push.content = {
                enter: animation.content,
            };
        }
        if (animation.topBar && !(0, has_1.default)(animation, 'topBar.enter') && !(0, has_1.default)(animation, 'topBar.exit')) {
            parentOptions.push.topBar = {
                enter: animation.topBar,
            };
        }
        if (animation.statusBar &&
            !(0, has_1.default)(animation, 'statusBar.enter') &&
            !(0, has_1.default)(animation, 'statusBar.exit')) {
            parentOptions.push.statusBar = {
                enter: animation.statusBar,
            };
        }
        if (animation.bottomTabs &&
            !(0, has_1.default)(animation, 'bottomTabs.enter') &&
            !(0, has_1.default)(animation, 'bottomTabs.exit')) {
            parentOptions.push.bottomTabs = {
                enter: animation.bottomTabs,
            };
        }
    }
}
exports.OptionsProcessor = OptionsProcessor;
