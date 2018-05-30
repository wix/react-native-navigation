# Styling Options

You can style the navigator appearance and behavior by passing an `options` object. This object can be passed when the screen is originally created; can be defined per-screen by setting `static get options()` on the screen component; and can be overridden when a screen is pushed.

The easiest way to style your screen is by adding `static get options()` to your screen React component definition.

```js
export default class StyledScreen extends Component {
  static get options() {
    return {
      topBar: {
        title: {
          largeTitle: false,
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

```js
Navigation.setDefaultOptions({
  topBar: {
    visible: false
  }
});
```

## Setting styles dynamically
Use the `mergeOptions` method to change a screen's style dynamically.

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
    backgroundColor: 'white',
    orientation: ['portrait', 'landscape'] // An array of supported orientations
  },
  modalPresentationStyle: 'overCurrentContext', // Supported styles are: 'formSheet', 'pageSheet', 'overFullScreen', 'overCurrentContext', 'currentContext', 'popOver', 'fullScreen' and 'none'. On Android, only overCurrentContext and none are supported.
  topBar: {
    visible: true,
    animate: false, // Controls wether TopBar visibility changes should be animated
    hideOnScroll: true,
    buttonColor: 'black',
    drawBehind: false,
    testID: 'topBar',
    component: {
      name: 'example.CustomTopBar'
    },
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
    background: {
      color: '#00ff00',
      component: {
        name: 'example.CustomTopBarBackground'
      }
    }
  },
  bottomTabs: {
    visible: true,
    animate: false, // Controls wether BottomTabs visibility changes should be animated
    currentTabIndex: 0,
    currentTabId: 'currentTabId',
    testID: 'bottomTabsTestID',
    drawBehind: false,
    backgroundColor: 'white',
    tabColor: 'red',
    selectedTabColor: 'blue',
    fontFamily: 'Helvetica',
    fontSize: 10
  },
  bottomTab: {
    title: 'Tab 1',
    badge: '2',
    testID: 'bottomTabTestID',
    icon: require('tab.png')
  },
  sideMenu: {
    left: {
      visible: false,
      enabled: true
    },
    right: {
      visible: false,
      enabled: true
    }
  },
  overlay: {
    interceptTouchOutside: true
  },
  preview: {
    elementId: 'PreviewId',
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
    translucent: true,
    transparent: false,
    noBorder: false,
    blur: false,
    largeTitle: false,
    backButtonImage: require('icon.png'),
    backButtonHidden: false,
    backButtonTitle: 'Back',
    hideBackButtonTitle: false,
  },
  bottomTabs: {
    translucent: true,
    hideShadow: false
  },
  bottomTab: {
    iconInsets: { top: 0, left: 0, bottom: 0, right: 0 }
  }
}
```

### Android specific options

```js
{
  statusBar: {
    backgroundColor: 'red'
  },
  topBar: {
    height: 70, // TopBar height in dp.
    borderColor: 'red',
    borderHeight: 1.3,
    title: {
      height: 70 // TitleBar height in dp.
    }
  }
}
```

## Styling the StatusBar
If you set any styles related to the Status Bar, make sure that in Xcode > project > Info.plist, the property `View controller-based status bar appearance` is set to `YES`.

## Custom fonts
If you'd like to use a custom font, you'll first have to edit your project.

* Android - add the `.ttf` or `.otf` files to `src/main/assets/fonts/`

* iOS - follow this [guide](https://medium.com/@dabit3/adding-custom-fonts-to-react-native-b266b41bff7f)

All supported styles are defined [here](https://github.com/wix/react-native-controllers#styling-navigation). There's also an example project there showcasing all the different styles.
