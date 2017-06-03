export default class PublicEventsRegistry {
  constructor(nativeEventsReceiver) {
    this.nativeEventsReceiver = nativeEventsReceiver;
  }

  onAppLaunched(callback) {
    this.nativeEventsReceiver.appLaunched(callback);
  }
}
