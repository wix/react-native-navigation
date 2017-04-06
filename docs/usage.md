# Usage 

If you don't like reading, just jump into the fully working example projects:

* [example](https://github.com/wix/react-native-navigation/tree/master/example) - Example project showing the best practice use of this package. Shows many navigation features.
* [redux-example](https://github.com/wix/react-native-navigation/tree/master/old-example-redux) - (**deprecated** in favor of [JuneDomingo/movieapp](https://github.com/JuneDomingo/movieapp/tree/feature/similar-movies)) Best practice use of this package in a [redux](https://github.com/reactjs/redux)-based project.

> Note: example redux is deprecated. Since we did not have enough time and resources to maintain both example projects, we decided to stop maintaining the redux example. This does not mean redux can't be used with react-native-navigation (In fact, we use redux in the Wix app). For a working example project which uses redux with RNN you can refer to [JuneDomingo/movieapp](https://github.com/JuneDomingo/movieapp).

#### Step 1 - Change the way your app starts

This would normally go in your `index.ios.js`

```js
import { Navigation } from 'react-native-navigation';

import { registerScreens } from './screens';
registerScreens(); // this is where you register all of your app's screens

// start the app
Navigation.startTabBasedApp({
  tabs: [
    {
      label: 'One',
      screen: 'example.FirstTabScreen', // this is a registered name for a screen
      icon: require('../img/one.png'),
      selectedIcon: require('../img/one_selected.png'), // iOS only
      title: 'Screen One'
    },
    {
      label: 'Two',
      screen: 'example.SecondTabScreen',
      icon: require('../img/two.png'),
      selectedIcon: require('../img/two_selected.png'), // iOS only
      title: 'Screen Two'
    }
  ]
});
```

#### Step 2 - Register all of your screen components

Every screen that you want to be able to place in a tab, push to the navigation stack or present modally needs to be registered. We recommend doing this in a central place, like [`screens/index.js`](example/src/screens/index.js).

> Note: Since your screens will potentially be bundled with other packages, your registered name must be **unique**! Follow a namespacing convention like `packageName.ScreenName`.

```js
import { Navigation } from 'react-native-navigation';

import FirstTabScreen from './FirstTabScreen';
import SecondTabScreen from './SecondTabScreen';
import PushedScreen from './PushedScreen';

// register all screens of the app (including internal ones)
export function registerScreens() {
  Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen);
  Navigation.registerComponent('example.SecondTabScreen', () => SecondTabScreen);
  Navigation.registerComponent('example.PushedScreen', () => PushedScreen);
}
```

#### Step 3 - That's it

If you want to do a navigation action like push a new screen over an existing one, take a look at the [Screen API](#screen-api). It would look something like this:

```js
// this would go inside the Component implementation of one of your screens, like FirstTabScreen.js
this.props.navigator.push({
  screen: 'example.PushedScreen',
  title: 'Pushed Screen'
});
```