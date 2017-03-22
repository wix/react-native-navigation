# Android Installation

### Important
The `latest` (stable) version of this framework is `1.x.x` which supports react-native `0.25.1`. It's installation instructions are [here](https://github.com/wix/react-native-navigation/blob/v1.x.x/README.md#installation---android). To use it specify `"react-native-navigation": "latest"` in your package.json dependencies.

The following instructions are for the `next` version `2.0.0-experimental.x`, which supports react-native `0.37.0`. To use it specify `"react-native-navigation": "next"` in your package.json dependencies. Bear in mind, as the name of the version suggests - this version is experimental and under heavy development, and will break from time to time - thus when using it you should check out these instructions from time to time to avoid breaking your project. 

----

* Make sure you are using react-native version 0.37.0

 >Note: Android adaptation is still under active development therfore the API might break from time to time. 
 
1.  Add the following to your `settings.gradle` file located in the `android` folder:

	```groovy
	include ':react-native-navigation'
	project(':react-native-navigation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-navigation/android/app/')
	```
	
2. Update project dependencies in `build.gradle` under `app` folder.

	```groovy
	dependencies {
	    compile fileTree(dir: "libs", include: ["*.jar"])
	    compile "com.android.support:appcompat-v7:23.0.1"
	    compile "com.facebook.react:react-native:+"
	    compile project(':react-native-navigation')
}
```

3. Your `MainActivity` should extend `com.reactnativenavigation.controllers.SplashActivity` instead of `ReactActivity`. If you have any `react-native` related methods in your `MainActivity` you can safely delete them.

4. Update the MainApplication file and update the `Application` element in `AndroidManifest.xml`
	
	```java
	import com.reactnativenavigation.NavigationApplication;
	
	public class MainApplication extends NavigationApplication {
	
	}
	```
	
	```xml
	<application
        android:name=".MainApplication"
        ...
        />
	```
5. Implement `isDebug` and `createAdditionalReactPackages` methods

	```java
	import com.reactnativenavigation.NavigationApplication;
	
	public class MyApplication extends NavigationApplication {
 
    	@Override
		public boolean isDebug() {
			// Make sure you are using BuildConfig from your own application
			return BuildConfig.DEBUG;
		}

	    @NonNull
	    @Override
	    public List<ReactPackage> createAdditionalReactPackages() {
		    // Add the packages you require here.
			// No need to add RnnPackage and MainReactPackage
	        return null;
	    }
	}
	```
6. Run react-native start