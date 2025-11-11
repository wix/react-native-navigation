"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.EventsRegistry = void 0;
class EventsRegistry {
    nativeEventsReceiver;
    commandsObserver;
    componentEventsObserver;
    constructor(nativeEventsReceiver, commandsObserver, componentEventsObserver) {
        this.nativeEventsReceiver = nativeEventsReceiver;
        this.commandsObserver = commandsObserver;
        this.componentEventsObserver = componentEventsObserver;
    }
    registerAppLaunchedListener(callback) {
        return this.nativeEventsReceiver.registerAppLaunchedListener(callback);
    }
    registerComponentWillAppearListener(callback) {
        return this.nativeEventsReceiver.registerComponentWillAppearListener(callback);
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
    registerBottomTabPressedListener(callback) {
        return this.nativeEventsReceiver.registerBottomTabPressedListener(callback);
    }
    registerBottomTabLongPressedListener(callback) {
        return this.nativeEventsReceiver.registerBottomTabLongPressedListener(callback);
    }
    registerNavigationButtonPressedListener(callback) {
        return this.nativeEventsReceiver.registerNavigationButtonPressedListener(callback);
    }
    registerModalDismissedListener(callback) {
        return this.nativeEventsReceiver.registerModalDismissedListener(callback);
    }
    registerModalAttemptedToDismissListener(callback) {
        return this.nativeEventsReceiver.registerModalAttemptedToDismissListener(callback);
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
    registerComponentListener(listener, componentId) {
        return this.componentEventsObserver.registerComponentListener(listener, componentId);
    }
    registerScreenPoppedListener(callback) {
        return this.nativeEventsReceiver.registerScreenPoppedListener(callback);
    }
}
exports.EventsRegistry = EventsRegistry;
