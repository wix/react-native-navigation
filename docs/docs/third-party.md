# Third Party Libraries Support

## Redux

### registerComponentWithRedux(screenID, generator, store, provider)
Utility helper function like registerComponent,
wraps the provided component with a react-redux Provider with the passed redux store

```js
Navigation.registerComponentWithRedux(`navigation.playground.WelcomeScreen`, () => WelcomeScreen, store, provider);
```