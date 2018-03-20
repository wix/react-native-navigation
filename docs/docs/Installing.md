# Installing

## Requirements
* node >= 8
* react-native >= 0.51

## npm
* `npm install --save react-native-navigation@alpha`

## iOS

> Make sure your Xcode is updated. We recommend editing `.h` and `.m` files in Xcode as the IDE will usually point out common errors.

1. In Xcode, in Project Navigator (left pane), right-click on the `Libraries` > `Add files to [project name]`. Add `node_modules/react-native-navigation/lib/ios/ReactNativeNavigation.xcodeproj` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#manual-linking)).

2. In Xcode, in Project Navigator (left pane), click on your project (top), then click on your *target* row (on the "project and targets list", which is on the left column of the right pane) and select the `Build Phases` tab (right pane). In the `Link Binary With Libraries` section add `libReactNativeNavigation.a` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2)).

3. In Xcode, you will need to edit this file: `AppDelegate.m`. This function is the main entry point for your app:

	```objectivec
	 - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions { ... }

	```
  
  Its contents must be replaced with the contents of this [reference](https://github.com/wix/react-native-navigation/blob/v2/playground/ios/playground/AppDelegate.m#L16).
  

## Android

> Make sure your Android Studio installation is updated. We recommend editing `gradle` and `java` files in Android Studio as the IDE will suggest fixes and point out errors, this way you avoid most common pitfalls.

1. Add the following in `android/settings.gradle`:

	```groovy
	include ':react-native-navigation'
	project(':react-native-navigation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-navigation/lib/android/app/')
	```

2. Update `android/build.gradle`:

	```diff
	buildscript {
	    repositories {
	+        google()
	+        mavenLocal()
	+        mavenCentral()
	+        jcenter()
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
	+		mavenLocal()
			jcenter()
			maven {
	           // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
	           url "$rootDir/../../node_modules/react-native/android"
			}
		}
	}
	```

3. Update project dependencies in `android/app/build.gradle`.

	```groovy
	android {
	    compileSdkVersion 25
	    buildToolsVersion "27.0.3"
	    
	    defaultConfig {
	        minSdkVersion 19
	        targetSdkVersion 25
		...
	    }
	
	    compileOptions {
	        sourceCompatibility JavaVersion.VERSION_1_8
	        targetCompatibility JavaVersion.VERSION_1_8
	    }
	    ...
	}
	
	dependencies {
	    implementation fileTree(dir: "libs", include: ["*.jar"])
	    implementation "com.android.support:appcompat-v7:25.4.0"
	    implementation "com.facebook.react:react-native:+"
	    implementation project(':react-native-navigation')
	}
	```
	
4. Make sure you're using the new gradle plugin, edit `android/gradle/wrapper/gradle-wrapper.properties`

	```diff
	distributionBase=GRADLE_USER_HOME
	distributionPath=wrapper/dists
	zipStoreBase=GRADLE_USER_HOME
	zipStorePath=wrapper/dists
	+distributionUrl=https\://services.gradle.org/distributions/gradle-4.4-all.zip
	-distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip
	```

5. Update `gradle.properties` and disable incremental resource processing

	```diff
	+# Disable incremental resource processing as it broke relase build
	+android.enableAapt2=false
	```

6. In `MainActivity.java` it should extend `com.reactnativenavigation.NavigationActivity` instead of `ReactActivity`.

	This file can be located in `android/app/src/main/java/com/yourproject/`.

	```java
	import com.reactnativenavigation.NavigationActivity;

	public class MainActivity extends NavigationActivity {

	}
	```

	If you have any **react-native** related methods, you can safely delete them.

7. In `MainApplication.java`, add the following
	
	```java
	import com.reactnativenavigation.NavigationApplication;

	public class MainApplication extends NavigationApplication {
		@Override
		public boolean isDebug() {
			return BuildConfig.DEBUG;
		}

		@Override
		public List<ReactPackage> createAdditionalReactPackages() {
			return Arrays.<ReactPackage>asList(
				// eg. new VectorIconsPackage()
			);
		}
 	}
	```
    Make sure that `isDebug` method is implemented.

8. Update `AndroidManifest.xml` and set `application` **android:name** value to `.MainApplication`
	
	```xml
	<application
		android:name=".MainApplication"
		...
	/>
	```
