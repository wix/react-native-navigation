# Installing

## Requirements
* node >= 8
* react-native >= 0.51

## npm
* `npm install --save react-native-navigation`

## Installing with react-native link
If you're using RN 0.60 or higher, you can link RNN automatically with react-native link. Otherwise, follow the manual installation steps. Unlike most other libraries, react-native-navigation requires you to make a few changes to native files. To make all the necessary changes, run `react-native link react-native-navigation` in your project's root folder. Make sure to commit the changes introduced by the link script.

If the link script completed successfully, you're good to go! If one of the steps failed, you'll need to complete the relevant step in the manual installation steps below.

## Manual Installation
If installation with react-native link did not work, follow the manual installation steps.

### iOS

> Make sure your Xcode is updated. We recommend editing `.h` and `.m` files in Xcode as the IDE will usually point out common errors.

#### Native Installation

1. In Xcode, in Project Navigator (left pane), right-click on the `Libraries` > `Add files to [project name]`. Add `node_modules/react-native-navigation/lib/ios/ReactNativeNavigation.xcodeproj` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#manual-linking)).

2. In Xcode, in Project Navigator (left pane), click on your project (top), then click on your *target* row (on the "project and targets list", which is on the left column of the right pane) and select the `Build Phases` tab (right pane). In the `Link Binary With Libraries` section add `libReactNativeNavigation.a` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2)).

	a. If you're seeing an error message in Xcode such as:
	```
	'ReactNativeNavigation/ReactNativeNavigation.h' file not found.
	```
	You may also need to add a Header Search Path: ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-3)).
	```objectivec
	$(SRCROOT)/../node_modules/react-native-navigation/lib/ios
	```

3. In Xcode, you will need to edit this file: `AppDelegate.m`. This function is the main entry point for your app:

	```objectivec
	 - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions { ... }

	```

	Its content should look like this:
	```objectivec
	#import "AppDelegate.h"

	#import <React/RCTBundleURLProvider.h>
	#import <React/RCTRootView.h>
	#import <ReactNativeNavigation/ReactNativeNavigation.h>

	@implementation AppDelegate

	- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
	{
		NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
		[ReactNativeNavigation bootstrap:jsCodeLocation launchOptions:launchOptions];

		return YES;
	}

	@end
	```

    a. If, in Xcode, you see the following error message in `AppDelegate.m` next to `#import "RCTBundleURLProvider.h"`: 
  	```
  	! 'RCTBundleURLProvider.h' file not found
  	```
  	This is because the `React` scheme is missing from your project. You can verify this by opening the `Product` menu and the `Scheme` submenu. 
  	To make the `React` scheme available to your project, run `npm install -g react-native-git-upgrade` followed by `react-native-git-upgrade`. Once this is done, you can click back to the menu in Xcode: `Product -> Scheme -> Manage Schemes`, then click '+' to add a new scheme. From the `Target` menu, select "React", and click the checkbox to make the scheme `shared`. This should make the error disappear.

    b. If, in Xcode, you see the following warning message in `AppDelegate.m` next to `#import "@implementation AppDelegate"`:
  	```
  	Class 'AppDelegate' does not conform to protocol 'RCTBridgeDelegate'
  	```
  	You can remove `RCTBridgeDelegate` from this file: `AppDelegate.h`:
  	```diff
  	- #import <React/RCTBridgeDelegate.h>
  	- @interface AppDelegate : UIResponder <UIApplicationDelegate, RCTBridgeDelegate>
  	+ @interface AppDelegate : UIResponder <UIApplicationDelegate>
  		...
  	```

#### Installation with CocoaPods

Projects generated using newer versions of react-native use CocoaPods by default. In that case it's easier to install react-native-navigation using CocoaPods.

1. Update your `Podfile`:
**If you're upgrading to v5 from a previous RNN version**, make sure to remove manual linking of RNN

