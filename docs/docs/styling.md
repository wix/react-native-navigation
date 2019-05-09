# Styling Options

You can style the navigator appearance and behavior by passing an `options` object. This object can be passed when the screen is originally created; can be defined per-screen by setting `static options(passProps)` on the screen component; and can be overridden when a screen is pushed, dynamically (after the screen was already rendered at least once) using `mergeOptions()`.

The easiest way to style your screen is by adding `static options(passProps)` to your screen React component definition. `passProps` is the same passProps you can specify as part of the push/modal or other command operation.

```js
export default class StyledScreen extends Component {
  static options(passProps) {
    return {
      topBar: {
        title: {
          text: 'My Screen'
        },
        drawBehind: true,
        visible: false,
        animate: false
      }
    };
  }

  constructor(props) {
    super(props);
  }
  render() {
    return (
      <View style={{flex: 1}}>...</View>
     );
  }
```

## Enabling persistent styling properties
In v2 we added `setDefaultOptions` API for styles that should be applied on all components.

> `setDefaultOptions` Does not update options of existing component, therefore it should be called before `setRoot`

```js
Navigation.setDefaultOptions({
  topBar: {
    visible: false
  }
});
```

## Setting styles dynamically
Use the `mergeOptions` method to change a screen's style dynamically. WARNING! these options will be applied on an already rendered screen, after it has been rendered at least once.

```js
Navigation.mergeOptions(this.props.componentId, {
  topBar: {
    visible: true
  }
});
```

## Options object format

### Common options

```js
{
  statusBar: {
    visible: false,
    style: 'light' | 'dark'
  },
  layout: {
    direction: 'ltr', // Supported directions are: 'rtl', 'ltr'
    backgroundColor: 'white',
    orientation: ['portrait', 'landscape'] // An array of supported orientations
  },
  modalPresentationStyle: 'overCurrentContext', // Supported styles are: 'formSheet', 'pageSheet', 'overFullScreen', 'overCurrentContext', 'currentContext', 'popover', 'fullScreen' and 'none'. On Android, only overCurrentContext and none are supported.
  topBar: {
    visible: true,
    animate: false, // Controls whether TopBar visibility changes should be animated
    hideOnScroll: true,
    leftButtonColor: 'black',
    rightButtonColor: 'black',
    drawBehind: false,
    testID: 'topBar',
    title: {
      text: 'Title',
      fontSize: 14,
      color: 'red',
      fontFamily: 'Helvetica',
      component: {
        name: 'example.CustomTopBarTitle',
        alignment: 'center'
      }
    },
    subtitle: {
      text: 'Title',
      fontSize: 14,
      color: 'red',
      fontFamily: 'Helvetica',
      alignment: 'center'
    },
    backButton: {
      icon: require('icon.png'),
      visible: true
    },
    background: {
      color: '#00ff00',
      component: {
        name: 'example.CustomTopBarBackground'
      }
    }
  },
  bottomTabs: {
    visible: true,
    animate: false, // Controls whether BottomTabs visibility changes should be animated
    currentTabIndex: 0,
    currentTabId: 'currentTabId',
    testID: 'bottomTabsTestID',
    drawBehind: false,
    backgroundColor: 'white'
  },
  bottomTab: {
    text: 'Tab 1',
    badge: '2',
    badgeColor: 'red',
    testID: 'bottomTabTestID',
    icon: require('tab.png'),
    iconColor: 'red',
    selectedIconColor: 'blue',
    textColor: 'red',
    selectedTextColor: 'blue',
    fontFamily: 'Helvetica',
    fontSize: 10
  },
  sideMenu: {
    left: {
      width: 260,
      height: 270,
      visible: false,
      enabled: true
    },
    right: {
      width: 260,
      height: 270,
      visible: false,
      enabled: true
    }
  },
  overlay: {
    interceptTouchOutside: true,
    handleKeyboardEvents: true
  },
  preview: {
    reactTag: 0, // result from findNodeHandle(ref)
    width: 100,
    height: 100,
    commit: false,
    actions: [{
      id: 'ActionId1',
      title: 'Action title',
      style: 'selected', // default, selected, destructive,
      actions: [/* ... */]
    }]
  }  
}
```

