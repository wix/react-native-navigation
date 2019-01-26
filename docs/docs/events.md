# Events

## onAppLaunched

Called once the app is launched. This event is used to set the Application's initial layout - after which the app is ready for user interaction.

```js
const appLaunchedListener = Navigation.events().registerAppLaunchedListener(() => {

});
```

RNN automatically unsubscribes components when they unmount, therefore unsubscribing isn't actually mandatory if you subscribed in `componentDidMount`.

But you can use the following method to unsubscribe manually.

```js
appLaunchedListener.remove();
```

## componentDidAppear
Called each time this component appears on screen (attached to the view hierarchy)

```js
class MyComponent extends Component {

  componentDidMount() {
    this.navigationEventListener = Navigation.events().bindComponent(this);
  }

  componentWillUnmount() {
    // Not mandatory
    if (this.navigationEventListener) {
      this.navigationEventListener.remove();
    }
  }

  componentDidAppear() {

  }
}
```

This event can be observed globally as well:

```js
// Subscribe
const screenEventListener = Navigation.events().registerComponentDidAppearListener(({ componentId, componentName }) => {

});
...
// Unsubscribe
screenEventListener.remove();
```
|       Parameter         | Description |
|:--------------------:|:-----|
|**componentId**| Id of the appearing component|
|**componentName**|Registered name used when registering the component with `Navigation.registerComponent()`|

## componentDidDisappear
Called each time this component disappears from screen (detached from the view heirarchy)

```js
class MyComponent extends Component {

  componentDidMount() {
    this.navigationEventListener = Navigation.events().bindComponent(this);
  }

  componentWillUnmount() {
    // Not mandatory
    if (this.navigationEventListener) {
      this.navigationEventListener.remove();
    }
  }

  componentDidDisappear() {

  }
}
```

This event can be observed globally as well:

```js
// Subscribe
const screenEventListener = Navigation.events().registerComponentDidDisappearListener(({ componentId, componentName }) => {

});
...
// Unsubscribe
screenEventListener.remove();
```
|       Parameter         | Description |
|:--------------------:|:-----|
|**componentId**| Id of the disappearing component|
|**componentName**|Registered name used when registering the component with `Navigation.registerComponent()`|

## registerCommandListener
The `commandListener` is called whenever a *Navigation command* (i.e push, pop, showModal etc) is invoked.

```js
// Subscribe
const commandListener = Navigation.events().registerCommandListener((name, params) => {

});
...
// Unsubscribe
commandListener.remove();
```
|       Parameter         | Description |
|:--------------------:|:-----|
|**name** | The name of the command that was invoked. For example `push`|
|**params**|`commandId`: Each command is assigned a unique Id<br>`componentId`: Optional, the componentId passed to the command<br>`layout`: Optional, If the command invoked created a screen. Slim representation of the component and its options |

## registerCommandCompletedListener
Invoked when a command finishes executing in native. If the command contains animations, for example pushed screen animation,) the listener is invoked after the animation ends.

```js
// Subscribe
const commandCompletedListener = Navigation.events().registerCommandCompletedListener(({ commandId, completionTime, params }) => {

});
...
// Unsubscribe
commandCompletedListener.remove();
```

|       Parameter         | Description |
|:--------------------:|:-----|
|**commandId** | Id of the completed command|
|**completionTime**|Timestamp when the command, and consecutive animations, completed.|

## registerModalDismissedListener
Invoked when modal dismissed.

```js
// Subscribe
const modalDismissedListener = Navigation.events().registerModalDismissedListener(({ componentId, modalsDismissed }) => {

});
...
// Unsubscribe
modalDismissedListener.remove();
```

|       Parameter         | Description |
|:--------------------:|:-----|
|**componentId** | Id of the modal|
|**modalsDismissed**| Number of modal which were dismissed|

## registerBottomTabSelectedListener
Invoked when a BottomTab is selected by the user.

```js
// Subscribe
const bottomTabEventListener = Navigation.events().registerBottomTabSelectedListener(({ selectedTabIndex, unselectedTabIndex }) => {

});
...
// Unsubscribe
bottomTabEventListener.remove();
```

|       Parameter         | Description |
|:--------------------:|:-----|
|**selectedTabIndex** | The index of the newly selected tab|
|**unselectedTabIndex**|The index of the previously selected tab|

## navigationButtonPressed event
This event is emitted whenever a TopBar button is pressed by the user.

```js
class MyComponent extends Component {

  componentDidMount() {
    this.navigationEventListener = Navigation.events().bindComponent(this);
  }

  componentWillUnmount() {
    // Not mandatory
    if (this.navigationEventListener) {
      this.navigationEventListener.remove();
    }
  }
  
  navigationButtonPressed({ buttonId }) {

  }
}
```

This event can be observed globally as well:

```js
// Subscribe
const navigationButtonEventListener = Navigation.events().registerNavigationButtonPressedListener(({ buttonId }) => {

});
...
// Unsubscribe
navigationButtonEventListener.remove();
```

|Parameter|Description|
|:-:|:--|
|**buttonId**|`buttonId`: `id` of the pressed button|

## searchBarUpdated (iOS 11+ only)
Called when a SearchBar from NavigationBar gets updated.

```js
class MyComponent extends Component {

  componentDidMount() {
    this.navigationEventListener = Navigation.events().bindComponent(this);
  }

  componentWillUnmount() {
    // Not mandatory
    if (this.navigationEventListener) {
      this.navigationEventListener.remove();
    }
  }

  searchBarUpdated({ text, isFocused }) {

  }
}
```

## searchBarCancelPressed (iOS 11+ only)
Called when the cancel button on the SearchBar from NavigationBar gets pressed.

```js
class MyComponent extends Component {

  componentDidMount() {
    this.navigationEventListener = Navigation.events().bindComponent(this);
  }

  componentWillUnmount() {
    // Not mandatory
    if (this.navigationEventListener) {
      this.navigationEventListener.remove();
    }
  }

  searchBarCancelPressed() {

  }
}
```

## previewCompleted (iOS 11.4+ only)
Called when preview peek is completed

```js
class MyComponent extends Component {

  componentDidMount() {
    this.navigationEventListener = Navigation.events().bindComponent(this);
  }

  componentWillUnmount() {
    // Not mandatory
    if (this.navigationEventListener) {
      this.navigationEventListener.remove();
    }
  }

  previewCompleted({ previewComponentId }) {

  }
}
```
