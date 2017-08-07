class PublicEventsRegistry {
  constructor(nativeEventsReceiver) {
    this.nativeEventsReceiver = nativeEventsReceiver;
  }

  onAppLaunched(callback) {
    this.nativeEventsReceiver.appLaunched(callback);
  }
}

module.exports = PublicEventsRegistry;
