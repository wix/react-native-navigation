"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NavigationComponent = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importDefault(require("react"));
class NavigationComponent extends react_1.default.Component {
    /**
     * Options used to apply a style configuration when the screen appears.
     *
     * This field can either contain the concrete options to be applied, or a generator function
     * which accepts props and returns an Options object.
     */
    static options;
    componentWillAppear(_event) { }
    componentDidAppear(_event) { }
    componentDidDisappear(_event) { }
    navigationButtonPressed(_event) { }
    searchBarUpdated(_event) { }
    searchBarCancelPressed(_event) { }
    previewCompleted(_event) { }
    screenPopped(_event) { }
}
exports.NavigationComponent = NavigationComponent;
