
<h1 align="center">
  <img src="./logo.png"/> <img src="./mm.png"/><br>
  React Native Navigation / MediaMonksMobile
</h1>

## Disable open gesture

You can use the folowing functionality from any screen to disable/enable 
the opening of the drawer menu:
```js
this.props.navigator.disableOpenGesture({
						disableOpenGesture: false,
					});
```

## Switch pages
The method startSingleScreenApp will now return the freshly created
navigatorID. Using that together with updateSingleScreen app you can now
reset the navigation from outside a screen.

This was implemented to be able to listen to navigation deeplinks sent
from a drawer, and flip the page without reimplementing the same logic
on every drawer page.

Example:
```js
const navigatorID = Navigation.startSingleScreenApp({
			screen: {
				screen,
				navigatorButtons,
			},
		});
Navigation.setEventHandler('root', (event) => {
			if (event.type === 'DeepLink') {
				Navigation.updateSingleScreenApp({
					navigatorID,
					screen: event.link,
					animated: false,
				});
			}
		});
```
## Add sideMenu button behavior for iOS
In this library for android, the navigation buttons have the behavior
that when the id of the button is 'sideMenu', a standard menu button
will be shown and the menu is automatically opened/closed onPress.
Now iOS will look for this id as well, you will still have to provide
the look and feel yourself.

#Add ability to implement screen specific navigator options
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