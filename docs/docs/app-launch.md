# App Launch
When your app is launched for the first time, the bundle is parsed and executed. At this point you need to show your UI. To do so, Listen to the `appLaunched` event and call `Navigation.setRoot` when the event is received.

```js
Navigation.events().registerAppLaunchedListener(() => {
  // Each time the event is received we should call Navigation.setRoot
});
```

!> Register the listener as soon as possible - it should be one of the first lines in your `index.js` file.
If you're observing a "white screen" or a hanging splash screen after relaunching your app, it probably means `Navigation.setRoot` isn't called once the app has launched. Perhaps the listener was registered too late.

## The difference between the platforms
When your app is launched, RN makes sure Js context is running. Js context is what enables you to execute JavaScript code.
There are a few differences between iOS and Android in this regard

### iOS
Each time the app moves the the background, Js context is destroyed as the app's process is destroyed.

### Android
An Android application is typically bound to two contexts:
1. Application - bound to the process
2. Activity - bound to UI

Js Context is executed and bound to the Application. This means, that even when the Activity is destroyed, Js Context is still executed until the Application (and your process) are destroyed by the system.

!>*Important!* Because of this, when your app returns to foreground, Js Context might still exist on Android - meaning you might not need to initialise all of your logic, instead you'll only need to call `Navigation.setRoot`.

