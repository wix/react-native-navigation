# Android Specific Use Cases

TOC
* [Activity Lifecycle and onActivityResult](https://github.com/wix/react-native-navigation/wiki/Android#activity-lifecycle-and-onactivityresult)
* [Adjusting soft input mode](https://github.com/wix/react-native-navigation/wiki/Android/#adjusting-soft-input-mode)
* [Splash screen](https://github.com/wix/react-native-navigation/wiki/Android/#splash-screen)
* [Collapsing React header](https://github.com/wix/react-native-navigation/wiki/Android/#collapsing-react-header---android-only)
  * [Collapsing react view](https://github.com/wix/react-native-navigation/wiki/Android/#collapsing-react-view)
  * [Collapsing react view with top tabs](https://github.com/wix/react-native-navigation/wiki/Android/_edit#collapsing-react-view-with-top-tabs)
* [Reloading from terminal](https://github.com/wix/react-native-navigation/wiki/Android/#reloading-from-terminal)

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

# Collapsing React header
A screen can have a header, either an image or a react component, that collapses as the screen is scrolled.

## Collapsing react view

```js
export default class CollapsingReactViewScreen extends Component {
static navigatorStyle = {
    navBarHideOnScroll: false,
    navBarBackgroundColor: '#4dbce9', // This will be the TitleBars color when the react view is hidden and collapsed
    collapsingToolBarComponent: 'example.header',
    navBarTranslucent: true, // Optional, sets a translucent dark background to the TitleBar. Useful when displaying bright colored header to emphasize the title and buttons in the TitleBar
    showTitleWhenExpended: false, // default: true. Show the screens title only when the toolbar is collapsed
    collapsingToolBarCollapsedColor: 'green', // optional. The TitleBar (navBar) color in collapsed state
    collapsingToolBarExpendedColor: 'red' // optional. The TitleBar (navBar) color in expended state
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
# Shared Element Transition
Screen transitions provide visual connections between different states through motion and transformations between common elements. You can specify custom animations for transitions of shared elements between screens.

The `<SharedElementTransition>` component determines how views that are shared between two screens transition between these screens. For example, if two screens have the same image in different positions and sizes, the `<SharedElementTransition>` will translate and scale the image smoothly between these screens.

## Supported transitions
* Scale
* Text color
* Linear translation
* Curved motion translation
* Image bounds and scale transformation - Unlike the basic scale transformation, this transformation will change the actual image scale and bounds, instead of simply scaling it up or down.

## Specifying shared elements
First, wrap the view you would like to transition in a `<SharedElementTransition/>` and give it a unique id. This is how our `<Text/>` element is defined in the first screen:

```js
<SharedElementTransition sharedElementId={'SharedTextId'}>
    <Text style={{color: 'blue'}}>React Native Navigation</Text>
</SharedElementTransition>
```
<br>In the second screen, we also wrap the corresponding `<Text/>` element but this time, we also specify the transition props:

```js
<SharedElementTransition
    sharedElementId={'SharedTextId'}
    showDuration={600}
    hideDuration={400}
    showInterpolation={
          {
            type: 'linear',
            easing: 'FastOutSlowIn'
          }
        }
    hideInterpolation={
          {
            type: 'linear',
            easing:'FastOutSlowIn'
          }
        }
  >
    <Text style={{color: 'green'}}>React Native Navigation</Text>
  </SharedElementTransition>
</View>
```
<br>Finally, specify the elements you'd like to transition when pushing the second screen:

```js
this.props.navigator.push({
    screen: 'SharedElementScreen',
    sharedElements: ['SharedTextId']
  }
});
```
## Animating image bounds and scale
By default, when animating images, a basic scale transition is used. This is good enough for basic use cases where both images have the same aspect ratio. If the images have different size and scale, you can animate their bounds and scale by setting `animateClipBounds={true}` on the final `<SharedElementTransition/>` element.

## Curved motion
The `path` interpolator transitions elements along a curved path based on Bézier curves. This interpolator specifies a motion curve in a 1x1 square, with anchor points at (0,0) and (1,1) and control points specified using the `showInterpolation` and `hideInterpolation` props.

### Using curved motion
First, wrap the view you would like to transition in a `<SharedElementTransition/>` and give it a unique id. In this example we are going to transition an `<Image/>'.

```js
<SharedElementTransition sharedElementId={'sharedImageId'}>
    <Image
        style={{height: 50, width: 50}}
        source={this.props.image}/>
</SharedElementTransition>
```

<br>In the `<SharedElementTransition/>` wrapping the Image in the second screen, define control points in `showInterpolation` and `hideInterpolation` props:

```js
<SharedElementTransition
  sharedElementId={'sharedImageId'}
  showDuration={600}
  hideDuration={400}
  showInterpolation={
    {
      type: 'path',
      controlX1: '0.5',
      controlY1: '1',
      controlX2: '0',
      controlY2: '0.5',
      easing: 'FastOutSlowIn'
    }
  }
  hideInterpolation={
    {
      type: 'path',
      controlX1: '0.5',
      controlY1: '0',
      controlX2: '1',
      controlY2: '0.5',
      easing:'FastOutSlowIn'
    }
  }
>
  <Image
    style={{height: 100, width: 100}}
    source={this.props.image}
    fadeDuration={0} // Disable react-native's default 300 ms fade-in animation
  />
</SharedElementTransition>
```

<br>As in the previous example, specify the elements you'd like to transition when pushing the second screen:

```js
this.props.navigator.push({
    screen: 'SharedElementScreen',
    sharedElements: ['sharedImageId']
  }
});
```

## Easing
specify the rate of change of a parameter over time

* `accelerateDecelerate` - the rate of change starts and ends slowly but accelerates through the middle.
* `accelerate` - the rate of change starts out slowly and and then accelerates.
* `decelerate` - the rate of change starts out quickly and and then decelerates.
* `fastOutSlowIn` - the rate of change starts fast but decelerates slowly.
* `linear` - the rate of change is constant (default)

## Screen animation
When Shared Element Transition is used, a cross-fade transition is used between the entering and exiting screens. Make sure the root `View` has a background color in order for the cross-fade animation to be visible.

To disable the corss-fade animation, set `animated: false` when pushing the second screen. Disabling this animation is useful if you'd like to animate the reset of the elements on screen your self.

# Reloading from terminal
You can easily reload your app from terminal using `adb shell am broadcast -a react.native.RELOAD`. This is particularly useful when debugging on device.