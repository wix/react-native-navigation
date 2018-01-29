export class PublicEventsRegistry {
  private nativeEventsReceiver;

  constructor(nativeEventsReceiver) {
    this.nativeEventsReceiver = nativeEventsReceiver;
  }

  public onAppLaunched(callback) {
    this.nativeEventsReceiver.registerAppLaunched(callback);
  }

  public navigationCommands(callback) {
    this.nativeEventsReceiver.registerNavigationCommands(callback);
  }

  public componentLifecycle(callback) {
    this.nativeEventsReceiver.registerComponentLifecycle(callback);
  }
}
