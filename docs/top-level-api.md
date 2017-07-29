# Top Level API

## Navigation

```js
import { Navigation } from 'react-native-navigation';
```

## registerComponent(screenID, generator, store = undefined, Provider = undefined)

Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending `React.Component`.

```js
// not using redux (just ignore the last 2 arguments)
Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen);

// using redux, pass your store and the Provider object from react-redux
Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen, store, Provider);
```

## startTabBasedApp(params)

Change your app root into an app based on several tabs (usually 2-5), a very common pattern in iOS (like Facebook app or the iOS Contacts app). Every tab has its own navigation stack with a native nav bar.

```js
Navigation.startTabBasedApp({
  tabs: [
    {
      label: 'One', // tab label as appears under the icon in iOS (optional)
      screen: 'example.FirstTabScreen', // unique ID registered with Navigation.registerScreen
      icon: require('../img/one.png'), // local image asset for the tab icon unselected state (optional on iOS)
      selectedIcon: require('../img/one_selected.png'), // local image asset for the tab icon selected state (optional, iOS only. On Android, Use `tabBarSelectedButtonColor` instead)
      iconInsets: { // add this to change icon position (optional, iOS only).
        top: 6, // optional, default is 0.
        left: 0, // optional, default is 0.
        bottom: -6, // optional, default is 0.
        right: 0 // optional, default is 0.
      },
      title: 'Screen One', // title of the screen as appears in the nav bar (optional)
      titleImage: require('../img/titleImage.png'), // iOS only. navigation bar title image instead of the title text of the pushed screen (optional)
      navigatorStyle: {}, // override the navigator style for the tab screen, see "Styling the navigator" below (optional),
      navigatorButtons: {} // override the nav buttons for the tab screen, see "Adding buttons to the navigator" below (optional)
    },
    {
      label: 'Two',
      screen: 'example.SecondTabScreen',
      icon: require('../img/two.png'),
      selectedIcon: require('../img/two_selected.png'),
      title: 'Screen Two'
    }
  ],
  tabsStyle: { // optional, add this if you want to style the tab bar beyond the defaults
    tabBarButtonColor: '#ffff00', // optional, change the color of the tab icons and text (also unselected)
    tabBarSelectedButtonColor: '#ff9900', // optional, change the color of the selected tab icon and text (only selected)
    tabBarBackgroundColor: '#551A8B' // optional, change the background color of the tab bar
  },
  appStyle: {
    orientation: 'portrait' // Sets a specific orientation to the entire app. Default: 'auto'. Supported values: 'auto', 'landscape', 'portrait'
  },
  drawer: { // optional, add this if you want a side menu drawer in your app
    left: { // optional, define if you want a drawer from the left
      screen: 'example.FirstSideMenu', // unique ID registered with Navigation.registerScreen
      passProps: {} // simple serializable object that will pass as props to all top screens (optional)
    },
    right: { // optional, define if you want a drawer from the right
      screen: 'example.SecondSideMenu', // unique ID registered with Navigation.registerScreen
      passProps: {} // simple serializable object that will pass as props to all top screens (optional)
    },
    disableOpenGesture: false // optional, can the drawer be opened with a swipe instead of button
  },
  passProps: {}, // simple serializable object that will pass as props to all top screens (optional)
  animationType: 'slide-down' // optional, add transition animation to root change: 'none', 'slide-down', 'fade'
});
```

## startSingleScreenApp(params)

Change your app root into an app based on a single screen (like the iOS Calendar or Settings app). The screen will receive its own navigation stack with a native nav bar

```js
Navigation.startSingleScreenApp({
  screen: {
    screen: 'example.WelcomeScreen', // unique ID registered with Navigation.registerScreen
    title: 'Welcome', // title of the screen as appears in the nav bar (optional)
    navigatorStyle: {}, // override the navigator style for the screen, see "Styling the navigator" below (optional)
    navigatorButtons: {} // override the nav buttons for the screen, see "Adding buttons to the navigator" below (optional)
  },
  drawer: { // optional, add this if you want a side menu drawer in your app
    left: { // optional, define if you want a drawer from the left
      screen: 'example.FirstSideMenu', // unique ID registered with Navigation.registerScreen
      passProps: {}, // simple serializable object that will pass as props to all top screens (optional)
    },
    right: { // optional, define if you want a drawer from the right
      screen: 'example.SecondSideMenu', // unique ID registered with Navigation.registerScreen
      passProps: {} // simple serializable object that will pass as props to all top screens (optional)
    },
    disableOpenGesture: false // optional, can the drawer be opened with a swipe instead of button
  },
  passProps: {}, // simple serializable object that will pass as props to all top screens (optional)
  animationType: 'slide-down' // optional, add transition animation to root change: 'none', 'slide-down', 'fade'
});
```

## showModal(params = {})

Show a screen as a modal.

```js
Navigation.showModal({
  screen: "example.ModalScreen", // unique ID registered with Navigation.registerScreen
  title: "Modal", // title of the screen as appears in the nav bar (optional)
  passProps: {}, // simple serializable object that will pass as props to the modal (optional)
  navigatorStyle: {}, // override the navigator style for the screen, see "Styling the navigator" below (optional)
  navigatorButtons: {}, // override the nav buttons for the screen, see "Adding buttons to the navigator" below (optional)
  animationType: 'slide-up' // 'none' / 'slide-up' , appear animation for the modal (optional, default 'slide-up')
});
```

## dismissModal(params = {})

Dismiss the current modal.

```js
Navigation.dismissModal({
  animationType: 'slide-down' // 'none' / 'slide-down' , dismiss animation for the modal (optional, default 'slide-down')
});
```

## dismissAllModals(params = {})

Dismiss all the current modals at the same time.

```js
Navigation.dismissAllModals({
  animationType: 'slide-down' // 'none' / 'slide-down' , dismiss animation for the modal (optional, default 'slide-down')
});
```

## showLightBox(params = {})

Show a screen as a lightbox.

```js
Navigation.showLightBox({
  screen: "example.LightBoxScreen", // unique ID registered with Navigation.registerScreen
  passProps: {}, // simple serializable object that will pass as props to the lightbox (optional)
  style: {
    backgroundBlur: "dark", // 'dark' / 'light' / 'xlight' / 'none' - the type of blur on the background
    backgroundColor: "#ff000080", // tint color for the background, you can specify alpha here (optional)
    tapBackgroundToDismiss: true // dismisses LightBox on background taps (optional)
  }
});
```

## dismissLightBox(params = {})

Dismiss the current lightbox.

```js
Navigation.dismissLightBox();
```

## registerScreen(screenID, generator)

This is an internal function you probably don't want to use directly. If your screen components extend `Screen` directly (`import { Screen } from 'react-native-navigation'`), you can register them directly with `registerScreen` instead of with `registerComponent`. The main benefit of using `registerComponent` is that it wraps your regular screen component with a `Screen` automatically.

```js
Navigation.registerScreen('example.AdvancedScreen', () => AdvancedScreen);
```

## getCurrentlyVisibleScreenId()
In some cases you might need the id of the currently visible screen. This method returns the unique id of the currently visible screen:
`const visibleScreenInstanceId = await Navigation.getCurrentlyVisibleScreenId()`
In order to have any use of this method, you'd need to map instanceId to screens your self.
