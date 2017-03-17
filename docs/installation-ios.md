# iOS Installation

### Important
The `latest` (stable) version of this framework is `1.x.x` which supports react-native `0.25.1`. It's installation instructions are [here](https://github.com/wix/react-native-navigation/blob/v1.x.x/README.md#installation---ios). To use it specify `"react-native-navigation": "latest"` in your package.json dependencies.

The following instructions are for the `next` version `2.0.0-experimental.x`, which supports react-native `0.37.0`. To use it specify `"react-native-navigation": "next"` in your package.json dependencies. Bear in mind, as the name of the version suggests - this version is experimental and under heavy development, and will break from time to time - thus when using it you should check out these instructions from time to time to avoid breaking your project. 

----

 * Make sure you are using react-native version 0.37.0

 * In your project folder run `npm install react-native-navigation@next --save`
> Note: We recommend using npm ver 3+. If you insist on using npm ver 2 please notice that the location for react-native-controllers in node_modules will be under the react-native-navigation folder and you'll need to change the paths in Xcode below accordingly.

* In Xcode, in Project Navigator (left pane), right-click on the `Libraries` > `Add files to [project name]`. Add `./node_modules/react-native-navigation/ios/ReactNativeNavigation.xcodeproj` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-1))

* In Xcode, in Project Navigator (left pane), click on your project (top) and select the `Build Phases` tab (right pane). In the `Link Binary With Libraries` section add `libReactNativeNavigation.a` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2))

* In Xcode, in Project Navigator (left pane), click on your project (top) and select the `Build Settings` tab (right pane). In the `Header Search Paths` section add `$(SRCROOT)/../node_modules/react-native-navigation/ios`. Make sure on the right to mark this new path `recursive` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-3))

* In Xcode, under your project files, modify `AppDelegate.m` according to this [example](https://github.com/wix/react-native-navigation/blob/master/example/ios/example/AppDelegate.m)

* Run react-native start