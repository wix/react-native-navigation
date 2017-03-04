# Android Specific Use Cases

TOC
* [Activity Lifecycle and onActivityResult](https://github.com/wix/react-native-navigation/wiki/Android#activity-lifecycle-and-onactivityresult)
* [Adjusting soft input mode](https://github.com/wix/react-native-navigation/wiki/Android/_edit#adjusting-soft-input-mode)
* [Splash screen](https://github.com/wix/react-native-navigation/wiki/Android/_edit#splash-screen)
* [Collapsing React header](https://github.com/wix/react-native-navigation/wiki/Android/_edit#collapsing-react-header---android-only)
  * [Collapsing react view](https://github.com/wix/react-native-navigation/wiki/Android/_edit#collapsing-react-view)
  * [Collapsing react view with top tabs](https://github.com/wix/react-native-navigation/wiki/Android/_edit#collapsing-react-view-with-top-tabs)
* [Reloading from terminal](https://github.com/wix/react-native-navigation/wiki/Android/_edit#reloading-from-terminal)

# Activity Lifecycle and onActivityResult
In order to listen to activity lifecycle callbacks, set `ActivityCallback` in `MainApplication.onCreate` as follows:

```java
public class MainApplication extends NavigationApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        setActivityCallbacks(new ActivityCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                
            }

            @Override
            public void onActivityStarted(Activity activity) {
                
            }

            @Override
            public void onActivityResumed(Activity activity) {
                
            }

            @Override
            public void onActivityPaused(Activity activity) {
                
            }

            @Override
            public void onActivityStopped(Activity activity) {
                
            }

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                
            }
        });
    }
}
```

## Adjusting soft input mode

```java
public class MyApplication extends NavigationApplication {
    @Override
    public void onCreate() {
        registerActivityLifecycleCallbacks(new LifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
    }
}
```

## Why overriding these methods in `MainActivity` won't work
`MainActivity` extends `SplashActiviy` which is used to start the react context. Once react is up and running `MainActivity` is **stopped** and another activity takes over to run our app: `NavigationActivity`. Due to this design, there's usually no point in overriding lifecycle callbacks in `MainActivity`.

# Splash screen
Override `getSplashLayout` or `createSplashLayout` in `MainActivity` to provide a splash layout which will be displayed while Js context initialises.

# Collapsing React header - Android only
A screen can have a header, either an image or a react component, that collapses as the screen is scrolled.

## Collapsing react view

```js
export default class CollapsingReactViewScreen extends Component {
static navigatorStyle = {
    navBarHideOnScroll: false,
    navBarBackgroundColor: '#4dbce9', // This will be the TitleBars color when the react view is hidden and collapsed
    collapsingToolBarComponent: 'example.header',
    navBarTranslucent: true // Optional, sets a translucent dark background to the TitleBar. Useful when displaying bright colored header to emphasize the title and buttons in the TitleBar
  };
}
```

## Collapsing react view with top tabs

**Note:** `example.header` represents a component that's registered as a screen:
```js
import Header  from './Header';
Navigation.registerComponent('example.header', () => Header);
```

```js
export default class CollapsingReactViewTopTabsScreen extends Component {
  static navigatorStyle = {
    navBarHideOnScroll: false, // false, since we collapse the TopBar and the TitleBar remains visible with the top tabs
    topBarCollapseOnScroll: true,
    navBarBackgroundColor: '#4dbce9', // This will be the TitleBar's color when the react view is hidden and collapsed
    collapsingToolBarComponent: 'example.header', // id used to register the component
    expendCollapsingToolBarOnTopTabChange: false, // Don't expend the TopBar when selected TopTab changes
    collapsingToolBarCollapsedColor: '#4dbce9' // Optional, use this property with navBarTranslucent: true to animate between translucent and solid color title bar color
  };
}
```

Specify `topTab` in the screen object you use when starting your app:

```js
Navigation.startSingleScreenApp({
    screen: {
    screen: 'example.collapsingReactViewTopTabsScreen',
    title: 'Collapsing React TopTabs View',
    topTabs: [
      {
        screenId: 'example.ListScreen',
        icon: require('../img/list.png')
      },
      {
        screenId: 'example.secondTabScreen',
        icon: require('../img/list.png')
      }
    ]
});
```

# Reloading from terminal
You can easily reload your app from terminal using `adb shell am broadcast -a react.native.RELOAD`. This is particularly useful when debugging on device.