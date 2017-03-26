# Styling the Tab Bar

You can style the tab bar appearance by passing a `tabsStyle` object when the app is originally created (on `startTabBasedApp`).

```js
Navigation.startTabBasedApp({
  tabs: [ ... ],
  tabsStyle: { // optional, **iOS Only** add this if you want to style the tab bar beyond the defaults
    tabBarButtonColor: '#ff0000'
  }
});
```

#### Style object format

```js
{
  tabBarButtonColor: '#ffff00', // change the color of the tab icons and text (also unselected)
  tabBarSelectedButtonColor: '#ff9900', // change the color of the selected tab icon and text (only selected)
  tabBarBackgroundColor: '#551A8B' // change the background color of the tab bar
}
```

> *Note:* On Android, add BottomTabs styles to `AppStyle`:
```js
Navigation.startTabBasedApp({
  tabs: [...],
  appStyle: {
    tabBarBackgroundColor: '#0f2362',
    tabBarButtonColor: '#ffffff',
    tabBarSelectedButtonColor: '#63d7cc'
  },
...
}
```

All supported styles are defined [here](https://github.com/wix/react-native-controllers#styling-tab-bar). There's also an example project there showcasing all the different styles.