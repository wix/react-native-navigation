# Events

## onAppLaunched

Called once the app is launched. This event is used to set the Application's initial layout - after which the app is ready for user interaction.

```js
Navigation.events().registerAppLaunchedListener(() => {

});
```

## componentDidAppear
Called each time this component appears on screen (attached to the view heirarchy)

```js
class MyComponent extends Component {
  componentDidAppear() {
    
  }
}
```

This event can be observed globally as well:

```js
Navigation.events().registerComponentDidAppearListener((componentId, componentName) => {

});
```
|       Parameter         | Description |
|:--------------------:|:-----|
**componentId**| Id of the appearing component|
**componentName**|Registered name used when registering the component with `Navigation.registerComponent()`|

## componentDidDisappear
Called each time this component disappears from screen (detached from the view heirarchy)

```js
class MyComponent extends Component {
  componentDidAppear() {
    
  }
}
```

This event can be observed globally as well:

```js
Navigation.events().registerComponentDidDisappearListener((componentId, componentName) => {

});
```
|       Parameter         | Description |
|:--------------------:|:-----|
**componentId**| Id of the disappearing component|
**componentName**|Registered name used when registering the component with `Navigation.registerComponent()`|

## registerCommandListener
The `commandListener` is called whenever a *Navigation command* (i.e push, pop, showModal etc) is invoked.

```js
Navigation.events().registerCommandListener((name, params) => {

});
```
|       Parameter         | Description |
|:--------------------:|:-----|
|**name** | The name of the command that was invoked. For example `push`|
|**params**|`commandId`: Each command is assigned a unique Id<br>`componentId`: Optional, the componentId passed to the command<br>`layout`: Optional, If the command invoked created a screen. Slim representation of the component and its options |

## registerCommandCompletedListener
Invoked when a command finishes executing in native. If the command contains animations, for example pushed screen animation,) the listener is invoked after the animation ends.

```js
Navigation.events().registerCommandCompletedListener((commandId, completionTime, params) => {

});
```

|       Parameter         | Description |
|:--------------------:|:-----|
|**commandId** | Id of the completed command|
|**completionTime**|Timestamp when the comand, and consecutive animations, completed.|


## onNavigationButtonPressed
Called when a TopBar button is pressed.
```js
class MyComponent extends Component {
  onNavigationButtonPressed(buttonId) {
    
  }
}
```