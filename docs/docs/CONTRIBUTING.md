# Contributing

Thanks for you interest in helping out! We'd love your contributions, and there's plenty of work to be done regardless of your skill level. Before you start, you'll need to have some things installed in your environment so that you can run locally.

## Environment Requirements

* Mac OSX
* Latest stable XCode
* Latest stable Node:

```
curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.1/install.sh | bash
export NVM_DIR="$HOME/.nvm" && [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
nvm install stable
nvm use stable
```

* Android SDK:

```
export ANDROID_HOME=$HOME/android-sdk-macosx
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# fix for https://code.google.com/p/android/issues/detail?id=223424
mkdir -p ~/.android

# download android SDK
echo "Downloading Android SDK"
curl --location https://dl.google.com/android/android-sdk_r24.4.1-macosx.zip | tar -x -z -C $HOME
```

* [AppleSimulatorUtils](https://github.com/wix/AppleSimulatorUtils)

```
brew tap wix/brew
brew install --HEAD applesimutils
```

## Basics - Getting Started

Got your environment set up? Go ahead and clone the repo. (Fork it first so you can open a PR when you're ready.)

Then:

1. Install dependencies:

    ```
    npm install
    ```

1. Run the playground project in Android and iOS so that you can get a feel for the project.

    1. `npm run start` to get the package running in a terminal, leave it open

    1. iOS: `npm run xcode` & run the project from XCode

    1. Android: Open the app in Android Studio and click `Run`

1. Run the tests. Before you start changing things, make sure everything works.

     ```
     npm run test-all
     ```

## Troubleshooting

* If the tests fail with an error like `Ineligible destinations for the "ReactNativeNavigation" scheme`, double check that you have the latest XCode installed.
* If the tests fail because an Android emulator isn't available (something like `com.android.builder.testing.api.DeviceException: No connected devices!`), start the Android project from Android Studio and leave the emulator running, then try again.
* If the tests fail with an error such as:

```js

 beforeEach(async () => {
                   ^
SyntaxError: Unexpected token (

```

You probably have an old node version which doesn't support async functions. Update your node using nvm according to the instructions above.

## Workflow
This project is driven by tests. Before implementing any feature or fixing any bug, a failing test (e2e or unit or both) should be added, depending on the environment of where the fix should be implemented. For example, for an API change, a failing e2e should be written. For a small bug fix in Android, for example, a unit test in Android should be added.

This will ensure good quality throughout the life of the project and will avoid unexpected breakages.

No PR will be accepted without adequate test coverage.

## Folder Structure

| Folder | Description |
| ------ | ----------- |
| `lib` | The project itself composed of: |
| `lib/android` | android sources and unit tests |
| `lib/ios` | iOS sources and unit tests |
| `lib/src` | TypeScript sources and unit tests |
| `lib/dist` | compiled javascript sources and unit tests |
| `lib/dist/index.js` | the entry point for `import Navigation from 'react-native-navigation'` |
| `e2e` | [detox](https://github.com/wix/detox) iOS e2e tests (in the future, once detox supports it, we will have android e2e here as well) |
| `AndroidE2E` | Android e2e tests using native uiautomator (until detox for android is ready) |
| `playground` | The end-user project all e2e tests run against. Contains its own `src`, `android` and `ios`. Does not have its own package.json, depends on the local `<root>/lib` in order not to go through npm. |
| `integration` | misc javascript integration tests |
| `scripts` | all scripts |

## Scripts

| Command | Description |
| ------- | ----------- |
| `npm install` | installs dependencies |
| `npm run build` | compiles TypeScript sources `./lib/src` into javascript `./lib/dist` |
| `npm run clean` | cleans all build directories, stops packager, fixes flakiness by removing watchman cache, etc. |
| `npm run start` | starts the react-native packager for local debugging |
| `npm run xcode` | for convenience, opens xcode in this project |
| `npm run install-android`  |  builds playground debug/release version and installs on running android devices/emulators. <br> **Options:** `-- --release` |
| `npm run uninstall-android` | uninstalls playground from running android devices/simulators |
| `npm run test-js` | runs javascript tests and coverage report |
| `npm run test-unit-ios` | runs ios unit tests in debug/release <br> **Options:** `-- --release` |
| `npm run test-unit-android` | runs android unit tests in debug/release <br> **Options:** `-- --release` |
| `npm run test-e2e-ios` | runs the ios e2e suite (with detox) in debug/release <br> **Options:** `-- --release`|
| `npm run test-e2e-android` | runs the android e2e suite (with uiautomator) in debug/release on running devices/emulators <br> **Options:** `-- [--release] [--just com.TestClass#testMethod]` |
| `npm run test-all` | runs all tests in parallel |

