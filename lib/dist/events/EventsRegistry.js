"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class EventsRegistry {
    constructor(nativeEventsReceiver, commandsObserver, componentEventsObserver) {
        this.nativeEventsReceiver = nativeEventsReceiver;
        this.commandsObserver = commandsObserver;
        this.componentEventsObserver = componentEventsObserver;
    }
    registerAppLaunchedListener(callback) {
        return this.nativeEventsReceiver.registerAppLaunchedListener(callback);
    }
    registerComponentDidAppearListener(callback) {
        return this.nativeEventsReceiver.registerComponentDidAppearListener(callback);
    }
    registerComponentDidDisappearListener(callback) {
        return this.nativeEventsReceiver.registerComponentDidDisappearListener(callback);
    }
    registerCommandCompletedListener(callback) {
        return this.nativeEventsReceiver.registerCommandCompletedListener(callback);
    }
    registerBottomTabSelectedListener(callback) {
        return this.nativeEventsReceiver.registerBottomTabSelectedListener(callback);
    }
    registerNavigationButtonPressedListener(callback) {
        return this.nativeEventsReceiver.registerNavigationButtonPressedListener(callback);
    }
    registerModalDismissedListener(callback) {
        return this.nativeEventsReceiver.registerModalDismissedListener(callback);
    }
    registerSearchBarUpdatedListener(callback) {
        return this.nativeEventsReceiver.registerSearchBarUpdatedListener(callback);
    }
    registerSearchBarCancelPressedListener(callback) {
        return this.nativeEventsReceiver.registerSearchBarCancelPressedListener(callback);
    }
    registerPreviewCompletedListener(callback) {
        return this.nativeEventsReceiver.registerPreviewCompletedListener(callback);
    }
    registerCommandListener(callback) {
        return this.commandsObserver.register(callback);
    }
    bindComponent(component, componentId) {
        return this.componentEventsObserver.bindComponent(component, componentId);
    }
}
exports.EventsRegistry = EventsRegistry;
