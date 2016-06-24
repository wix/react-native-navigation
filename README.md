# React Native Navigation

App-wide support for 100% native navigation with an easy cross-platform interface. For iOS, this package is a wrapper around [react-native-controllers](https://github.com/wix/react-native-controllers), but provides a simplified more abstract API over it. This abstract API will be unified with the Android solution which is currently work in progress. It also fully supports redux if you use it.

## Overview

* [Why use this package](#why-use-this-package)
* [Installation - iOS](#installation---ios)
* [Installation - Android](#installation---android)
* [Usage](#usage)
* [Top Level API](#top-level-api)
* [Screen API](#screen-api)
* [Styling the navigator](#styling-the-navigator)
* [Adding buttons to the navigator](#adding-buttons-to-the-navigator)
* [Styling the tab bar](#styling-the-tab-bar)
* [Deep links](#deep-links)
* [Third party libraries support](#third-party-libraries-support)
* [Release Notes](RELEASES.md)
* [License](#license)

## Why use this package

One of the major things missing from React Native core is fully featured native navigation. Navigation includes the entire skeleton of your app with critical components like nav bars, tab bars and side menu drawers.

If you're trying to deliver a user experience that's on par with the best native apps out there, you simply can't compromise on JS-based components trying to fake the real thing.

For example, this package replaces the native [NavigatorIOS](https://facebook.github.io/react-native/docs/navigatorios.html) that has been [abandoned](https://facebook.github.io/react-native/docs/navigator-comparison.html) in favor of JS-based solutions that are easier to maintain. For more details see in-depth discussion [here](https://github.com/wix/react-native-controllers#why-do-we-need-this-package).

## Installation - iOS

 * Make sure you are using react-native version >= 0.25.1

 * In your project folder run `npm install react-native-navigation --save`
> Note: We recommend using npm ver 3+. If you insist on using npm ver 2 please notice that the location for react-native-controllers in node_modules will be under the react-native-navigation folder and you'll need to change the paths in Xcode below accordingly.

 * Add the native files of the dependency [react-native-controllers](https://github.com/wix/react-native-controllers) to your Xcode project:

   * In Xcode, in Project Navigator (left pane), right-click on the `Libraries` > `Add files to [project name]`. Add `./node_modules/react-native-controllers/ios/ReactNativeControllers.xcodeproj` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-1))

   * In Xcode, in Project Navigator (left pane), click on your project (top) and select the `Build Phases` tab (right pane). In the `Link Binary With Libraries` section add `libReactNativeControllers.a` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2))

   * In Xcode, in Project Navigator (left pane), click on your project (top) and select the `Build Settings` tab (right pane). In the `Header Search Paths` section add `$(SRCROOT)/../node_modules/react-native-controllers/ios`. Make sure on the right to mark this new path `recursive` ([screenshots](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-3))

 * In Xcode, under your project files, modify `AppDelegate.m` according to this [example](https://github.com/wix/react-native-navigation/blob/master/example/ios/example/AppDelegate.m)

## Installation - Android

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
	    debugCompile project(path: ':react-native-navigation', configuration: 'libraryDebug')
	    releaseCompile project(path: ':react-native-navigation', configuration: 'libraryRelease')
}
```

3. Have your `MainActivity.java` extend `com.reactnativenavigation.activities.RootActivity`. 
`RootActivity` is used as a proxy activity to start your actuall app.

	The only method you might need to override is `getPackages()`, make sure you add `RnnPackage` as well.


## Usage

If you don't like reading, just jump into the fully working example projects:

* [example](example) - Example project showing the best practice use of this package. Shows many navigation features.
* [redux-example](example-redux) - Best practice use of this package in a [redux](https://github.com/reactjs/redux)-based project.

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
      selectedIcon: require('../img/one_selected.png'),
      title: 'Screen One'
    },
    {
      label: 'Two',
      screen: 'example.SecondTabScreen',
      icon: require('../img/two.png'),
      selectedIcon: require('../img/two_selected.png'),
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

## Top Level API

#### `Navigation`

```js
import { Navigation } from 'react-native-navigation';
```

 * **registerComponent(screenID, generator, store = undefined, Provider = undefined)**

Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending `React.Component`.

```js
// not using redux (just ignore the last 2 arguments)
Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen);

// using redux, pass your store and the Provider object from react-redux
Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen, store, Provider);
```

 * **startTabBasedApp(params)**

Change your app root into an app based on several tabs (usually 2-5), a very common pattern in iOS (like Facebook app or the iOS Contacts app). Every tab has its own navigation stack with a native nav bar.

```js
Navigation.startTabBasedApp({
  tabs: [
    {
      label: 'One', // tab label as appears under the icon in iOS (optional)
      screen: 'example.FirstTabScreen', // unique ID registered with Navigation.registerScreen
      icon: require('../img/one.png'), // local image asset for the tab icon unselected state (optional)
      selectedIcon: require('../img/one_selected.png'), // local image asset for the tab icon selected state (optional)
      title: 'Screen One', // title of the screen as appears in the nav bar (optional)
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
  drawer: { // optional, add this if you want a side menu drawer in your app
    left: { // optional, define if you want a drawer from the left
      screen: 'example.FirstSideMenu' // unique ID registered with Navigation.registerScreen
    },
    right: { // optional, define if you want a drawer from the right
      screen: 'example.SecondSideMenu' // unique ID registered with Navigation.registerScreen
    },
    disableOpenGesture: false // optional, can the drawer be opened with a swipe instead of button
  },
  passProps: {}, // simple serializable object that will pass as props to all top screens (optional)
  animationType: 'slide-down' // optional, add transition animation to root change: 'none', 'slide-down', 'fade'
});
```

 * **startSingleScreenApp(params)**

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
      screen: 'example.FirstSideMenu' // unique ID registered with Navigation.registerScreen
    },
    right: { // optional, define if you want a drawer from the right
      screen: 'example.SecondSideMenu' // unique ID registered with Navigation.registerScreen
    },
    disableOpenGesture: false // optional, can the drawer be opened with a swipe instead of button
  },
  passProps: {}, // simple serializable object that will pass as props to all top screens (optional)
  animationType: 'slide-down' // optional, add transition animation to root change: 'none', 'slide-down', 'fade'
});
```

 * **showModal(params = {})**

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

 * **dismissModal(params = {})**

Dismiss the current modal.

```js
Navigation.dismissModal({
  animationType: 'slide-down' // 'none' / 'slide-down' , dismiss animation for the modal (optional, default 'slide-down')
});
```

 * **dismissAllModals(params = {})**

Dismiss all the current modals at the same time.

```js
Navigation.dismissAllModals({
  animationType: 'slide-down' // 'none' / 'slide-down' , dismiss animation for the modal (optional, default 'slide-down')
});
```

* **showLightBox(params = {})**

Show a screen as a lightbox.

```js
Navigation.showLightBox({
  screen: "example.LightBoxScreen", // unique ID registered with Navigation.registerScreen
  passProps: {}, // simple serializable object that will pass as props to the lightbox (optional)
  style: {
    backgroundBlur: "dark", // 'dark' / 'light' / 'xlight' / 'none' - the type of blur on the background
    backgroundColor: "#ff000080" // tint color for the background, you can specify alpha here (optional)
  }
});
```

* **dismissLightBox(params = {})**

Dismiss the current lightbox.

```js
Navigation.dismissLightBox();
```

 * **registerScreen(screenID, generator)**

This is an internal function you probably don't want to use directly. If your screen components extend `Screen` directly (`import { Screen } from 'react-native-navigation'`), you can register them directly with `registerScreen` instead of with `registerComponent`. The main benefit of using `registerComponent` is that it wraps your regular screen component with a `Screen` automatically.

```js
Navigation.registerScreen('example.AdvancedScreen', () => AdvancedScreen);
```

## Screen API

This API is relevant when in a screen component context - it allows a screen to push other screens, pop screens, change its navigator style, etc. Access to this API is available through the `navigator` object that is passed to your component through `props`.

 * **push(params)**

Push a new screen into this screen's navigation stack.

```js
this.props.navigator.push({
  screen: 'example.ScreenThree', // unique ID registered with Navigation.registerScreen
  title: undefined, // navigation bar title of the pushed screen (optional)
  titleImage: require('../../img/my_image.png'), //navigation bar title image instead of the title text of the pushed screen (optional)
  passProps: {}, // simple serializable object that will pass as props to the pushed screen (optional)
  animated: true, // does the push have transition animation or does it happen immediately (optional)
  backButtonTitle: undefined, // override the back button title (optional)
  backButtonHidden: false, // hide the back button altogether (optional)
  navigatorStyle: {}, // override the navigator style for the pushed screen (optional)
  navigatorButtons: {} // override the nav buttons for the pushed screen (optional)
});
```

 * **pop(params = {})**

Pop the top screen from this screen's navigation stack.

```js
this.props.navigator.pop({
  animated: true // does the pop have transition animation or does it happen immediately (optional)
});
```

 * **popToRoot(params = {})**

Pop all the screens until the root from this screen's navigation stack.

```js
this.props.navigator.popToRoot({
  animated: true // does the pop have transition animation or does it happen immediately (optional)
});
```

 * **resetTo(params)**

Reset the screen's navigation stack to a new screen (the stack root is changed).

```js
this.props.navigator.resetTo({
  screen: 'example.ScreenThree', // unique ID registered with Navigation.registerScreen
  title: undefined, // navigation bar title of the pushed screen (optional)
  passProps: {}, // simple serializable object that will pass as props to the pushed screen (optional)
  animated: true, // does the push have transition animation or does it happen immediately (optional)
  navigatorStyle: {}, // override the navigator style for the pushed screen (optional)
  navigatorButtons: {} // override the nav buttons for the pushed screen (optional)
});
```

 * **showModal(params = {})**

Show a screen as a modal.

```js
this.props.navigator.showModal({
  screen: "example.ModalScreen", // unique ID registered with Navigation.registerScreen
  title: "Modal", // title of the screen as appears in the nav bar (optional)
  passProps: {}, // simple serializable object that will pass as props to the modal (optional)
  navigatorStyle: {}, // override the navigator style for the screen, see "Styling the navigator" below (optional)
  animationType: 'slide-up' // 'none' / 'slide-up' , appear animation for the modal (optional, default 'slide-up')
});
```

 * **dismissModal(params = {})**

Dismiss the current modal.

```js
this.props.navigator.dismissModal({
  animationType: 'slide-down' // 'none' / 'slide-down' , dismiss animation for the modal (optional, default 'slide-down')
});
```

 * **dismissAllModals(params = {})**

Dismiss all the current modals at the same time.

```js
this.props.navigator.dismissAllModals({
  animationType: 'slide-down' // 'none' / 'slide-down' , dismiss animation for the modal (optional, default 'slide-down')
});
```

* **showLightBox(params = {})**

Show a screen as a lightbox.

```js
this.props.navigator.showLightBox({
 screen: "example.LightBoxScreen", // unique ID registered with Navigation.registerScreen
 passProps: {}, // simple serializable object that will pass as props to the lightbox (optional)
 style: {
   backgroundBlur: "dark", // 'dark' / 'light' / 'xlight' / 'none' - the type of blur on the background
   backgroundColor: "#ff000080" // tint color for the background, you can specify alpha here (optional)
 }
});
```

* **dismissLightBox(params = {})**

Dismiss the current lightbox.

```js
this.props.navigator.dismissLightBox();
```

 * **handleDeepLink(params = {})**

Trigger a deep link within the app. See [deep links](#deep-links) for more details about how screens can listen for deep link events.

```js
this.props.navigator.handleDeepLink({
  link: "chats/2349823023" // the link string (required)
});
```

 * **setOnNavigatorEvent(callback)**

Set a handler for navigator events (like nav button press). This would normally go in your component constructor.

```js
// this.onNavigatorEvent will be our handler
this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
```

 * **setButtons(params = {})**

Set buttons dynamically on the navigator. If your buttons don't change during runtime, see "Adding buttons to the navigator" below to add them using `static navigatorButtons = {...};`.

```js
this.props.navigator.setButtons({
  leftButtons: [], // see "Adding buttons to the navigator" below for format (optional)
  rightButtons: [], // see "Adding buttons to the navigator" below for format (optional)
  animated: true // does the change have transition animation or does it happen immediately (optional)
});
```

 * **setTitle(params = {})**

Set the nav bar title dynamically. If your title doesn't change during runtime, set it when the screen is defined / pushed.

```js
this.props.navigator.setTitle({
  title: "Dynamic Title" // the new title of the screen as appears in the nav bar
});
```

 * **toggleDrawer(params = {})**

Toggle the side menu drawer assuming you have one in your app.

```js
this.props.navigator.toggleDrawer({
  side: 'left', // the side of the drawer since you can have two, 'left' / 'right'
  animated: true, // does the toggle have transition animation or does it happen immediately (optional)
  to: 'open' // optional, 'open' = open the drawer, 'closed' = close it, missing = the opposite of current state
});
```

 * **toggleTabs(params = {})**

Toggle whether the tabs are displayed or not (only in tab-based apps).

```js
this.props.navigator.toggleTabs({
  to: 'hidden', // required, 'hidden' = hide tab bar, 'shown' = show tab bar
  animated: true // does the toggle have transition animation or does it happen immediately (optional)
});
```

 * **setTabBadge(params = {})**

Set the badge on a tab (any string or numeric value).

```js
this.props.navigator.setTabBadge({
  tabIndex: 0, // (optional) if missing, the badge will be added to this screen's tab
  badge: 17 // badge value, null to remove badge
});
```

 * **switchToTab(params = {})**

Switch to a tab (sets it as the currently selected tab).

```js
this.props.navigator.switchToTab({
  tabIndex: 0 // (optional) if missing, this screen's tab will become selected
});
```

* **toggleNavBar(params = {})**

Toggle whether the navigation bar is displayed or not.

```js
this.props.navigator.toggleNavBar({
  to: 'hidden', // required, 'hidden' = hide navigation bar, 'shown' = show navigation bar
  animated: true // does the toggle have transition animation or does it happen immediately (optional). By default animated: true
});
```

## Styling the navigator

You can style the navigator appearance and behavior by passing a `navigatorStyle` object. This object can be passed when the screen is originally created; can be defined per-screen by setting `static navigatorStyle = {};` on the screen component; and can be overridden when a screen is pushed.

The easiest way to style your screen is by adding `static navigatorStyle = {};` to your screen React component definition.

```js
export default class StyledScreen extends Component {
  static navigatorStyle = {
    drawUnderNavBar: true,
    navBarTranslucent: true
  };
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <View style={{flex: 1}}>...</View>
     );
  }
```

#### Style object format

```js
{
  navBarTextColor: '#000000', // change the text color of the title (remembered across pushes)
  navBarBackgroundColor: '#f7f7f7', // change the background color of the nav bar (remembered across pushes)
  navBarButtonColor: '#007aff', // change the button colors of the nav bar (eg. the back button) (remembered across pushes)
  navBarHidden: false, // make the nav bar hidden
  navBarHideOnScroll: false, // make the nav bar hidden only after the user starts to scroll
  navBarTranslucent: false, // make the nav bar semi-translucent, works best with drawUnderNavBar:true
  navBarTransparent: false, // make the nav bar transparent, works best with drawUnderNavBar:true
  navBarNoBorder: false, // hide the navigation bar bottom border (hair line). Default false
  drawUnderNavBar: false, // draw the screen content under the nav bar, works best with navBarTranslucent:true
  drawUnderTabBar: false, // draw the screen content under the tab bar (the tab bar is always translucent)
  statusBarBlur: false, // blur the area under the status bar, works best with navBarHidden:true
  navBarBlur: false, // blur the entire nav bar, works best with drawUnderNavBar:true
  tabBarHidden: false, // make the screen content hide the tab bar (remembered across pushes)
  statusBarHideWithNavBar: false // hide the status bar if the nav bar is also hidden, useful for navBarHidden:true
  statusBarHidden: false, // make the status bar hidden regardless of nav bar state
  statusBarTextColorScheme: 'dark' // text color of status bar, 'dark' / 'light' (remembered across pushes)
}
```

> Note: If you set any styles related to the Status Bar, make sure that in Xcode > project > Info.plist, the property `View controller-based status bar appearance` is set to `YES`.

All supported styles are defined [here](https://github.com/wix/react-native-controllers#styling-navigation). There's also an example project there showcasing all the different styles.

## Adding buttons to the navigator

Nav bar buttons can be defined per-screen by adding `static navigatorButtons = {...};` on the screen component definition. This object can also be passed when the screen is originally created; and can be overridden when a screen is pushed. Handle onPress events for the buttons by setting your handler with `navigator.setOnNavigatorEvent(callback)`.

```js
class FirstTabScreen extends Component {
  static navigatorButtons = {
    rightButtons: [
      {
        title: 'Edit', // for a textual button, provide the button title (label)
        id: 'edit', // id for this button, given in onNavigatorEvent(event) to help understand which button was clicked
        testID: 'e2e_rules', // optional, used to locate this view in end-to-end tests
        disabled: true, // optional, used to disable the button (appears faded and doesn't interact)
        disableIconTint: true, // optional, by default the image colors are overridden and tinted to navBarButtonColor, set to true to keep the original image colors
      },
      {
        icon: require('../../img/navicon_add.png'), // for icon button, provide the local image asset name
        id: 'add' // id for this button, given in onNavigatorEvent(event) to help understand which button was clicked
      }
    ]
  };
  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }
  onNavigatorEvent(event) { // this is the onPress handler for the two buttons together
    if (event.type == 'NavBarButtonPress') { // this is the event type for button presses
      if (event.id == 'edit') { // this is the same id field from the static navigatorButtons definition
        AlertIOS.alert('NavBar', 'Edit button pressed');
      }
      if (event.id == 'add') {
        AlertIOS.alert('NavBar', 'Add button pressed');
      }
    }
  }
  render() {
    return (
      <View style={{flex: 1}}>...</View>
     );
  }
```

#### Buttons object format

```js
{
  rightButtons: [{ // buttons for the right side of the nav bar (optional)
    title: 'Edit', // if you want a textual button
    icon: require('../../img/navicon_edit.png'), // if you want an image button
    id: 'compose', // id of the button which will pass to your press event handler
    testID: 'e2e_is_awesome', // if you have e2e tests, use this to find your button
    disabled: true, // optional, used to disable the button (appears faded and doesn't interact)
    disableIconTint: true, // optional, by default the image colors are overridden and tinted to navBarButtonColor, set to true to keep the original image colors
  }],
  leftButtons: [] // buttons for the left side of the nav bar (optional)
}
```

## Styling the tab bar

You can style the tab bar appearance by passing a `tabsStyle` object when the app is originally created (on `startTabBasedApp`).

```js
Navigation.startTabBasedApp({
  tabs: [ ... ],
  tabsStyle: { // optional, add this if you want to style the tab bar beyond the defaults
    tabBarButtonColor: '#ff0000'
  }
});
```

#### Style object format

```js
{
  tabBarButtonColor: '#ffff00', // change the color of the tab icons and text (also unselected)
  tabBarSelectedButtonColor: '#ff9900', // change the color of the selected tab icon and text (only selected)
  tabBarBackgroundColor: '#551A8B' // change the background color of the tab bar
}
```

All supported styles are defined [here](https://github.com/wix/react-native-controllers#styling-tab-bar). There's also an example project there showcasing all the different styles.

## Deep links

Deep links are strings which represent internal app paths/routes. They commonly appear on push notification payloads to control which section of the app should be displayed when the notification is clicked. For example, in a chat app, clicking on the notification should open the relevant conversation on the "chats" tab.

Another use-case for deep links is when one screen wants to control what happens in another sibling screen. Normally, a screen can only push/pop from its own stack, it cannot access the navigation stack of a sibling tab for example. Returning to our chat app example, assume that by clicking on a contact in the "contacts" tab we want to open the relevant conversation in the "chats" tab. Since the tabs are siblings, you can achieve this behavior by triggering a deep link:

```js
onContactSelected(contactID) {
  this.props.navigator.handleDeepLink({
    link: 'chats/' + contactID
  });
}
```

> Tip: Deep links are also the recommended way to handle side drawer actions. Since the side drawer screen is a sibling to the rest of the app, it can control the other screens by triggering deep links.

#### Handling deep links

Every deep link event is broadcasted to all screens. A screen can listen to these events by defining a handler using `setOnNavigatorEvent` (much like listening for button events). Using this handler, the screen can filter links directed to it by parsing the link string and act upon any relevant links found.

```js
export default class SecondTabScreen extends Component {
  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }
  onNavigatorEvent(event) {
    // handle a deep link
    if (event.type == 'DeepLink') {
      const parts = event.link.split('/');
      if (parts[0] == 'tab2') {
        // handle the link somehow, usually run a this.props.navigator command
      }
    }
  }
```

#### Deep link string format

There is no specification for the format of deep links. Since you're implementing the parsing logic in your handlers, you can use any format you wish.

## Third party libraries support

### react-native-vector-icons

If you would like to use [react-native-vector-icons](https://github.com/oblador/react-native-vector-icons) for your Toolbar icons, you can follow [this example](https://github.com/wix/react-native-navigation/issues/43#issuecomment-223907515) or [this](https://gist.github.com/dropfen/4a2209d7274788027f782e8655be198f) gist.

## License

The MIT License.

See [LICENSE](LICENSE)
