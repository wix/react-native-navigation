# Usage

## Top Screen API

### Navigation
```js
import Navigation from 'react-native-navigation';
```
### Events - On App Launched
How to initiate your app.

```js
Navigation.events().onAppLaunched(() => {
  Navigation.setRoot({
    component: {
      name: 'navigation.playground.WelcomeScreen'
    }
  });
});
```

### registerComponent(screenID, generator)
Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending React.Component.

```js
Navigation.registerComponent(`navigation.playground.WelcomeScreen`, () => WelcomeScreen);
```

### setRoot({params})
Start a Single page app with two side menus:

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
Start a tab based app:

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
## Screen API

### push(params)
Push a new screen into this screen's navigation stack.

```js
Navigation.push(this.props.containerId, {
  name: 'navigation.playground.PushedScreen',
  passProps: {}
});
```
### pop(containerId)
Pop the top screen from this screen's navigation stack.

```js
Navigation.pop(this.props.containerId);
```
### popTo(containerId)
```js
Navigation.popTo(previousScreenId);
```
### popToRoot()
Pop all the screens until the root from this screen's navigation stack

```js
Navigation.popToRoot(this.props.containerId);
```
### showModal(params = {})
Show a screen as a modal.

```js
Navigation.showModal({
  container: {
    name: 'navigation.playground.ModalScreen',
    passProps: {
        key: 'value'
    }
  }
});
```
### dismissModal(containerId)
Dismiss modal.

```js
Navigation.dismissModal(this.props.containerId);
```
### dismissAllModals()
Dismiss all the current modals at the same time.
```js
Navigation.dismissAllModals();
```
### Screen Lifecycle - didDisappear() and didAppear()

The didDisappear() and didAppear() functions are lifecycle functions that are added to the screen and run when a screen apears and disappears from the screen. To use them simply add them to your component like any other react lifecycle function:

```js
class LifecycleScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: 'nothing yet'
    };
  }

  didAppear() {
    this.setState({ text: 'didAppear' });
  }

  didDisappear() {
    alert('didDisappear');
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
