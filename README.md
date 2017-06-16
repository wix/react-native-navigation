
<h1 align="center">
  <img src="./logo.png"/> <img src="./mm.png"/><br>
  React Native Navigation ft. MediaMonksMobile
</h1>

- Forked from https://github.com/wix/react-native-navigation
- Based on 1.1.85 (394e0960f8824b9206da75de2bb013fbd64554bd)
- Documentation https://wix.github.io/react-native-navigation/

## Disable open gesture

You can use the folowing functionality from any screen to
disable/enable the opening of the drawer menu:
```js
this.props.navigator.disableOpenGesture({
  disableOpenGesture: false,
});
```

## Switch pages
The methods startSingleScreenApp and startTabBasedApp will now return
a drawerID and a navigatorID. Using drawerId together with 
updateDrawerToScreen or updateDrawerToTabs you can now reset the 
drawer from outside a screen.

This was implemented to be able to listen to navigation deeplinks sent
from a drawer, and flip the page without reimplementing the same logic
on every drawer page.

Example:
```js
const drawerID = Navigation.startSingleScreenApp({
  screen: {
    screen,
  },
});
Navigation.setEventHandler('root', (event) => {
  if (event.type === 'DeepLink') {
  	if (showScreen) {	
	    Navigation.updateDrawerToScreen({
	      drawerID,
	      screen: event.link,
	    });
    } elseif (showTabs) {
  		Navigation.updateDrawerToTabs({
          drawerID,
          tabs: [
            {
              screen: 'screenOne',
              label: '1',
              icon: require('@static/image/1.png'),
            },
		    {
		      screen: 'screenTwo',
		      label: '2',
		      icon: require('@static/image/2.png'),
		    },
		    {
		      screen: 'screenThree',
		      label: '3',
		      icon: require('@static/image/3.png'),
		    },
          ],
  		});
    }
  }
});
```
You can also choose to use updateRootScreen, which will change the
entire screen. This will also remove the drawer etc.

```js
Navigation.updateRootScreen({
  screen: screen,
});
```

## Add sideMenu button behavior for iOS
In this library for android, the navigation buttons have the behavior
that when the id of the button is 'sideMenu', a standard menu button
will be shown and the menu is automatically opened/closed onPress.
Now iOS will look for this id as well, you will still have to provide
the look and feel yourself.

## Add ability to implement screen specific navigator options
When you add a static navigatorOptions to your screen component, the
navigator will check and inject these into the params for every action.
Take this call for example:
```js
this.props.navigator.push({ screen: 'example' });
```
If this screen has:
```js
static navigatorOptions = { title: 'Example' };
```
The navigator will inject 'title' into the 'push' params, so you don't
have to add the title param to every push you do.
Adding a title to the 'push' params will always override the static
navigatorOptions.

## Disable back navigation
You can now disable back navigation. This is not advised as standard
behavior, and should be used only in rare cases.
For example: When doing a backend call that can not be canceled.
It is very important to enable it after success or error.
For iOS this will hide the back button, you can specify if this 
should happen animated or not (fade). It will also disable the 
edge-swipe.
For Android this will hide the back button and ignore any 'physical'
back button taps.
Example usage:
```js
this.props.navigator.disableBackNavigation({
  disableBackNavigation: false,
  animated: true,
});
```

## Add and remove splash screen (iOS)
Add and remove the splash screen. This will not replace, but overlay
the app with the splash screen. Only one instance will be kept.
Example usage:
```js
Navigation.showSplashScreen();
Navigation.hideSplashScreen();
```

## Set drawer width per drawer
Functionality was added for Android and syntax is changed for iOS, to
behave the same way for both platforms.

Example:
```js
Navigation.startSingleScreenApp({
  screen: {
    screen: screen,
  },
  drawer: {
    left: {
      screen: menu,
      drawerWidth: Dimensions.get('window').width,
    },
  },
});
```