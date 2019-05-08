# Top Level API

So as to make the navigation API as consistent and homogenous as possible, we begin with a single, unifying function -- setRoot. SetRoot receives properties for any kind of layout, whether tabs or stacks, or a combination of both (as seen in this example.)

See [Layout types](docs/layout-types)


## setRoot(layout)

```js
Navigation.setRoot({
  root: {
    bottomTabs: {
      children: [{
        stack: {
          children: [{
            component: {
              name: 'example.FirstTabScreen',
              passProps: {
                text: 'This is tab 1'
              }
            }
          }],
          options: {
            bottomTab: {
              text: 'Tab 1',
              icon: require('../images/one.png'),
              testID: 'FIRST_TAB_BAR_BUTTON'
            }
          }
        }
      },
      {
        component: {
          name: 'navigation.playground.TextScreen',
          passProps: {
            text: 'This is tab 2'
          },
          options: {
            bottomTab: {
              text: 'Tab 2',
              icon: require('../images/two.png'),
              testID: 'SECOND_TAB_BAR_BUTTON'
            }
          }
        }
      }]
    }
  }
});
```

## showOverlay(layout = {})

Shows a component as an overlay.

```js
Navigation.showOverlay({
  component: {
    name: 'example.Overlay',
    options: {
      overlay: {
        interceptTouchOutside: true
      }
    }
  }
});
```

## dismissOverlay(componentId)

Dismisses an overlay matching the given overlay componentId.

```js
Navigation.dismissOverlay(this.props.componentId);
```


<!-- ## handleDeepLink(params = {})

Triggers a deep link within the app. See [deep links](https://wix.github.io/react-native-navigation/#/deep-links) for more details about how screens can listen for deep link events.

```js
Navigation.handleDeepLink({
  link: 'link/in/any/format',
  payload: '' // (optional) Extra payload with deep link
});
``` -->

<!-- ## registerScreen(screenID, generator)

This is an internal function you probably don't want to use directly. If your screen components extend `Screen` directly (`import { Screen } from 'react-native-navigation'`), you can register them directly with `registerScreen` instead of with `registerComponent`. The main benefit of using `registerComponent` is that it wraps your regular screen component with a `Screen` automatically.

```js
Navigation.registerScreen('example.AdvancedScreen', () => AdvancedScreen);
```

## getCurrentlyVisibleScreenId()

In some cases you might need the id of the currently visible screen. This method returns the unique id of the currently visible screen:
`const visibleScreenInstanceId = await Navigation.getCurrentlyVisibleScreenId()`
In order to have any use of this method, you'd need to map instanceId to screens yourself. -->
