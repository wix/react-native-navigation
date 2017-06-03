# Contributing
## Folder Structure

| Folder | Description |
| ------ | ----------- |
| `lib` | The project itself composed of: |
| `lib/android` | android sources and unit tests |
| `lib/ios` | iOS sources and unit tests |
| `lib/src` | javascript sources and unit tests |
| `lib/src/index.js` | the entry point for `import Navigation from 'react-native-navigation'` |
| `e2e` | [detox](https://github.com/wix/detox) iOS e2e tests (in the future, once detox supports it, we will have android e2e here as well) |
| `AndroidE2E` | Android e2e tests using native uiautomator (until detox for android is ready) |
| `playground` | The end-user project all e2e tests run against. Contains its own `src`, `android` and `ios`. Does not have its own package.json, depends on the local `<root>/lib` in order not to go through npm. |
| `integration` | misc javascript integration tests |
| `scripts` | all scripts |

## Running locally

### Environment requirements

* Mac OSX
* Latest stable XCode
* Latest stable Node:

```
curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.1/install.sh | bash
export NVM_DIR="$HOME/.nvm" && [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
nvm install stable
nvm use stable
```

* Latest stable Yarn:

```
curl -o- -L https://yarnpkg.com/install.sh | bash
export PATH=$PATH:$HOME/.yarn/bin
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

* fbsimctl:

```
export CODE_SIGNING_REQUIRED=NO
brew tap facebook/fb && brew install fbsimctl
```

### Scripts

| Command | Description |
| ------- | ----------- |
| `yarn install` | installs dependencies |
| `yarn clean` | cleans all build directories, stops packager, fixes flakiness by removing watchman cache, etc. |
| `yarn start` | starts the react-native packager for local debugging |
| `yarn xcode` | for convenience, opens xcode in this project |
| `yarn install-android`  |  builds playground debug/release version and installs on running android devices/emulators. <br> **Options:** `-- release` |
| `yarn uninstall-android` | uninstalls playground from running android devices/simulators |
| `yarn test-js` | runs javascript tests and coverage report |
| `yarn test-watch` | runs javascript tests in watch mode (can also use the provided wallaby config) |
| `yarn test-unit-ios` | runs ios unit tests in debug/release <br> **Options:** `-- release` |
| `yarn test-unit-android` | runs android unit tests in debug/release <br> **Options:** `-- release` |
| `yarn test-e2e-ios` | runs the ios e2e suite (with detox) in debug/release |
| `yarn test-e2e-android` | runs the android e2e suite (with uiautomator) in debug/release on running devices/emulators <br> **Options:** `-- release` |
| `yarn test-all` | runs all tests in parallel |

## Workflow
This project is driven by tests. Before implementing any feature or fixing any bug, a failing test (e2e or unit or both) should be added, depending on the environment of where the fix should be implemented. For example, for an API change, a failing e2e should be written. For a small bug fix in Android, for example, a unit test in Android should be added.

This will ensure good quality throughout the life of the project and will avoid unexpected breakages.

No PR will be accepted without adequate test coverage.

To run the tests, use the scripts above.

