
# Usage

- If you don't like reading, just jump into the fully working [playground](https://github.com/wix/react-native-navigation/tree/v2/playground) project. All features are implemented there and it's the basis for the e2e tests.
- We fully support redux, mobx and other state management libraries. See the integration tests [here](https://github.com/wix/react-native-navigation/tree/v2/integration).
- Navigation is written with `TypeScript` and shipped with the type definitions alongside the transpiled JS code. Use an IDE that supports it like VSCode or Webstorm to enjoy API autocompletion.
- Take a look at this excellent showcase app [JuneDomingo/movieapp](https://github.com/JuneDomingo/movieapp). (using v1 of React Native Navigation with redux).

## The Basics

### Navigation
```js
import { Navigation } from 'react-native-navigation';
```

### registerComponent(screenID, generator)
Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending `React.Component` or `React.PureComponent`. It can also be a HOC to provide context (or provide a redux store). Similar to ReactNative's `AppRegistry.registerComponent`.

```js
Navigation.registerComponent(`navigation.playground.WelcomeScreen`, () => WelcomeScreen);
```


### onAppLaunched(callback)
This event is called once the app is launched. Initialise the app with the layout you want. This creates the native layout hierarchy, loads the react components into the `component` by name, after which the app is ready for user interaction.

```js
Navigation.events().onAppLaunched(() => {
  Navigation.setRoot({
    component: {
      name: 'navigation.playground.WelcomeScreen'
    }
  });
});
```

## Layout Examples

The layout api is completely open in terms of what you can construct with it.

You can compose arbitrary native layout hierarchies, although some weird edge cases may not be possible or produce errors. In such cases, open an issue so that we either fix it or warn in dev time.


For all layout types see [API](/api/README).


### Single page app with two side menus:

```js
Navigation.setRoot({
  sideMenu: {
    left: {
      component: {
        name: 'navigation.playground.TextScreen',
        passProps: {
          text: 'This is a left side menu screen'
        }
      }
    },
    center: {
      component: {
        name: 'navigation.playground.WelcomeScreen'
      },
    },
    right: {
      component: {
        name: 'navigation.playground.TextScreen',
        passProps: {
          text: 'This is a right side menu screen'
        }
      }
    }
  }
});
```

### Tab based app (with passProps example):

```js
Navigation.setRoot({
  bottomTabs: {
    children: [
      {
        component: {
          name: 'navigation.playground.TextScreen',
          passProps: {
            text: 'This is tab 1',
            myFunction: () => 'Hello from a function!',
          },
        },
      },
      {
        component: {
          name: 'navigation.playground.TextScreen',
          passProps: {
            text: 'This is tab 2',
          },
        },
      },
    ],
  },
});
```

### Stack based app (with options example, initialised with 2 screens):

```js
Navigation.setRoot({
  stack: {
    options: {
      topBar: {
        hidden: true,
      },
    },
    children: [
      {
        component: {
          name: 'navigation.playground.TextScreen',
          passProps: {
            text: 'This is tab 1',
            myFunction: () => 'Hello from a function!',
          },
        },
      },
      {
        component: {
          name: 'navigation.playground.TextScreen',
          passProps: {
            text: 'This is tab 2',
          },
        },
      },
    ],
  },
});
```

## Navigating The Stack

For all commands see [API](/api/README).

### push
Push a new instance of a screen (component) on top of `this` screen's navigation stack.

```js
Navigation.push(this.props.componentId, {
  component: {
    name: 'navigation.playground.PushedScreen'
  }
});
```

### pop
Pop the top screen from `this` screen's navigation stack.

```js
Navigation.pop(this.props.componentId);
```

### showModal
Show a screen as a modal. (not part of the stack)

```js
Navigation.showModal({
  component: {
    name: 'navigation.playground.ModalScreen'
  }
});
```
### dismissModal
Dismiss modal.

```js
Navigation.dismissModal(this.props.componentId);
```

## Screen Lifecycle

The `componentDidAppear` and `componentDidDisappear` functions are lifecycle callbacks that are called by React Native Navigation on the component when it appears and disappears. 

These are similar to react's `componentDidMount` and `componentWillUnmount`, but are related to the actual visibility of a component to the user. While the component is `mounted` as soon as it's part of a layout, it is not always `visible` (for example, when another screen is `pushed` on top of it), and therefore React Native Navigation  takes some performance optimizations.

They are also useful for a lot of use cases, for example starting and stopping an animation while the component is shown on-screen.

> They are implemented by iOS's viewDidAppear/viewDidDisappear and Android's ViewTreeObserver visibility detection

To use them, simply implement them in your component like any other react lifecycle function:

```js
class LifecycleScreenExample extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: 'nothing yet'
    };
  }

  componentDidAppear() {
    this.setState({ text: 'componentDidAppear' });
  }

  componentDidDisappear() {
    alert('componentDidDisappear');
  }

  componentWillUnmount() {
    alert('componentWillUnmount');
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Lifecycle Screen`}</Text>
	      <Text style={styles.h1}>{this.state.text}</Text>
      </View>
    );
  }
}
```
