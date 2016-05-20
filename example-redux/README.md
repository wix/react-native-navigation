# example-redux

A simple usage example for an app based on redux. If you're not using redux, take a look at [example](../example) (without redux). The non-redux example will also include more examples of pure navigation features since we don't want to maintain two full examples. Make sure to look at it as well.

## Installation - iOS

* In the `example-redux/` folder, run `npm install`

> Make sure you're using npm ver 3. If you normally use npm ver 2 on your system and reluctant to upgrade, you can install [npm 3 alongside 2](https://www.npmjs.com/package/npm3). For more details see https://github.com/wix/react-native-navigation/issues/1. In addition, redux also causes some issues with npm 2. If you have to use npm 2 on this project, fix those by running `npm run fix_npm2` in your project directory after running `npm install`.

* Open `example-redux/ios/exampleRedux.xcodeproj` in Xcode and press the play button

## Folder Structure

* `src/screens/` - all your app screens, every screen is a redux connected ("smart") component
* `src/components/` - all react sub-components, they are not aware of redux ("dumb") and get everything by props
* `src/reducers/` - all of your redux goodness (reducts and their actions) goes here, all business logic should be here
