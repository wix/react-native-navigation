# Screen API

This API is relevant when in a screen component context - it allows a screen to push other screens, pop screens, change its navigator style, etc. Access to this API is available through the `navigator` object that is passed to your component through `props`.

## push(params)

Push a new screen into this screen's navigation stack.

```js
this.props.navigator.push({
  screen: 'example.ScreenThree', // unique ID registered with Navigation.registerScreen
  title: undefined, // navigation bar title of the pushed screen (optional)
  titleImage: require('../../img/my_image.png'), //navigation bar title image instead of the title text of the pushed screen (optional)
  passProps: {}, // Object that will be passed as props to the pushed screen (optional)
  animated: true, // does the push have transition animation or does it happen immediately (optional)
  backButtonTitle: undefined, // override the back button title (optional)
  backButtonHidden: false, // hide the back button altogether (optional)
  navigatorStyle: {}, // override the navigator style for the pushed screen (optional)
  navigatorButtons: {} // override the nav buttons for the pushed screen (optional)
});
```

## pop(params = {})

Pop the top screen from this screen's navigation stack.

```js
this.props.navigator.pop({
  animated: true // does the pop have transition animation or does it happen immediately (optional)
});
```

## popToRoot(params = {})

Pop all the screens until the root from this screen's navigation stack.

```js
this.props.navigator.popToRoot({
  animated: true // does the pop have transition animation or does it happen immediately (optional)
});
```

## resetTo(params)

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

## showModal(params = {})

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

## dismissModal(params = {})

Dismiss the current modal.

```js
this.props.navigator.dismissModal({
  animationType: 'slide-down' // 'none' / 'slide-down' , dismiss animation for the modal (optional, default 'slide-down')
});
```

## dismissAllModals(params = {})

Dismiss all the current modals at the same time.

```js
this.props.navigator.dismissAllModals({
  animationType: 'slide-down' // 'none' / 'slide-down' , dismiss animation for the modal (optional, default 'slide-down')
});
```

## showLightBox(params = {})

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

## dismissLightBox(params = {})

Dismiss the current lightbox.

```js
this.props.navigator.dismissLightBox();
```

## handleDeepLink(params = {})

Trigger a deep link within the app. See [deep links](https://wix.github.io/react-native-navigation/#/deep-links) for more details about how screens can listen for deep link events.

```js
this.props.navigator.handleDeepLink({
  link: "chats/2349823023" // the link string (required)
});
```

> `handleDeepLink` can also be called statically:
```js
  import {Navigation} from 'react-native-navigation';
  Navigation.handleDeepLink(...);
```

## setOnNavigatorEvent(callback)

Set a handler for navigator events (like nav button press). This would normally go in your component constructor.

```js
// this.onNavigatorEvent will be our handler
this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
```

## setButtons(params = {})

Set buttons dynamically on the navigator. If your buttons don't change during runtime, see "Adding buttons to the navigator" below to add them using `static navigatorButtons = {...};`.

```js
this.props.navigator.setButtons({
  leftButtons: [], // see "Adding buttons to the navigator" below for format (optional)
  rightButtons: [], // see "Adding buttons to the navigator" below for format (optional)
  animated: true // does the change have transition animation or does it happen immediately (optional)
});
```

## setTitle(params = {})

Set the nav bar title dynamically. If your title doesn't change during runtime, set it when the screen is defined / pushed.

```js
this.props.navigator.setTitle({
  title: "Dynamic Title" // the new title of the screen as appears in the nav bar
});
```


## setSubTitle(params = {})

Set the nav bar subtitle dynamically. If your subtitle doesn't change during runtime, set it when the screen is defined / pushed.

```js
this.props.navigator.setSubTitle({
  subtitle: "Connecting..." 
});
```

## toggleDrawer(params = {})

Toggle the side menu drawer assuming you have one in your app.

```js
this.props.navigator.toggleDrawer({
  side: 'left', // the side of the drawer since you can have two, 'left' / 'right'
  animated: true, // does the toggle have transition animation or does it happen immediately (optional)
  to: 'open' // optional, 'open' = open the drawer, 'closed' = close it, missing = the opposite of current state
});
```

## toggleTabs(params = {})

Toggle whether the tabs are displayed or not (only in tab-based apps).

```js
this.props.navigator.toggleTabs({
  to: 'hidden', // required, 'hidden' = hide tab bar, 'shown' = show tab bar
  animated: true // does the toggle have transition animation or does it happen immediately (optional)
});
```

## setTabBadge(params = {})

Set the badge on a tab (any string or numeric value).

```js
this.props.navigator.setTabBadge({
  tabIndex: 0, // (optional) if missing, the badge will be added to this screen's tab
  badge: 17 // badge value, null to remove badge
});
```

## switchToTab(params = {})

Switch to a tab (sets it as the currently selected tab).

```js
this.props.navigator.switchToTab({
  tabIndex: 0 // (optional) if missing, this screen's tab will become selected
});
```

## toggleNavBar(params = {})

Toggle whether the navigation bar is displayed or not.

```js
this.props.navigator.toggleNavBar({
  to: 'hidden', // required, 'hidden' = hide navigation bar, 'shown' = show navigation bar
  animated: true // does the toggle have transition animation or does it happen immediately (optional). By default animated: true
});
```

# Screen Visibility
Listen to screen visibility events in onNavigatorEvent handler:

```js
export default class ExampleScreen extends Component {
  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }
  onNavigatorEvent(event) {
    switch(event.id) {
      case 'willAppear':
       break;
      case 'didAppear':
        break;
      case 'willDisappear':
        break;
      case 'didDisappear':
        break;
    }
  }
}
```
