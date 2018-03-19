# Working Locally

> Thanks for your interest in helping out! We'd love your contributions, and there's plenty of work to be done regardless of your skill level

## Environment Requirements

* Mac OSX
* [Latest stable Xcode](https://developer.apple.com/xcode/)
* [Android SDK](https://developer.android.com/studio/index.html)
* [Node 8+](https://nodejs.org/en/)
* [AppleSimulatorUtils](https://github.com/wix/AppleSimulatorUtils)

## Running The Project

Got your environment set up? Go ahead and clone the repo. (Fork it first so you can open a PR when you're ready.)

Then:

1. Install dependencies `npm install`
2. Run the playground project in Android and iOS so that you can get a feel for the project.
    - `npm run start` to get the packager running in a terminal, leave it open
    - **iOS**: `npm run xcode` & run the project from Xcode
    - **Android**: `npm run install-android`, or open the app in Android Studio and click `Run`
3. Run the tests (using the scripts below). Before you start changing things, make sure everything works.
	- To easily run all tests in parallel `npm run test-all`


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
| `e2e` | [detox](https://github.com/wix/detox) e2e tests on both Android and iOS |
| `playground` | The end-user project all e2e tests run against. Contains its own `src`, `android` and `ios`. Does not have its own package.json, depends on the local `<root>/lib` for faster local development (no need to `npm install` locally). |
| `integration` | misc javascript integration tests, proving integration with other libraries like redux |
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
| `npm run test-e2e-ios` | runs the ios e2e tests using [detox](https://github.com/wix/detox) in debug/release <br> **Options:** `-- --release`|
| `npm run test-e2e-android` | runs the android e2e tests using [detox](https://github.com/wix/detox) in debug/release <br> **Options:** `-- --release` |
| `npm run test-all` | runs all tests in parallel |
| `npm run gen-docs` | generates api docs |
| `npm run local-docs` | serve the docs locally for `http://localhost:3000/` |

## Common Problems

* If the tests fail with an error like `Ineligible destinations for the "ReactNativeNavigation" scheme`, double check that you have the latest XCode installed.
* If the tests fail because an Android emulator isn't available (something like `com.android.builder.testing.api.DeviceException: No connected devices!`), start the Android project from Android Studio and leave the emulator running, then try again.
* If the tests fail with an error such as:
		
	```js
	 beforeEach(async () => {
	                   ^
	SyntaxError: Unexpected token (
	```
		
	You probably have an old node version which doesn't support async functions. Update your nodejs to 8+.