```diff
platform :ios, '9.0'
require_relative '../node_modules/@react-native-community/cli-platform-ios/native_modules'

target 'YourApp' do
  # Pods for YourApp
  pod 'React', :path => '../node_modules/react-native/'
  pod 'React-Core', :path => '../node_modules/react-native/React'
  pod 'React-DevSupport', :path => '../node_modules/react-native/React'
  pod 'React-fishhook', :path => '../node_modules/react-native/Libraries/fishhook'
  pod 'React-RCTActionSheet', :path => '../node_modules/react-native/Libraries/ActionSheetIOS'
  pod 'React-RCTAnimation', :path => '../node_modules/react-native/Libraries/NativeAnimation'
  pod 'React-RCTBlob', :path => '../node_modules/react-native/Libraries/Blob'
  pod 'React-RCTImage', :path => '../node_modules/react-native/Libraries/Image'
  pod 'React-RCTLinking', :path => '../node_modules/react-native/Libraries/LinkingIOS'
  pod 'React-RCTNetwork', :path => '../node_modules/react-native/Libraries/Network'
  pod 'React-RCTSettings', :path => '../node_modules/react-native/Libraries/Settings'
  pod 'React-RCTText', :path => '../node_modules/react-native/Libraries/Text'
  pod 'React-RCTVibration', :path => '../node_modules/react-native/Libraries/Vibration'
  pod 'React-RCTWebSocket', :path => '../node_modules/react-native/Libraries/WebSocket'

  pod 'React-cxxreact', :path => '../node_modules/react-native/ReactCommon/cxxreact'
  pod 'React-jsi', :path => '../node_modules/react-native/ReactCommon/jsi'
  pod 'React-jsiexecutor', :path => '../node_modules/react-native/ReactCommon/jsiexecutor'
  pod 'React-jsinspector', :path => '../node_modules/react-native/ReactCommon/jsinspector'
  pod 'yoga', :path => '../node_modules/react-native/ReactCommon/yoga'

  pod 'DoubleConversion', :podspec => '../node_modules/react-native/third-party-podspecs/DoubleConversion.podspec'
  pod 'glog', :podspec => '../node_modules/react-native/third-party-podspecs/glog.podspec'
  pod 'Folly', :podspec => '../node_modules/react-native/third-party-podspecs/Folly.podspec'

- pod 'ReactNativeNavigation', :podspec => '../node_modules/react-native-navigation/ReactNativeNavigation.podspec'

  use_native_modules!
end
```

2. `cd ios && pod install`

### Android

> Make sure your Android Studio installation is updated. We recommend editing `gradle` and `java` files in Android Studio as the IDE will suggest fixes and point out errors, this way you avoid most common pitfalls.

#### 1 Update `android/build.gradle`:

```diff
buildscript {
  ext {
-   minSdkVersion = 16
+   minSdkVersion = 19 // Or higher
    compileSdkVersion = 26
    targetSdkVersion = 26
    supportLibVersion = "26.1.0"
+   RNNKotlinVersion = "1.3.61" // Or any version above 1.3.x
+   RNNKotlinStdlib = "kotlin-stdlib-jdk8"
  }
	repositories {
        google()
        jcenter()
+        mavenLocal()
+        mavenCentral()
	}
	dependencies {
+        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61" // Or whatever Kotlin version you've specified above
+        classpath 'com.android.tools.build:gradle:3.4.2' // Or higher
-        classpath 'com.android.tools.build:gradle:2.2.3'
	}
}

allprojects {
	repositories {
+		google()
		mavenLocal()
		jcenter()
		maven {
			// All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
			url "$rootDir/../node_modules/react-native/android"
		}
-        maven {
-            url 'https://maven.google.com/'
-            name 'Google'
-        }
+		maven { url 'https://jitpack.io' }
	}
}
```
#### 2 Update `MainActivity.java`

`MainActivity.java` should extend `com.reactnativenavigation.NavigationActivity` instead of `ReactActivity`.

This file is located in `android/app/src/main/java/com/<yourproject>/MainActivity.java`.

