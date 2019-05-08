# Third Party Libraries Support

## Redux
!> This is deprecated in favor of [Registering screens with wrapping provider component](https://wix.github.io/react-native-navigation/#/docs/top-level-api-migration?id=registering-screens-with-wrapping-provider-component)   
### registerComponentWithRedux(screenID, generator, Provider, store)
Utility helper function like registerComponent,
wraps the provided component with a react-redux Provider with the passed redux store

```js
Navigation.registerComponentWithRedux('navigation.playground.WelcomeScreen', () => WelcomeScreen, Provider, store);
```
