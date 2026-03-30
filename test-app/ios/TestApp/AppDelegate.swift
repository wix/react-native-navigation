import UIKit
import React
import React_RCTAppDelegate
import ReactNativeNavigation

@main
class AppDelegate: RNNAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
  ) -> Bool {
    let delegate = TestAppReactNativeDelegate()
    self.reactNativeDelegate = delegate

    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}

class TestAppReactNativeDelegate: RCTDefaultReactNativeFactoryDelegate {
  override func sourceURL(for bridge: RCTBridge) -> URL? {
    self.bundleURL()
  }

  override func bundleURL() -> URL? {
#if DEBUG
    RCTBundleURLProvider.sharedSettings().jsBundleURL(forBundleRoot: "index")
#else
    Bundle.main.url(forResource: "main", withExtension: "jsbundle")
#endif
  }
}
