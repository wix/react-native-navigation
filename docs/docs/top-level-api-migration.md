# Top Level API Migration

In order to make our API homogenous as much as possible, we provide setRoot function that will receive layout of any kind.

## startTabBasedApp(params) -> setRoot({bottomTabs})

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
              title: 'Tab 1',
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
              title: 'Tab 2',
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

## startSingleScreenApp(params) -> setRoot({stack})

Change your app root into an app based on a single screen (like the iOS Calendar or Settings app). The screen will receive its own navigation stack with a native nav bar

```js
Navigation.setRoot({
  root: {
    stack: {
      children: [{
        component: {
          name: 'example.WelcomeScreen',
          passProps: {
            text: 'stack with one child'
          }
        }
      }],
      options: {
        topBar: {
          title: {
            text: 'Welcome screen'
          }
        }
      }
    }
  }
});
```

## showModal(params = {}) -> showModal(layout = {})

Show a screen as a modal.

```js
Navigation.showModal({
  stack: {
    children: [{
      component: {
        name: 'example.ModalScreen',
        passProps: {
          text: 'stack with one child'
        },
        options: {
          topBar: {
            title: {
              text: 'Modal with stack'
            }
          }
        }
      }
    }]
  }
});
```

## dismissModal(params = {}) -> dismissModal(componentId)

Dismiss the current modal.

```js
Navigation.dismissModal(this.props.componentId);
```

## dismissAllModals(params = {}) -> dismissAllModals()

Dismiss all the current modals at the same time.

```js
Navigation.dismissAllModals();
```

<!-- ## showLightBox(params = {}) - Use showOverlay

Show a screen as a lightbox.

```js
Navigation.showLightBox({
  screen: 'example.LightBoxScreen', // unique ID registered with Navigation.registerScreen
  passProps: {}, // simple serializable object that will pass as props to the lightbox (optional)
  style: {
    backgroundBlur: 'dark', // 'dark' / 'light' / 'xlight' / 'none' - the type of blur on the background
    backgroundColor: '#ff000080', // tint color for the background, you can specify alpha here (optional)
    tapBackgroundToDismiss: true // dismisses LightBox on background taps (optional)
  }
});
``` -->

<!-- ## dismissLightBox(params = {})

Dismiss the current lightbox.

```js
Navigation.dismissLightBox();
``` -->

<!-- ## handleDeepLink(params = {})

Trigger a deep link within the app. See [deep links](https://wix.github.io/react-native-navigation/#/deep-links) for more details about how screens can listen for deep link events.

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
In order to have any use of this method, you'd need to map instanceId to screens your self. -->