# iOS Installation

!> Make sure you are using **react-native** version >= 0.51. We also recommend using npm version >= 3

1. Install `react-native-navigation` latest stable version.

    ```sh
    yarn add react-native-navigation@latest
    ```

2. In Xcode, in Project Navigator (left pane), right-click on the `Libraries` > `Add files to [project name]`. Add `./node_modules/react-native-navigation/ios/ReactNativeNavigation.xcodeproj` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-1))

3. In Xcode, in Project Navigator (left pane), click on your project (top), then click on your *target* row (on the "project and targets list", which is on the left column of the right pane) and select the `Build Phases` tab (right pane). In the `Link Binary With Libraries` section add `libReactNativeNavigation.a` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2))

4. In Xcode, in Project Navigator (left pane), click on your project (top), then click on your *project* row (on the "project and targets list") and select the `Build Settings` tab (right pane). In the `Header Search Paths` section add `$(SRCROOT)/../node_modules/react-native-navigation/ios`. Make sure on the right to mark this new path `recursive` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-3))

5. In Xcode, you will need to edit this file: `AppDelegate.m`. This function is the main entry point for your app:

    ```objc
    - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions { ... }
    ```

Its content must be replaced with the content of this [reference](https://github.com/wix/react-native-navigation/blob/master/example/ios/example/AppDelegate.m)

Replace `@"index.ios"` with `@"index"` if you are using `index.js` as your entry point instead of `index.ios.js` and `index.android.js` (it is the default since React Native 0.49).
