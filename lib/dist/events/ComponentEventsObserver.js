"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const _ = require("lodash");
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
        this.notifySearchBarUpdated = this.notifySearchBarUpdated.bind(this);
        this.notifySearchBarCancelPressed = this.notifySearchBarCancelPressed.bind(this);
        this.notifyPreviewCompleted = this.notifyPreviewCompleted.bind(this);
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
        this.nativeEventsReceiver.registerSearchBarUpdatedListener(this.notifySearchBarUpdated);
        this.nativeEventsReceiver.registerSearchBarCancelPressedListener(this.notifySearchBarCancelPressed);
        this.nativeEventsReceiver.registerPreviewCompletedListener(this.notifyPreviewCompleted);
    }
    bindComponent(component, componentId) {
        const computedComponentId = componentId || component.props.componentId;
        if (!_.isString(computedComponentId)) {
            throw new Error(`bindComponent expects a component with a componentId in props or a componentId as the second argument`);
        }
        if (_.isNil(this.listeners[computedComponentId])) {
            this.listeners[computedComponentId] = {};
        }
        const key = _.uniqueId();
        this.listeners[computedComponentId][key] = component;
        return { remove: () => _.unset(this.listeners[computedComponentId], key) };
    }
    unmounted(componentId) {
        _.unset(this.listeners, componentId);
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
    notifySearchBarUpdated(event) {
        this.triggerOnAllListenersByComponentId(event, 'searchBarUpdated');
    }
    notifySearchBarCancelPressed(event) {
        this.triggerOnAllListenersByComponentId(event, 'searchBarCancelPressed');
    }
    notifyPreviewCompleted(event) {
        this.triggerOnAllListenersByComponentId(event, 'previewCompleted');
    }
    triggerOnAllListenersByComponentId(event, method) {
        _.forEach(this.listeners[event.componentId], (component) => {
            if (component && component[method]) {
                component[method](event);
            }
        });
    }
}
exports.ComponentEventsObserver = ComponentEventsObserver;
