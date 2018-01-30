import { Lifecycle } from '../components/Lifecycle';

export class PrivateEventsListener {
  private nativeEventsReceiver;
  private lifecycle;

  constructor(nativeEventsReceiver, store) {
    this.nativeEventsReceiver = nativeEventsReceiver;
    this.lifecycle = new Lifecycle(store);
  }

  public listenAndHandlePrivateEvents() {
    this.nativeEventsReceiver.registerComponentDidAppear(this.lifecycle.componentDidAppear);
    this.nativeEventsReceiver.registerComponentDidDisappear(this.lifecycle.componentDidDisappear);
    this.nativeEventsReceiver.registerNavigationButtonPressed(this.lifecycle.onNavigationButtonPressed);
  }
}
