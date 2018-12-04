# Third Party Libraries Support

## Redux

### wrapWithProvider(screenID, generator, Provider, providerParams)
Utility helper function like registerComponent,
wraps the provided component with a react-redux Provider or any other type of provider with the passed params for the provider.

```js
Navigation.wrapWithProvider('navigation.playground.WelcomeScreen', () => WelcomeScreen, Provider, {store: store});
```
