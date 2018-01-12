const Lifecycle = require('../components/Lifecycle');

class PrivateEventsListener {
  constructor(nativeEventsReceiver, store) {
    this.nativeEventsReceiver = nativeEventsReceiver;
    this.lifecycle = new Lifecycle(store);
  }

  listenAndHandlePrivateEvents() {
    this.nativeEventsReceiver.registerComponentDidAppear(this.lifecycle.componentDidAppear);
    this.nativeEventsReceiver.registerComponentDidDisappear(this.lifecycle.componentDidDisappear);
    this.nativeEventsReceiver.registerNavigationButtonPressed(this.lifecycle.onNavigationButtonPressed);
  }
}

module.exports = PrivateEventsListener;
