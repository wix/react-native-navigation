"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NavigationButton = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const react_native_1 = require("react-native");
const src_1 = require("react-native-navigation");
const EventsStore_1 = require("../Stores/EventsStore");
const NavigationButton = class extends react_1.Component {
    ref = undefined;
    render() {
        const { button, componentId } = this.props;
        if (button.component)
            return this.renderButtonComponent();
        return (react_1.default.createElement(react_native_1.Button, { testID: button.testID, key: button.id, title: button.text || '', disabled: button.enabled === false, onPress: () => button.enabled !== false &&
                EventsStore_1.events.invokeNavigationButtonPressed({
                    buttonId: button.id,
                    componentId,
                }) }));
    }
    renderButtonComponent() {
        const { button, componentId } = this.props;
        // @ts-ignore
        const buttonComponentId = button.component.componentId;
        // @ts-ignore
        const ComponentClass = src_1.Navigation.mock.store.getComponentClassForName(button.component.name);
        if (!ComponentClass) {
            throw new Error(`Cannot find registered component for: ${button.component?.name}`);
        }
        const ButtonComponent = ComponentClass();
        const props = src_1.Navigation.mock.store.getPropsForId(buttonComponentId);
        return (react_1.default.createElement(react_native_1.TouchableOpacity, { onPress: () => {
                if (this.ref) {
                    this.invokeOnClick(
                    // @ts-ignore
                    (this.ref._reactInternalFiber || this.ref._reactInternals).return.stateNode);
                }
                EventsStore_1.events.invokeNavigationButtonPressed({
                    buttonId: button.id,
                    componentId: componentId,
                });
            }, testID: button.testID },
            react_1.default.createElement(ButtonComponent, { key: buttonComponentId, ...props, componentId: buttonComponentId, ref: (ref) => (this.ref = ref) })));
    }
    invokeOnClick(stateNode) {
        if (stateNode.children) {
            // @ts-ignore
            stateNode.children.forEach((instance) => {
                if (instance.internalInstanceHandle &&
                    instance.internalInstanceHandle.stateNode.props.onClick) {
                    instance.internalInstanceHandle.stateNode.props.onClick();
                }
                this.invokeOnClick(instance);
            });
        }
    }
};
exports.NavigationButton = NavigationButton;
