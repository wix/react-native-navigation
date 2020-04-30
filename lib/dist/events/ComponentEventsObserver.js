"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const isString_1 = tslib_1.__importDefault(require("lodash/isString"));
const isNil_1 = tslib_1.__importDefault(require("lodash/isNil"));
const uniqueId_1 = tslib_1.__importDefault(require("lodash/uniqueId"));
const unset_1 = tslib_1.__importDefault(require("lodash/unset"));
const forEach_1 = tslib_1.__importDefault(require("lodash/forEach"));
class ComponentEventsObserver {
    constructor(nativeEventsReceiver, store) {
        this.nativeEventsReceiver = nativeEventsReceiver;
        this.store = store;
        this.listeners = {};
        this.alreadyRegistered = false;
        this.notifyComponentDidAppear = this.notifyComponentDidAppear.bind(this);
        this.notifyComponentDidDisappear = this.notifyComponentDidDisappear.bind(this);
        this.notifyNavigationButtonPressed = this.notifyNavigationButtonPressed.bind(this);
        this.notifyModalDismissed = this.notifyModalDismissed.bind(this);
        this.notifyModalAttemptedToDismiss = this.notifyModalAttemptedToDismiss.bind(this);
        this.notifySearchBarUpdated = this.notifySearchBarUpdated.bind(this);
        this.notifySearchBarCancelPressed = this.notifySearchBarCancelPressed.bind(this);
        this.notifyPreviewCompleted = this.notifyPreviewCompleted.bind(this);
        this.notifyScreenPopped = this.notifyScreenPopped.bind(this);
    }
    registerOnceForAllComponentEvents() {
        if (this.alreadyRegistered) {
            return;
        }
        this.alreadyRegistered = true;
        this.nativeEventsReceiver.registerComponentDidAppearListener(this.notifyComponentDidAppear);
        this.nativeEventsReceiver.registerComponentDidDisappearListener(this.notifyComponentDidDisappear);
        this.nativeEventsReceiver.registerNavigationButtonPressedListener(this.notifyNavigationButtonPressed);
        this.nativeEventsReceiver.registerModalDismissedListener(this.notifyModalDismissed);
        this.nativeEventsReceiver.registerModalAttemptedToDismissListener(this.notifyModalAttemptedToDismiss);
        this.nativeEventsReceiver.registerSearchBarUpdatedListener(this.notifySearchBarUpdated);
        this.nativeEventsReceiver.registerSearchBarCancelPressedListener(this.notifySearchBarCancelPressed);
        this.nativeEventsReceiver.registerPreviewCompletedListener(this.notifyPreviewCompleted);
        this.nativeEventsReceiver.registerScreenPoppedListener(this.notifyPreviewCompleted);
    }
    bindComponent(component, componentId) {
        const computedComponentId = componentId || component.props.componentId;
        if (!isString_1.default(computedComponentId)) {
            throw new Error(`bindComponent expects a component with a componentId in props or a componentId as the second argument`);
        }
        if (isNil_1.default(this.listeners[computedComponentId])) {
            this.listeners[computedComponentId] = {};
        }
        const key = uniqueId_1.default();
        this.listeners[computedComponentId][key] = component;
        return { remove: () => unset_1.default(this.listeners[computedComponentId], key) };
    }
    unmounted(componentId) {
        unset_1.default(this.listeners, componentId);
    }
    notifyComponentDidAppear(event) {
        event.passProps = this.store.getPropsForId(event.componentId);
        this.triggerOnAllListenersByComponentId(event, 'componentDidAppear');
    }
    notifyComponentDidDisappear(event) {
        this.triggerOnAllListenersByComponentId(event, 'componentDidDisappear');
    }
    notifyNavigationButtonPressed(event) {
        this.triggerOnAllListenersByComponentId(event, 'navigationButtonPressed');
    }
    notifyModalDismissed(event) {
        this.triggerOnAllListenersByComponentId(event, 'modalDismissed');
    }
    notifyModalAttemptedToDismiss(event) {
        this.triggerOnAllListenersByComponentId(event, 'modalAttemptedToDismiss');
    }
    notifySearchBarUpdated(event) {
        this.triggerOnAllListenersByComponentId(event, 'searchBarUpdated');
    }
    notifySearchBarCancelPressed(event) {
        this.triggerOnAllListenersByComponentId(event, 'searchBarCancelPressed');
    }
    notifyPreviewCompleted(event) {
        this.triggerOnAllListenersByComponentId(event, 'previewCompleted');
    }
    notifyScreenPopped(event) {
        this.triggerOnAllListenersByComponentId(event, 'screenPopped');
    }
    triggerOnAllListenersByComponentId(event, method) {
        forEach_1.default(this.listeners[event.componentId], (component) => {
            if (component && component[method]) {
                component[method](event);
            }
        });
    }
}
exports.ComponentEventsObserver = ComponentEventsObserver;
