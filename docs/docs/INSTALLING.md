# Installing

### Add dependency
`npm install --save react-native-navigation@alpha`

### Configure Xcode
drag-drop `node_modules/react-native-navigation/lib/ios/ReactNativeNavigation.xcodeproj` into Libraries folder in xcode (along the rest of react)

Add ReactNativeNavigation into build-phases

### Configure Gradle
add `compile project(':react-native-navigation')` in build.gradle under dependencies.

add

```
include ':react-native-navigation'
project(':react-native-navigation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-navigation/lib/android/app/')
```
in settings.gradle

### Setup iOS
in AppDelegate:

add:

`#import <ReactNativeNavigation/ReactNativeNavigation.h>`

remove everything in the method didFinishLaunchingWithOptions and add:

```
NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index.ios" fallbackResource:nil];
[ReactNativeNavigation bootstrap:jsCodeLocation launchOptions:launchOptions];
```

### Setup Android

Make your Application object extend from `NavigationApplication` and implement `isDebug` as `{YOUR_PACKAGE_NAME}.BuildConfig.DEBUG`

Make your Acitivty extend from `NavigationActivity`
