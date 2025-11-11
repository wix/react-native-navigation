"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Deprecations = void 0;
const tslib_1 = require("tslib");
const once_1 = tslib_1.__importDefault(require("lodash/once"));
const get_1 = tslib_1.__importDefault(require("lodash/get"));
const lodash_1 = require("lodash");
const react_native_1 = require("react-native");
class Deprecations {
    deprecatedOptions = [
        {
            key: 'topBar.searchBarHiddenWhenScrolling',
            showWarning: (0, once_1.default)((_key, parentOptions) => {
                console.warn(`${_key} is deprecated and will be removed in the next major version. For more information see https://github.com/wix/react-native-navigation/issues/6585`, parentOptions);
            }),
        },
        {
            key: 'topBar.searchBarPlaceholder',
            showWarning: (0, once_1.default)((_key, parentOptions) => {
                console.warn(`${_key} is deprecated and will be removed in the next major version. For more information see https://github.com/wix/react-native-navigation/issues/6585`, parentOptions);
            }),
        },
        {
            key: 'topBar.searchBarBackgroundColor',
            showWarning: (0, once_1.default)((_key, parentOptions) => {
                console.warn(`${_key} is deprecated and will be removed in the next major version. For more information see https://github.com/wix/react-native-navigation/issues/6585`, parentOptions);
            }),
        },
        {
            key: 'topBar.searchBarTintColor',
            showWarning: (0, once_1.default)((_key, parentOptions) => {
                console.warn(`${_key} is deprecated and will be removed in the next major version. For more information see https://github.com/wix/react-native-navigation/issues/6585`, parentOptions);
            }),
        },
        {
            key: 'topBar.hideNavBarOnFocusSearchBar',
            showWarning: (0, once_1.default)((_key, parentOptions) => {
                console.warn(`${_key} is deprecated and will be removed in the next major version. For more information see https://github.com/wix/react-native-navigation/issues/6585`, parentOptions);
            }),
        },
    ];
    checkForDeprecatedOptions(options) {
        (0, lodash_1.each)(this.deprecatedOptions, (option) => {
            if ((0, get_1.default)(options, option.key, null)) {
                option.showWarning(option.key, options);
            }
        });
    }
    onProcessOptions(key, parentOptions, commandName) {
        if (key === 'bottomTabs' &&
            parentOptions[key]?.visible !== undefined &&
            react_native_1.Platform.OS === 'ios' &&
            commandName === 'mergeOptions') {
            this.deprecateBottomTabsVisibility(parentOptions);
        }
        if (key === 'searchBar' && react_native_1.Platform.OS === 'ios' && typeof parentOptions[key] === 'boolean') {
            this.deprecateSearchBarOptions(parentOptions);
        }
        if (key === 'interpolation' && typeof parentOptions[key] === 'string') {
            this.deprecateInterpolationOptions(parentOptions);
        }
        if (key === 'showModal' || key === 'dismissModal') {
            if (typeof parentOptions[key] === 'object' &&
                !('enter' in parentOptions[key]) &&
                !('exit' in parentOptions[key]))
                console.warn(`${key} without enter/exit is deprecated, and will be changed  in the next major version. For more information see https://wix.github.io/react-native-navigation/docs/style-animations#modal-animations`, parentOptions);
        }
    }
    onProcessDefaultOptions(_key, _parentOptions) { }
    deprecateSearchBarOptions = (0, once_1.default)((parentOptions) => {
        console.warn(`toggling searchBar visibility using a boolean value will be removed in the next major version. For more information see https://github.com/wix/react-native-navigation/issues/6585`, parentOptions);
    });
    deprecateInterpolationOptions = (0, once_1.default)((parentOptions) => {
        console.warn(`Using Interpolation types as strings has been deprecated and will be removed in the next major version. For more information see https://github.com/wix/react-native-navigation/pull/6644`, parentOptions);
    });
    deprecateBottomTabsVisibility = (0, once_1.default)((parentOptions) => {
        console.warn(`toggling bottomTabs visibility is deprecated on iOS. For more information see https://github.com/wix/react-native-navigation/issues/6416`, parentOptions);
    });
}
exports.Deprecations = Deprecations;
