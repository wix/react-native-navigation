import { Lifecycle } from '../components/Lifecycle';

export class PrivateEventsListener {
  private lifecycle;

  constructor(
    private readonly nativeEventsReceiver,
    private readonly store) {
    this.lifecycle = new Lifecycle(this.store);
  }

  public listenAndHandlePrivateEvents() {
    this.nativeEventsReceiver.registerComponentDidAppear(this.lifecycle.componentDidAppear);
    this.nativeEventsReceiver.registerComponentDidDisappear(this.lifecycle.componentDidDisappear);
    this.nativeEventsReceiver.registerNavigationButtonPressed(this.lifecycle.onNavigationButtonPressed);
  }
}
