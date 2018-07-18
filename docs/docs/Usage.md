# Usage

- If you don't like reading, just jump into the fully working [playground](https://github.com/wix/react-native-navigation/tree/v2/playground) project. All features are implemented there, and it's the basis for the e2e tests.
- We fully support Redux, MobX and other state management libraries. See the integration tests [here](https://github.com/wix/react-native-navigation/tree/v2/integration).
- Navigation is written with `TypeScript` and shipped with the type definitions alongside the transpiled JS code. To enjoy API autocompletion, use an IDE that supports it, like VSCode or Webstorm.
- Take a look at this excellent showcase app [JuneDomingo/movieapp](https://github.com/JuneDomingo/movieapp). (using v1 of React Native Navigation with redux).

## The Basics

### Navigation
```js
import { Navigation } from 'react-native-navigation';
```

### registerComponent(screenID, generator)
Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending `React.Component` or `React.PureComponent`. It can also be a HOC to provide context (or a Redux store). Analgous to ReactNative's `AppRegistry.registerComponent`.

```js
Navigation.registerComponent(`navigation.playground.WelcomeScreen`, () => WelcomeScreen);
```


### registerAppLaunchedListener(callback)
This event is called once the app is launched. It's where you will initialize the app with the layout you want, via the SetRoot command. This creates the native layout hierarchy, loading React components into the `component` by name. 

Afterwards, the app is ready for user interaction. (Common gotcha: Be sure not to run setRoot before registerAppLaunchedListener() has fired!)

```js
Navigation.events().registerAppLaunchedListener(() => {
  Navigation.setRoot({
    root: {
      component: {
        name: 'navigation.playground.WelcomeScreen'
      }
    }
  });
});
```

## Screen Lifecycle

The `componentDidAppear` and `componentDidDisappear` functions are special React Native Navigation lifecycle callbacks that are called on the component when it appears and disappears (after it was bounded using `Navigation.events().bindComponent(this)`). 

Similar to React's `componentDidMount` and `componentWillUnmount`, what's different is that they represent whether the user can actually see the component in question -- and not just whether it's been mounted or not. Because of the way React Native Navigation optimizes performance, a component is actually `mounted` as soon as it's part of a layout -- but it is not always `visible` (for example, when another screen is `pushed` on top of it).

There are lots of use cases for these. For example: starting and stopping an animation while the component is shown on-screen.

> They are implemented by iOS's viewDidAppear/viewDidDisappear and Android's ViewTreeObserver visibility detection

To use them, simply implement them in your component like any other React lifecycle function, and bind the screen to the Navigation events module which will call all functions automatically:

```js
class LifecycleScreenExample extends Component {
  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this);
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

  navigationButtonPressed({buttonId}) {
    // a navigation-based button (for example in the topBar) was clicked. See section on buttons.
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
