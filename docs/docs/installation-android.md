# Android Installation

!> Make sure your Android Studio installation is updated. We recommend editing `gralde` and `java` files in Android Studio as the ide will suggest fixes and point out errors, this way you avoid most common pitfalls.


1. Install `react-native-navigation` latest stable version.

	```sh
	yarn add react-native-navigation@alpha
	```

2. Add the following in `android/settings.gradle`.

	```groovy
	include ':react-native-navigation'
	project(':react-native-navigation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-navigation/lib/android/app/')
	```
	
3. Update `android/build.gradle`

	```diff
	buildscript {
	    repositories {
	+        mavenLocal()
	+        mavenCentral()
	+        google()
	+        jcenter()
	    }
	    dependencies {
	+        classpath 'com.android.tools.build:gradle:3.0.1'
	-        classpath 'com.android.tools.build:gradle:2.2.3'
	    }
	}
	
	allprojects {
	    repositories {
	        mavenLocal()
	+        mavenCentral()
	+        google()
	        jcenter()
	        maven {
	            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
	            url "$rootDir/../../node_modules/react-native/android"
	        }
	    }
	}
	```

4. Update project dependencies in `android/app/build.gradle`.

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
	
5. Make sure you're using the new gradle plugin, edit `android/gradle/wrapper/gradle-wrapper.properties`

	```diff
	distributionBase=GRADLE_USER_HOME
	distributionPath=wrapper/dists
	zipStoreBase=GRADLE_USER_HOME
	zipStorePath=wrapper/dists
	+distributionUrl=https\://services.gradle.org/distributions/gradle-4.4-all.zip
	-distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip
	```

6. Update `gradle.properties` and disable incremental resource processing

	```diff
	+# Disable incremental resource processing as it broke relase build
	+android.enableAapt2=false
	```

7. In `MainActivity.java` it should extend `com.reactnativenavigation.NavigationActivity` instead of `ReactActivity`.

	This file can be located in `android/app/src/main/java/com/yourproject/`.

	```java
	import com.reactnativenavigation.NavigationActivity;

	public class MainActivity extends NavigationActivity {

	}
	```

	If you have any **react-native** related methods, you can safely delete them.

8. In `MainApplication.java`, add the following
	
	```java
	import com.reactnativenavigation.NavigationApplication;

	public class MainApplication extends NavigationApplication {
     @Override
	public boolean isDebug() {
		return BuildConfig.DEBUG;
	}

	@Nullable
	@Override
	public List<ReactPackage> createAdditionalReactPackages() {
		return Arrays.<ReactPackage>asList(
			// eg. new VectorIconsPackage()
		);
	}
 	}
	```

	Make sure that `isDebug` methods is implemented.

9. Update `AndroidManifest.xml` and set **android:name** value to `.MainApplication`
	
	```xml
	<application
		android:name=".MainApplication"
		...
	/>
	```
