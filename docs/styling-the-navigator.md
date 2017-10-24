# Styling the Navigator

You can style the navigator appearance and behavior by passing a `navigatorStyle` object. This object can be passed when the screen is originally created; can be defined per-screen by setting `static navigatorStyle = {};` on the screen component; and can be overridden when a screen is pushed.

The easiest way to style your screen is by adding `static navigatorStyle = {};` to your screen React component definition.

```js
export default class StyledScreen extends Component {
  static navigatorStyle = {
    drawUnderNavBar: true,
    navBarTranslucent: true
  };
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <View style={{flex: 1}}>...</View>
     );
  }
```

## Disabling persistent styling properties on iOS
By design, most style properties on iOS are carried on to pushed screens. For example, If the navigation stack contains a single screen with `navBarButtonColor: 'blue'`; any screen pushed to this stack will have a blue `navBar` without needing to declare that property. This results in unpredictable and hard to determine style mechanism, therefore it wasn't implemented on Android.
To disable this behavior on iOS, add `keepStyleAcrossPush: false` to `appStyle`:

```js
Navigation.startTabBasedApp({
  ...
  appStyle: {
    keepStyleAcrossPush: false
  }
});
```

## Setting styles dynamically
Use the `setStyle` method to change a screen's style dynamically.

```js
this.props.navigator.setStyle({
  navBarBackgroundColor: 'blue'
});
```

## Style object format

```js
{
  // Common
  navBarTextColor: '#000000', // change the text color of the title (remembered across pushes)
  navBarTextFontSize: 18, // change the font size of the title
  navBarTextFontFamily: 'font-name', // Changes the title font
  navBarBackgroundColor: '#f7f7f7', // change the background color of the nav bar (remembered across pushes)
  navBarCustomView: 'example.CustomTopBar', // registered component name
  navBarComponentAlignment: 'center', // center/fill
  navBarCustomViewInitialProps: {}, // Serializable JSON passed as props
  navBarButtonColor: '#007aff', // Change color of nav bar buttons (eg. the back button) (remembered across pushes)

  navBarHidden: false, // make the nav bar hidden
  navBarHideOnScroll: false, // make the nav bar hidden only after the user starts to scroll
  navBarTranslucent: false, // make the nav bar semi-translucent, works best with drawUnderNavBar:true
  navBarTransparent: false, // make the nav bar transparent, works best with drawUnderNavBar:true,
  navBarNoBorder: false, // hide the navigation bar bottom border (hair line). Default false
  drawUnderNavBar: false, // draw the screen content under the nav bar, works best with navBarTranslucent:true
  drawUnderTabBar: false, // draw the screen content under the tab bar (the tab bar is always translucent)
  navBarBlur: false, // blur the entire nav bar, works best with drawUnderNavBar:true
  tabBarHidden: false, // make the screen content hide the tab bar (remembered across pushes)
  statusBarHidden: false, // make the status bar hidden regardless of nav bar state
  statusBarTextColorScheme: 'dark', // text color of status bar, 'dark' / 'light' (remembered across pushes)
  navBarSubtitleColor: 'red', // subtitle color
  navBarSubtitleFontFamily: 'font-name', // subtitle font, 'sans-serif-thin' for example
  navBarSubtitleFontSize: 13, // subtitle font size
  screenBackgroundColor: 'white', // Default screen color, visible before the actual react view is rendered
  orientation: 'portrait' // Sets a specific orientation to a modal and all screens pushed to it. Default: 'auto'. Supported values: 'auto', 'landscape', 'portrait'
  disabledButtonColor: '#ff0000' // chnaged the navigation bar button text color when disabled.
  
  // iOS only
  statusBarTextColorSchemeSingleScreen: 'light', // same as statusBarTextColorScheme but does NOT remember across pushes
  statusBarHideWithNavBar: false, // hide the status bar if the nav bar is also hidden, useful for navBarHidden:true
  statusBarBlur: false, // blur the area under the status bar, works best with navBarHidden:true
  
  disabledBackGesture: false, // default: false. Disable the back gesture (swipe gesture) in order to pop the top screen. 
  disabledSimultaneousGesture: true, // default: true. Disable simultaneous gesture recognition.
  screenBackgroundImageName: '<name of image in Images.xcassets>', // Optional. default screen background image.
  rootBackgroundImageName: '<name of image in Images.xcassets>', // Static while you transition between screens. Works best with screenBackgroundColor: 'transparent'

  navBarButtonFontSize: 20, // Change font size nav bar buttons (eg. the back button) (remembered across pushes)
  navBarButtonFontWeight: '500', // Change font weight nav bar buttons (eg. the back button) (remembered across pushes)

  navBarLeftButtonFontSize: 17, // Change font size of left nav bar button
  navBarLeftButtonColor: 'red', // Change color of left nav bar button
  navBarLeftButtonFontWeight: '400', // Change font weight of left nav bar button

  navBarRightButtonFontSize: 17, // Change font size of right nav bar button
  navBarRightButtonColor: 'blue', // Change color of right nav bar button
  navBarRightButtonFontWeight: '600', // Change font weight of right nav bar button
  
  // Android only
  navigationBarColor: '#000000', // change the background color of the bottom native navigation bar.
  navBarTitleTextCentered: true, // default: false. centers the title.
  navBarButtonFontFamily: 'sans-serif-thin', // Change the font family of textual buttons
  topBarElevationShadowEnabled: false, // default: true. Disables TopBar elevation shadow on Lolipop and above
  statusBarColor: '#000000', // change the color of the status bar.
  collapsingToolBarImage: "http://lorempixel.com/400/200/", // Collapsing Toolbar image.
  collapsingToolBarImage: require('../../img/topbar.jpg'), // Collapsing Toolbar image. Either use a url or require a local image.
  collapsingToolBarCollapsedColor: '#0f2362', // Collapsing Toolbar scrim color.
  navBarTextFontBold: false, // Optional. Set the title to bold.
  navBarHeight: 70, // Optional, set the navBar height in pixels.
  topTabsHeight: 70, // Optional, set topTabs height in pixels.
  topBarBorderColor: 'red', Optional, set a flat border under the TopBar.
  topBarBorderWidth: 5.5, // Optional, set the width of the border.
}
```

## Styling the StatusBar
If you set any styles related to the Status Bar, make sure that in Xcode > project > Info.plist, the property `View controller-based status bar appearance` is set to `YES`.

## Custom fonts
If you'd like to use a custom font, you'll first have to your project.

* Android - add the `.ttf` or `.otf` files to `src/main/assets/fonts/`

* iOS - follow this [guide](https://medium.com/@dabit3/adding-custom-fonts-to-react-native-b266b41bff7f)

All supported styles are defined [here](https://github.com/wix/react-native-controllers#styling-navigation). There's also an example project there showcasing all the different styles.
