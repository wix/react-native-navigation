const { NativeModules, NativeEventEmitter } = require('react-native');

class NativeEventsReceiver {
  constructor() {
    this.emitter = new NativeEventEmitter(NativeModules.RNNEventEmitter);
  }

  containerStart(callback) {
    this.emitter.addListener('RNN.containerStart', callback);
  }

  containerStop(callback) {
    this.emitter.addListener('RNN.containerStop', callback);
  }

  appLaunched(callback) {
    this.emitter.addListener('RNN.appLaunched', callback);
  }
}

module.exports = NativeEventsReceiver;
