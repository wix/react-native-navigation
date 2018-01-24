const { NativeModules, NativeEventEmitter } = require('react-native');

class NativeEventsReceiver {
  constructor() {
    this.emitter = new NativeEventEmitter(NativeModules.RNNEventEmitter);
  }

  registerComponentDidAppear(callback) {
    this.emitter.addListener('RNN.componentDidAppear', callback);
  }

  registerComponentDidDisappear(callback) {
    this.emitter.addListener('RNN.componentDidDisappear', callback);
  }

  registerAppLaunched(callback) {
    this.emitter.addListener('RNN.appLaunched', callback);
  }

  registerNavigationCommands(callback) {
    this.emitter.addListener('RNN.navigationCommands', callback);
  }

  registerComponentLifecycle(callback) {
    this.emitter.addListener('RNN.componentLifecycle', callback);
  }

  registerNavigationButtonPressed(callback) {
    this.emitter.addListener('RNN.navigationButtonPressed', callback);
  }
}

module.exports = NativeEventsReceiver;
