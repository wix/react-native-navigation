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
  tabBarHidden: false, // make the tab bar hidden
  tabBarButtonColor: '#ffff00', // change the color of the tab icons and text (also unselected)
  tabBarSelectedButtonColor: '#ff9900', // change the color of the selected tab icon and text (only selected)
  tabBarBackgroundColor: '#551A8B' // change the background color of the tab bar
  tabBarTranslucent: false // change the translucent of the tab bar to false
  tabBarTextFontFamily: 'Avenir-Medium' //change the tab font family
  tabBarLabelColor: '#ffb700', // iOS only. change the color of tab text
  tabBarSelectedLabelColor: 'red', // iOS only. change the color of the selected tab text
  forceTitlesDisplay: true // Android only. If true - Show all bottom tab labels. If false - only the selected tab's label is visible.
  tabBarHideShadow: true // iOS only. Remove default tab bar top shadow (hairline)
  forceTitlesDisplay: true // Android only. If true - Show all bottom tab labels. If false - only the selected tab's label is visible.
}
```

?> On Android, add BottomTabs styles to `AppStyle`:

```js
Navigation.startTabBasedApp({
  tabs: [...],
  appStyle: {
    tabBarBackgroundColor: '#0f2362',
    tabBarButtonColor: '#ffffff',
    tabBarSelectedButtonColor: '#63d7cc',
    tabBarTranslucent: false,
    tabFontFamily: 'Avenir-Medium.ttf'  // for asset file or use existing font family name
  },
...
}
```
