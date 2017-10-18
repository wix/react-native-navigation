# Android Installation

1. Install `react-native-navigation` latest stable version.

	```sh
	yarn add react-native-navigation@alpha
	```

2. Add the following in `android/settings.gradle`.

	```groovy
	include ':react-native-navigation'
	project(':react-native-navigation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-navigation/lib/android/app/')
	```

3. Update project dependencies in `android/app/build.gradle`.
	```groovy
	android {
		compileSdkVersion 25
		buildToolsVersion "25.0.1"
		...
	}

	dependencies {
		compile fileTree(dir: "libs", include: ["*.jar"])
		compile "com.android.support:appcompat-v7:23.0.1"
		compile "com.facebook.react:react-native:+"
		compile project(':react-native-navigation')
	}
	```

4. In `MainActivity.java` it should extend `com.reactnativenavigation.NavigationActivity` instead of `ReactActivity`.

	This file can be located in `android/app/src/main/java/com/yourproject/`.

	```java
	import com.reactnativenavigation.NavigationActivity;

	public class MainActivity extends NavigationActivity {

	}
	```

	If you have any **react-native** related methods, you can safely delete them.

5. In `MainApplication.java`, add the following
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

6. Update `AndroidManifest.xml` and set **android:name** value to `.MainApplication`
	```xml
	<application
		android:name=".MainApplication"
		...
	/>
	```
