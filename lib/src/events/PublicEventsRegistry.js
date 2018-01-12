class PublicEventsRegistry {
  constructor(nativeEventsReceiver) {
    this.nativeEventsReceiver = nativeEventsReceiver;
  }

  onAppLaunched(callback) {
    this.nativeEventsReceiver.registerAppLaunched(callback);
  }
}

module.exports = PublicEventsRegistry;
