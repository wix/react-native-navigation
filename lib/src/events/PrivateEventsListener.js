const Lifecycle = require('../containers/Lifecycle');

class PrivateEventsListener {
  constructor(nativeEventsReceiver, store) {
    this.nativeEventsReceiver = nativeEventsReceiver;
    this.lifecycle = new Lifecycle(store);
  }

  listenAndHandlePrivateEvents() {
    this.nativeEventsReceiver.containerStart(this.lifecycle.containerStart);
    this.nativeEventsReceiver.containerStop(this.lifecycle.containerStop);
  }
}

module.exports = PrivateEventsListener;
