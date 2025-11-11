"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NativeEventsReceiver = void 0;
const tslib_1 = require("tslib");
const lodash_1 = tslib_1.__importDefault(require("lodash"));
const EventsStore_1 = require("../Stores/EventsStore");
class NativeEventsReceiver {
    registerAppLaunchedListener(callback) {
        callback();
        return {
            remove: () => { },
        };
    }
    registerComponentWillAppearListener(callback) {
        EventsStore_1.events.componentWillAppear.push(callback);
        return {
            remove: () => {
                lodash_1.default.remove(EventsStore_1.events.componentWillAppear, (value) => value === callback);
            },
        };
    }
    registerComponentDidAppearListener(callback) {
        EventsStore_1.events.componentDidAppear.push(callback);
        return {
            remove: () => {
                lodash_1.default.remove(EventsStore_1.events.componentDidAppear, (value) => value === callback);
            },
        };
    }
    registerComponentDidDisappearListener(callback) {
        EventsStore_1.events.componentDidDisappear.push(callback);
        return {
            remove: () => {
                lodash_1.default.remove(EventsStore_1.events.componentDidDisappear, (value) => value === callback);
            },
        };
    }
    registerNavigationButtonPressedListener(callback) {
        EventsStore_1.events.navigationButtonPressed.push(callback);
        return {
            remove: () => {
                lodash_1.default.remove(EventsStore_1.events.navigationButtonPressed, (value) => value === callback);
            },
        };
    }
    registerBottomTabPressedListener(callback) {
        EventsStore_1.events.bottomTabPressed.push(callback);
        return {
            remove: () => {
                lodash_1.default.remove(EventsStore_1.events.bottomTabPressed, (value) => value === callback);
            },
        };
    }
    registerModalDismissedListener(callback) {
        EventsStore_1.events.modalDismissed.push(callback);
        return {
            remove: () => {
                lodash_1.default.remove(EventsStore_1.events.modalDismissed, (value) => value === callback);
            },
        };
    }
    registerModalAttemptedToDismissListener(_callback) {
        return {
            remove: () => { },
        };
    }
    registerSearchBarUpdatedListener(_callback) {
        return {
            remove: () => { },
        };
    }
    registerSearchBarCancelPressedListener(_callback) {
        return {
            remove: () => { },
        };
    }
    registerPreviewCompletedListener(_callback) {
        return {
            remove: () => { },
        };
    }
    registerCommandCompletedListener(callback) {
        EventsStore_1.events.commandCompleted.push(callback);
        return {
            remove: () => { },
        };
    }
    registerBottomTabSelectedListener(_callback) {
        return {
            remove: () => { },
        };
    }
    registerBottomTabLongPressedListener(_callback) {
        return {
            remove: () => { },
        };
    }
    registerScreenPoppedListener(callback) {
        EventsStore_1.events.screenPopped.push(callback);
        return {
            remove: () => { },
        };
    }
}
exports.NativeEventsReceiver = NativeEventsReceiver;
