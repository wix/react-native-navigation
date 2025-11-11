"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.TopBar = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const react_native_1 = require("react-native");
const src_1 = require("react-native-navigation");
const LayoutStore_1 = require("../Stores/LayoutStore");
const NavigationButton_1 = require("./NavigationButton");
const EventsStore_1 = require("../Stores/EventsStore");
const DEFAULT_BACK_BUTTON_ID = 'RNN.back';
const TopBar = class extends react_1.Component {
    constructor(props) {
        super(props);
    }
    render() {
        const topBarOptions = this.props.topBarOptions;
        const topbarTestId = topBarOptions?.testID;
        const titleTestId = topbarTestId ? { testID: `${topbarTestId}.title` } : {};
        const subtitleTestId = topbarTestId ? { testID: `${topbarTestId}.subtitle` } : {};
        if (topBarOptions?.visible === false)
            return null;
        else {
            const component = topBarOptions?.title?.component;
            return (react_1.default.createElement(react_native_1.View, { testID: topbarTestId },
                react_1.default.createElement(react_native_1.Text, { ...titleTestId }, topBarOptions?.title?.text),
                react_1.default.createElement(react_native_1.Text, { ...subtitleTestId }, topBarOptions?.subtitle?.text),
                this.renderButtons(topBarOptions?.leftButtons),
                this.renderButtons(topBarOptions?.rightButtons),
                component &&
                    //@ts-ignore
                    this.renderComponent(component.componentId, component.name),
                this.shouldRenderBackButton(this.props.layoutNode) && this.renderBackButton()));
        }
    }
    shouldRenderBackButton(layoutNode) {
        const backButtonVisible = layoutNode.resolveOptions().topBar?.backButton?.visible;
        return layoutNode.getStack().children.length > 1 && backButtonVisible !== false;
    }
    renderButtons(buttons = []) {
        return buttons.map((button, i) => {
            return (react_1.default.createElement(NavigationButton_1.NavigationButton, { button: button, key: button.id || i, componentId: this.props.layoutNode.nodeId }));
        });
    }
    renderBackButton() {
        const backButtonOptions = this.props.backButtonOptions;
        return (react_1.default.createElement(react_native_1.Button, { testID: backButtonOptions?.testID, title: backButtonOptions && backButtonOptions.title ? backButtonOptions.title : '', onPress: () => {
                if (backButtonOptions?.popStackOnPress === false) {
                    EventsStore_1.events.invokeNavigationButtonPressed({
                        buttonId: backButtonOptions?.id || DEFAULT_BACK_BUTTON_ID,
                        componentId: this.props.layoutNode.nodeId,
                    });
                }
                else {
                    LayoutStore_1.LayoutStore.pop(this.props.layoutNode.nodeId);
                }
            } }));
    }
    renderComponent(id, name, testID) {
        const Component = src_1.Navigation.mock.store.getComponentClassForName(name)();
        const props = src_1.Navigation.mock.store.getPropsForId(id);
        return (react_1.default.createElement(react_native_1.View, { key: id, testID: testID },
            react_1.default.createElement(Component, { ...props, componentId: id })));
    }
};
exports.TopBar = TopBar;
