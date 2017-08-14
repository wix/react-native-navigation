const Lifecycle = require('../containers/Lifecycle');

class PrivateEventsListener {
  constructor(nativeEventsReceiver, store) {
    this.nativeEventsReceiver = nativeEventsReceiver;
    this.lifecycle = new Lifecycle(store);
  }

  listenAndHandlePrivateEvents() {
    this.nativeEventsReceiver.containerDidAppear(this.lifecycle.containerDidAppear);
    this.nativeEventsReceiver.containerDidDisappear(this.lifecycle.containerDidDisappear);
  }
}

module.exports = PrivateEventsListener;
