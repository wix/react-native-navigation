# example

A simple usage example. If you're using redux, take a look at [example-redux](../example-redux).

## Installation - iOS

* In the `example/` folder, run `npm install`

> Make sure you're using npm ver 3. If you normally use npm ver 2 on your system and reluctant to upgrade, you can install [npm 3 alongside 2](https://www.npmjs.com/package/npm3). For more details see https://github.com/wix/react-native-navigation/issues/1

* Open `example/ios/example.xcodeproj` in Xcode and press the play button

## Installation - Android

* In the `example/` folder, run `npm install`

* In the `example/android` folder, place a `local.properties` file that contains the path to your local Android SDK directory (the one with platform & build tool directories within it).

For example, this would be the content of your `local.properties` file if your local Android SDK directory is `C:\Android\SDK`:

> sdk.dir=C\:\\Android\\SDK

* Ensure that you either have an Android device physically connected or a virtual device setup.

> `adb devices` should list your device.

* In the `example/` folder, run `react-native run-android` to build and launch the Android version of the App.
