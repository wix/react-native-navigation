# App Launch
When your app is launched for the first time, the bundle is parsed and executed. At this point you need to show your UI. To do so, Listen to the `appLaunched` event and call `Navigation.setRoot` when the event is received.

```js
Navigation.events().registerAppLaunchedListener(() => {
  // Each time the event is received we should call Navigation.setRoot
});
```

!> Register the listener with `registerAppLaunchedListener` as soon as possible - it should be one of the first lines in your `index.js` file.
If you're observing a "white screen" or a hanging splash screen after relaunching your app, it probably means `Navigation.setRoot` isn't called once the app has launched. Perhaps the listener was registered too late.

## The difference between the platforms
When your app is launched, RN makes sure Js context is running. Js context is what enables you to execute JavaScript code.
There are a few differences between iOS and Android in this regard

### iOS
Whenever the app is put into the background, the app process could potentially be destroyed by the system. If this destruction of the app takes place, the Js context will be destroyed along with the app process.

### Android
An Android application is typically bound to two contexts:
1. Application context - bound to the app process
2. Activity context - bound to app UI

React's Js Context is executed on and bound to the Application context. This means that even when the Activity context (and thus the UI) is destroyed, React's Js Context remains active until the Application (and your process) are destroyed by the system.

!>*Important!* Because of this, when your app returns to foreground, Js Context might still exist on Android, even when the Activity and UI context has been destroyed - meaning you might not need to initialise all of your app logic; instead you'll only need to call `Navigation.setRoot` to repopulate the UI context. This circumstance can easily be determined by setting a flag on your app's first `appLaunched` event, and checking the value of that flag on subsequent `appLaunched` events -- if the flag is true in your `appLaunched` callback, you need to call `Navigation.setRoot` to repopulate the UI.

