"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NativeEventsReceiver = void 0;
const tslib_1 = require("tslib");
const react_native_1 = require("react-native");
const NativeRNNTurboEventEmitter_1 = tslib_1.__importDefault(require("./NativeRNNTurboEventEmitter"));
class NativeEventsReceiver {
    emitter;
    constructor() {
        try {
            this.emitter = new react_native_1.NativeEventEmitter(NativeRNNTurboEventEmitter_1.default);
        }
        catch (e) {
            this.emitter = {
                addListener: () => {
                    return {
                        remove: () => undefined,
                    };
                },
            };
        }
    }
    registerAppLaunchedListener(callback) {
        return this.emitter.addListener('RNN.AppLaunched', callback);
    }
    registerComponentWillAppearListener(callback) {
        return this.emitter.addListener('RNN.ComponentWillAppear', callback);
    }
    registerComponentDidAppearListener(callback) {
        return this.emitter.addListener('RNN.ComponentDidAppear', callback);
    }
    registerComponentDidDisappearListener(callback) {
        return this.emitter.addListener('RNN.ComponentDidDisappear', callback);
    }
    registerNavigationButtonPressedListener(callback) {
        return this.emitter.addListener('RNN.NavigationButtonPressed', callback);
    }
    registerBottomTabPressedListener(callback) {
        return this.emitter.addListener('RNN.BottomTabPressed', callback);
    }
    registerModalDismissedListener(callback) {
        return this.emitter.addListener('RNN.ModalDismissed', callback);
    }
    registerModalAttemptedToDismissListener(callback) {
        return this.emitter.addListener('RNN.ModalAttemptedToDismiss', callback);
    }
    registerSearchBarUpdatedListener(callback) {
        return this.emitter.addListener('RNN.SearchBarUpdated', callback);
    }
    registerSearchBarCancelPressedListener(callback) {
        return this.emitter.addListener('RNN.SearchBarCancelPressed', callback);
    }
    registerPreviewCompletedListener(callback) {
        return this.emitter.addListener('RNN.PreviewCompleted', callback);
    }
    registerCommandCompletedListener(callback) {
        return this.emitter.addListener('RNN.CommandCompleted', callback);
    }
    registerBottomTabSelectedListener(callback) {
        return this.emitter.addListener('RNN.BottomTabSelected', callback);
    }
    registerBottomTabLongPressedListener(callback) {
        return this.emitter.addListener('RNN.BottomTabLongPressed', callback);
    }
    registerScreenPoppedListener(callback) {
        return this.emitter.addListener('RNN.ScreenPopped', callback);
    }
}
exports.NativeEventsReceiver = NativeEventsReceiver;
