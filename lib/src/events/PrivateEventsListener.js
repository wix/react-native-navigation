import _ from 'lodash';
import Lifecycle from '../containers/Lifecycle';

export default class PrivateEventsListener {
  constructor(nativeEventsReceiver, store) {
    this.nativeEventsReceiver = nativeEventsReceiver;
    this.lifecycle = new Lifecycle(store);
  }

  listenAndHandlePrivateEvents() {
    this.nativeEventsReceiver.containerStart(this.lifecycle.containerStart);
    this.nativeEventsReceiver.containerStop(this.lifecycle.containerStop);
  }
}