```diff
-import com.facebook.react.ReactActivity;
+import com.reactnativenavigation.NavigationActivity;

-public class MainActivity extends ReactActivity {
+public class MainActivity extends NavigationActivity {
-    @Override
-    protected String getMainComponentName() {
-        return "yourproject";
-    }
}
```

If you have any **react-native** related methods, you can safely delete them.

#### 3 Update `MainApplication.java`

This file is located in `android/app/src/main/java/com/<yourproject>/MainApplication.java`.

```diff
...
import android.app.Application;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

+import com.reactnativenavigation.NavigationApplication;
+import com.reactnativenavigation.react.NavigationReactNativeHost;

-public class MainApplication extends Application implements ReactApplication {
+public class MainApplication extends NavigationApplication {

private final ReactNativeHost mReactNativeHost =
-      new ReactNativeHost(this) {
+      new NavigationReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
-    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
  }
}
```

## You can use react-native-navigation \o/

Update `index.js` file
```diff
+import { Navigation } from "react-native-navigation";
-import {AppRegistry} from 'react-native';
import App from "./App";
-import {name as appName} from './app.json';

-AppRegistry.registerComponent(appName, () => App);
+Navigation.registerComponent(`navigation.playground.WelcomeScreen`, () => App);

+Navigation.events().registerAppLaunchedListener(() => {
+  Navigation.setRoot({
+    root: {
+      component: {
+        name: "navigation.playground.WelcomeScreen"
+      }
+    }
+  });
+});
```

⚠️ we use the layout type `component` here, which renders a React component but does not allow you to navigate to others. See [Usage](docs/Usage.md) and [LayoutTypes](docs/layout-types.md) for more options.

## Troubleshooting
### Build app with gradle command
**prefered solution** - The RNN flavor you would like to build is specified in `app/build.gradle`. Therefore in order to compile only that flavor, instead of building your entire project using `./gradlew assembleDebug`, you should instruct gradle to build the app module: `./gradlew app:assembleDebug`. The easiest way is to add a package.json command to build and install your debug Android APK .

```
"scripts": {
  ...
  "android": "cd ./android && ./gradlew app:assembleDebug && ./gradlew installDebug"
}
```

Now run `npm run android` to build your application

### Ignore other RNN flavors
If you don't want to run `npm run android` and want to keep the default `react-native run-android` command, you need to instruct gradle to ignore the other flavors RNN provides.

To do so edit `android/build.gradle` and add:

```diff
+subprojects { subproject ->
+    afterEvaluate {
+        if ((subproject.plugins.hasPlugin('android') || subproject.plugins.hasPlugin('android-library'))) {
+            android {
+                variantFilter { variant ->
+                    def names = variant.flavors*.name
+                    if (names.contains("reactNative51") || names.contains("reactNative55")) {
+                        setIgnore(true)
+                    }
+                }
+            }
+        }
+    }
+}
```

**Note**: As more build variants come available in the future, you will need to adjust the list (`names.contains("reactNative51") || names.contains("reactNative55")`). This is why we recommend the first solution.

### Force the same support library version across all dependencies
Some of your dependencies might require a different version of one of Google's support library packages. This results in compilation errors similar to this:

```
FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:preDebugBuild'.
> Android dependency 'com.android.support:design' has different version for the compile (25.4.0) and runtime (26.1.0) classpath. You should manually set the same version via DependencyResolution
```

To resolve these conflicts, add the following to your `app/build.gradle`:

```groovy
android {
    ...
}

configurations.all {
    resolutionStrategy.eachDependency { DependencyResolveDetails details ->
        def requested = details.requested
        if (requested.group == 'com.android.support' && requested.name != 'multidex') {
            details.useVersion "${rootProject.ext.supportLibVersion}"
        }
    }
}

dependencies {
    ...
    implementation 'com.android.support:design:25.4.0'
    implementation "com.android.support:appcompat-v7:${rootProject.ext.supportLibVersion}"
}

```
