"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
class NativeEventsReceiver {
    constructor() {
        // NOTE: This try catch is workaround for integration tests
        // TODO: mock NativeEventEmitter in integration tests rather done adding try catch in source code
        try {
            this.emitter = new react_native_1.NativeEventEmitter(react_native_1.NativeModules.RNNEventEmitter);
        }
        catch (e) {
            this.emitter = {
                addListener: () => {
                    return {
                        remove: () => undefined
                    };
                }
            };
        }
    }
    registerAppLaunchedListener(callback) {
        return this.emitter.addListener('RNN.AppLaunched', callback);
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
    registerModalDismissedListener(callback) {
        return this.emitter.addListener('RNN.ModalDismissed', callback);
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
}
exports.NativeEventsReceiver = NativeEventsReceiver;
