# iOS Installation

1. Install `react-native-navigation` latest stable version.

    ```sh
    npm install --save react-native-navigation@alpha
    ```

2. In Xcode, in Project Navigator (left pane), right-click on the `Libraries` > `Add files to [project name]`. Add `./node_modules/react-native-navigation/lib/ios/ReactNativeNavigation.xcodeproj` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-1))

3. In Xcode, in Project Navigator (left pane), click on your project (top) and select the `Build Phases` tab (right pane). In the `Link Binary With Libraries` section add `libReactNativeNavigation.a` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2))

4. In Xcode, in Project Navigator (left pane), click on your project (top) and select the `Build Settings` tab (right pane). In the `Header Search Paths` section add `$(SRCROOT)/../node_modules/react-native-navigation/lib/ios`. Make sure on the right to mark this new path `recursive` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-3))

5. In Xcode, under your project files, modify `AppDelegate.m`. add:

`#import <ReactNativeNavigation/ReactNativeNavigation.h>`

remove everything in the method didFinishLaunchingWithOptions and add:

```
NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index.ios" fallbackResource:nil];
[ReactNativeNavigation bootstrap:jsCodeLocation launchOptions:launchOptions];

return YES;
```