### iOS specific options
```js
{
  statusBar: {
    hideWithTopBar: false,
    blur: true
  },
  popGesture: true,
  backgroundImage: require('background.png'),
  rootBackgroundImage: require('rootBackground.png'),
  topBar: {
    barStyle: 'default' | 'black',
    background: {
      color: 'white',
      translucent: true,
      blur: false
    }
    noBorder: false,
    backButton: {
      title: 'Back',
      showTitle: false
    },
    searchBar: true, // iOS 11+ native UISearchBar inside topBar
    searchBarHiddenWhenScrolling: true,
    searchBarPlaceholder: 'Search', // iOS 11+ SearchBar placeholder
    largeTitle: {
      visible: true,
      fontSize: 30,
      color: 'red',
      fontFamily: 'Helvetica'
    },
  },
  sideMenu: {
    left: {
      shouldStretchDrawer: false, // defaults to true, when false sideMenu contents not stretched when opened past the width
      animationVelocity: 2500 // defaults to 840, high number is a faster sideMenu open/close animation
    },
    right: {
      shouldStretchDrawer: false, // defaults to true, when false sideMenu contents not stretched when opened past the width
      animationVelocity: 2500 // defaults to 840, high number is a faster sideMenu open/close animation
    },
    animationType: 'parallax', // defaults to none if not provided, options are 'parallax', 'door', 'slide', or 'slide-and-scale'    
    openGestureMode: 'entireScreen' | 'bezel'
  }
  bottomTabs: {
    barStyle: 'default' | 'black',
    translucent: true,
    hideShadow: false
  },
  bottomTab: {
    iconInsets: { top: 0, left: 0, bottom: 0, right: 0 },
    selectedIcon: require('selectedTab.png'),
    disableIconTint: true, //set true if you want to disable the icon tinting
    disableSelectedIconTint: true
  }
}
```

### Android specific options

```js
{
  statusBar: {
    backgroundColor: 'red',
    drawBehind: true,
    visible: false
  },
  layout: {
    topMargin: (await Navigation.constants()).statusBarHeight, // Set the layout's top margin
    orientation: ['portrait', 'landscape'] | ['sensorLandscape'], // An array of supported orientations
    componentBackgroundColor: 'red' // Set background color only for components, helps reduce overdraw if background color is set in default options.
  },
  topBar: {
    height: 70, // TopBar height in dp
    backButton: {
      color: 'red'
    },
    borderColor: 'red',
    borderHeight: 1.3,
    elevation: 1.5, // TopBar elevation in dp
    topMargin: 24, // top margin in dp
    title: {
      height: 70, // TitleBar height in dp
      alignment: 'center', // Center title
    }
  },
  bottomTabs: {
    elevation: 8, // BottomTabs elevation in dp
    titleDisplayMode: 'alwaysShow' | 'showWhenActive' | 'alwaysHide' // Sets the title state for each tab.
  },
  bottomTab: {
    selectedFontSize: 19 // Selected tab font size in sp
  }
}
```

### RTL layout usage
In order to set layout direction to RTL use following options:
```javascript
{
  layout: {
    direction: rtl
  },
  ...
}
```

also __Android__ requires to set `supportsRTL` in _AndroidManifest.xml_
```xml
<application
      android:name=".MainApplication"
+     android:supportsRtl="true"
      ...
      android:theme="@style/AppTheme">
```

## Styling the StatusBar
If you set any styles related to the Status Bar, make sure that in Xcode > project > Info.plist, the property `View controller-based status bar appearance` is set to `YES`.

## Custom fonts
If you'd like to use a custom font, you'll first have to edit your project.

* Android - add the `.ttf` or `.otf` files to `src/main/assets/fonts/`

* iOS - follow this [guide](https://medium.com/@dabit3/adding-custom-fonts-to-react-native-b266b41bff7f)

## Custom tab icons

* Android - add corresponding resolution icons into folders in `android/app/src/main/res`.
For example, `icon_name.png` in each drawable-x folder.
* iOS - drag and drop to `Images.xcassets` in Xcode.
For example, image set `icon_name` in `Images.xcassets` with x1, x2, x3.

Then, the tab icon can be defined with the following syntax:

```js
bottomTab: {
  icon: {
    uri: 'icon_name',
    ...
  },
  ...
}
```

## Customizing screen animations
Animation used for navigation commands that modify the layout hierarchy can be controlled in options. Animations can be modified per command and it's also possible to change the default animation for each command.

## Animation properties

The following properties can be animated:
* x
* y
* alpha
* scaleX
* scaleY
* rotationX
* rotationY
* rotation

```js
{
  from: 0, // Mandatory, initial value
  to: 1, // Mandatory, end value
  duration: 400, // Default value is 300 ms
  startDelay: 100, // Default value is 0
  interpolation: 'accelerate' | 'decelerate' // Optional
}
```

For example, changing the animation used when the app is first launched (Supported only on Android):
```js
Navigation.setDefaultOptions({
  animations: {
    setRoot: {
      enabled: 'true' | 'false', // Optional, used to enable/disable the animation
      alpha: {
        from: 0,
        to: 1,
        duration: 400,
        startDelay: 100,
        interpolation: 'accelerate'
      }
    }
  }
});
```

## Customizing navigation commands animation

Animations for the following set of commands can be customized
* setRoot
* push
* pop
* showModal
* dismissModal

## Customizing stack command animation

When *pushing* and *popping* screens to and from a stack, you can control the TopBar, BottomTabs and actual content animations as separately.

```js
animations: {
  push: {
    enabled: 'true' | 'false', // Optional, used to enable/disable the animation
    topBar: {
      id: 'TEST', // Optional, id of the TopBar we'd like to animate.
      alpha: {
        from: 0,
        to: 1
      }
    },
    bottomTabs: {
      alpha: {
        from: 0,
        to: 1
      }
    },
    content: {
      alpha: {
        from: 0,
        to: 1
      }
    }
  },
  pop: {
    ...
  }
}
```
