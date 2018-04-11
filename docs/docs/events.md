# Events

## onAppLaunched

This event is called once the app is launched. Initialise the app with the layout you want. This creates the native layout hierarchy, loads the react components into the component by name, after which the app is ready for user interaction.

```js
Navigation.events().onAppLaunched(() => {

});
```

## componentDidAppear

Listen globally to all components `componentDidAppear` events
```js
Navigation.events().componentDidAppear((componentId, componentName) => {

});
```

Listen for component
```js
class MyComponent extends Component {
  componentDidAppear() {
    
  }
}
```

## componentDidDisappear

Listen globally to all components `componentDidDisappear` events
```js
Navigation.events().componentDidDisappear((componentId, componentName) => {

});
```

Listen for component
```js
class MyComponent extends Component {
  componentDidDisappear() {
    
  }
}
```

## onNavigationButtonPressed

Listen globally to all `onNavigationButtonPressed` events
```js
Navigation.events().onNavigationButtonPressed((buttonId) => {

});
```

Listen in component
```js
class MyComponent extends Component {
  onNavigationButtonPressed(buttonId) {
    
  }
}
```