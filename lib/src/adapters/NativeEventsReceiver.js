const { NativeModules, NativeEventEmitter } = require('react-native');

class NativeEventsReceiver {
  constructor() {
    this.emitter = new NativeEventEmitter(NativeModules.RNNEventEmitter);
  }

  containerDidAppear(callback) {
    this.emitter.addListener('RNN.containerDidAppear', callback);
  }

  containerDidDisappear(callback) {
    this.emitter.addListener('RNN.containerDidDisappear', callback);
  }

  appLaunched(callback) {
    this.emitter.addListener('RNN.appLaunched', callback);
  }
}

module.exports = NativeEventsReceiver;
