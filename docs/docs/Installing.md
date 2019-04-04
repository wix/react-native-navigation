# Installing

## Requirements
* node >= 8
* react-native >= 0.51

## npm
* `npm install --save react-native-navigation`

## iOS

> Make sure your Xcode is updated. We recommend editing `.h` and `.m` files in Xcode as the IDE will usually point out common errors.

1. In Xcode, in Project Navigator (left pane), right-click on the `Libraries` > `Add files to [project name]`. Add `node_modules/react-native-navigation/lib/ios/ReactNativeNavigation.xcodeproj` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#manual-linking)).

2. In Xcode, in Project Navigator (left pane), click on your project (top), then click on your *target* row (on the "project and targets list", which is on the left column of the right pane) and select the `Build Phases` tab (right pane). In the `Link Binary With Libraries` section add `libReactNativeNavigation.a` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2)).

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

3a. If, in Xcode, you see the following error message in `AppDelegate.m` next to `#import "RCTBundleURLProvider.h"`: 
```
! 'RCTBundleURLProvider.h' file not found
```
This is because the `React` scheme is missing from your project. You can verify this by opening the `Product` menu and the `Scheme` submenu. 

To make the `React` scheme available to your project, run `npm install -g react-native-git-upgrade` followed by `react-native-git-upgrade`. Once this is done, you can click back to the menu in Xcode: `Product -> Scheme -> Manage Schemes`, then click '+' to add a new scheme. From the `Target` menu, select "React", and click the checkbox to make the scheme `shared`. This should make the error disappear.

3b. If, in Xcode, you see the following warning message in `AppDelegate.m` next to `#import "@implementation AppDelegate"`:
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

## Android

> Make sure your Android Studio installation is updated. We recommend editing `gradle` and `java` files in Android Studio as the IDE will suggest fixes and point out errors, this way you avoid most common pitfalls.

### 1. Add the following in `android/settings.gradle`:

```groovy
include ':react-native-navigation'
project(':react-native-navigation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-navigation/lib/android/app/')
```

### 2. Make sure you're using the new gradle plugin, edit `android/gradle/wrapper/gradle-wrapper.properties`

```diff
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
+distributionUrl=https\://services.gradle.org/distributions/gradle-4.4-all.zip
-distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip
```

### 3 Update `android/build.gradle`:

```diff
buildscript {
	repositories {
+        google()
+        mavenLocal()
+        mavenCentral()
+        jcenter()
-        maven {
-            url 'https://maven.google.com/'
-            name 'Google'
-        }
	}
	dependencies {
+        classpath 'com.android.tools.build:gradle:3.0.1'
-        classpath 'com.android.tools.build:gradle:2.2.3'
	}
}

allprojects {
	repositories {
+		google()
+		mavenCentral()
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

ext {
-    buildToolsVersion = "26.0.3"
+    buildToolsVersion = "27.0.3"
-    minSdkVersion = 16
+    minSdkVersion = 19
    compileSdkVersion = 26
    targetSdkVersion = 26
    supportLibVersion = "26.1.0"
}


```

### 4 Update project dependencies in `android/app/build.gradle`.

```diff
android {
    compileSdkVersion rootProject.ext.compileSdkVersion
    buildToolsVersion rootProject.ext.buildToolsVersion

    defaultConfig {
        applicationId "com.yourproject"
        minSdkVersion rootProject.ext.minSdkVersion
        targetSdkVersion rootProject.ext.targetSdkVersion
        versionCode 1
        versionName "1.0"
        ndk {
            abiFilters "armeabi-v7a", "x86"
        }
    }
+	compileOptions {
+		sourceCompatibility JavaVersion.VERSION_1_8
+		targetCompatibility JavaVersion.VERSION_1_8
+	}
	...
}

dependencies {
-    compile fileTree(dir: "libs", include: ["*.jar"])
-    compile "com.android.support:appcompat-v7:${rootProject.ext.supportLibVersion}"
-    compile "com.facebook.react:react-native:+"  // From node_modules
+    implementation fileTree(dir: "libs", include: ["*.jar"])
+    implementation "com.android.support:appcompat-v7:${rootProject.ext.supportLibVersion}"
+    implementation "com.facebook.react:react-native:+"  // From node_modules
+    implementation project(':react-native-navigation')
}
```

### 5 RNN and React Native version

react-native-navigation supports multiple React Native versions. Target the React Native version required by your project by specifying the RNN build flavor in `android/app/build.gradle`.

```diff
android {
    ...
    defaultConfig {
        applicationId "com.yourproject"
        minSdkVersion rootProject.ext.minSdkVersion
        targetSdkVersion rootProject.ext.targetSdkVersion
+        missingDimensionStrategy "RNN.reactNativeVersion", "reactNative57" // See note below!
        versionCode 1
        versionName "1.0"
        ...
    }
    ...
}
```

!>Important note about `missingDimensionStrategy`<Br>
>`reactNative51` - RN 0.54.x and below<Br>
>`reactNative55` - RN 0.55.x<Br>
>`reactNative56` - RN 0.56.x<Br>
>`reactNative57` - RN 0.57.0 - 0.57.4<Br>
>`reactNative57_5` - RN 0.57.5 and above<Br>

Now we need to instruct gradle how to build that flavor. To do so here two solutions:

#### 5.1 Build app with gradle command 

**prefered solution** The RNN flavor you would like to build is specified in `app/build.gradle`. Therefore in order to compile only that flavor, instead of building your entire project using `./gradlew assembleDebug`, you should instruct gradle to build the app module: `./gradlew app:assembleDebug`. The easiest way is to add a package.json command to build and install your debug Android APK .

```
"scripts": {
  ...
  "android": "cd ./android && ./gradlew app:assembleDebug && ./gradlew installDebug"
}
```

Now run `npm run android` to build your application

#### 5.2 Ignore other RNN flavors

If you don't want to run `npm run android` and want to keep the default `react-native run-android` command, you need to specify to graddle to ignore the other flavors RNN provides.

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


### 6. Update `MainActivity.java`

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

### 7. Update `MainApplication.java`

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
+import com.reactnativenavigation.react.ReactGateway;

import java.util.Arrays;
import java.util.List;

-public class MainApplication extends Application implements ReactApplication {
+public class MainApplication extends NavigationApplication {
+    
+    @Override
+    protected ReactGateway createReactGateway() {
+        ReactNativeHost host = new NavigationReactNativeHost(this, isDebug(), createAdditionalReactPackages()) {
+            @Override
+            protected String getJSMainModuleName() {
+                return "index";
+            }
+        };
+        return new ReactGateway(this, isDebug(), host);
+    }
+
+    @Override
+    public boolean isDebug() {
+        return BuildConfig.DEBUG;
+    }
+
+    protected List<ReactPackage> getPackages() {
+        // Add additional packages you require here
+        // No need to add RnnPackage and MainReactPackage
+        return Arrays.<ReactPackage>asList(
+            // eg. new VectorIconsPackage()
+        );
+    }
+  
+    @Override
+    public List<ReactPackage> createAdditionalReactPackages() {
+        return getPackages();
+    }
- ...
+}

```

### 8. Force the same support library version across all dependencies (optional)

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
