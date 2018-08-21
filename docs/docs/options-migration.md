# Common Options

## navBarTextColor
Title text color

```js
topBar: {
  title: {
    color: 'red'
  }
}
```

## navBarTextFontSize
Title font size

```js
topBar: {
  title: {
    fontSize: 18
  }
}
```

## navBarTextFontFamily
Title font

```js
topBar: {
  title: {
    fontFamily: 'Helvetica'
  }
}
```

## navBarBackgroundColor
TopBar background color

```js
topBar: {
  background: {
    color: 'red'
  }
}  
```

## navBarCustomView
Use a react view as the TopBar's background or use a React view instead of the textual title.

```js
topBar: {
  background: {
    component: {
      name: 'example.CustomTopBarBackground'
    }
  },
  title: {
    component: {
      name: 'example.CustomTopBarTitle'
    }
  }
}
```

## navBarComponentAlignment
Align the React view used as the title

```js
topBar: {
  title: {
    component: {
      name: 'example.CustomTopBarTitle',
      alignment: 'center' | 'fill'
    }
  }
}  
```

## navBarCustomViewInitialProps
Initial props passed to the React component

```js
topBar: {
  background: {
    component: {
      name: 'example.CustomTopBarBackground',
      passProps: {}
    }
  },
  title: {
    component: {
      name: 'example.CustomTopBarTitle',
      passProps: {}
    }
  }
} 
```

## navBarButtonColor
TopBar button color

```js
topBar: {
  rightButtons: [
    {
      color: 'red'
    }
  ],
  leftButtons: [
    {
      color: 'red'
    }
  ],
  backButton: {
    color: 'red'
  }
}
```

## navBarHidden
TopBar visibility. When setting `visible: false`, you probably want to set `drawBehind: true` as well. Use `animate: false` to toggle visibility without animation.

```js
topBar: {
  visible: false
} 
```

## navBarTransparent
Transparent TopBar. Set `drawBehind: true` to draw the screen behind the transparent TopBar.

```js
topBar: {
  transparent: true
}  
```

## drawUnderNavBar
Draw the screen behind the TopBar, Useful when the TopBar is toggled or transparent

```js
topBar: {
  drawBehind: true
}  
```

## drawUnderTabBar
Draw the screen behind the BottomTabs, Useful when toggling BottomTabs or when the BottomTabs are translucent.

```js
bottomTabs: {
  drawBehind: true
}  
```

## tabBarHidden
BottomTabs visibility.
```js
bottomTabs: {
  visible: false
}  
```

The BottomTab's visibility can be toggled only on **Android** using `mergeOptions`:
```js
Navigation.mergeOptions(this.props.componentId, {
  bottomTabs: {
    visible: false
  }
});
```
On **iOS**, BottomTab visibility can be changed only when pushing screens. This means that if you'd like to hide BottomTabs when pushing a screen, You'll need to set the property to `false` in the options passed to the `push` command or via the `static options(passProps) {}` api.

## statusBarHidden
StatusBar visibility

```js
statusBar: {
  visible: false
}  
```

## statusBarTextColorScheme
Theme of text and icons displayed in the StatusBar

```js
statusBar: {
  style: 'light' | 'dark'
}
```

## navBarSubtitleColor
Subtitle color

```js
topBar: {
  subtitle: {
    color: 'red'
  }
}
```

## navBarSubtitleFontFamily
Subtitle font

```js
topBar: {
  subtitle: {
    fontFamily: 'Helvetica'
  }
}
```

## navBarSubtitleFontSize
Subtitle font size

```js
topBar: {
  subtitle: {
    fontSize: 14
  }
}
```

## screenBackgroundColor
Screen color, visible before the actual React view is rendered

```js
layout: {
  backgroundColor: 'red'
}  
```

## orientation

```js
layout: {
  orientation: ['portrait', 'landscape'] // An array of supported orientations
}
```

## disabledButtonColor
Button color when `enabled: false` is used

```js
topBar: {
  rightButtons: [
    {
      disabledColor: 'grey'
    }
  ]
}
```

## navBarButtonFontSize
Button font size

```js
topBar: {
  rightButtons: [
    {
      fontSize: 13
    }
  ],
  leftButtons: [
    {
      fontSize: 13
    }
  ]
}  
```

## navBarLeftButtonFontSize
Left button font size

```js
{
  topBar: {
    leftButtons: [
      {
        fontSize: 13
      }
    ]
  }
}
```

## navBarLeftButtonColor
Left button color

```js
{
  topBar: {
    leftButtons: [
      {
        color: 'red'
      }
    ]
  }
}
```

## navBarLeftButtonFontWeight
Left button font weight

```js
{
  topBar: {
    leftButtons: [
      {
        weight: '300'
      }
    ]
  }
}  
```

## navBarRightButtonFontSize
Right button font size

```js
topBar: {
  leftButtons: [
    {
      fontSize: 13
    }
  ]
}
```

## navBarRightButtonColor
Right button color

```js
topBar: {
  rightButtons: [
    {
      color: 'red'
    }
  ]
}
```

## navBarRightButtonFontWeight
Right button font weight

```js
topBar: {
  rightButtons: [
    {
      weight: '400'
    }
  ]
} 
```

## modalPresentationStyle
Controls he behavior of screens displayed modally. 

### Options supported on iOS
* overCurrentContext - Content is displayed over the previous screen. Useful for **transparent modals**
* `formSheet` - Content is centered in the screen
* `pageSheet` -Content partially covers the underlying content
* `overFullScreen` - Content covers the screen, without detaching previous content.
* `fullScreen` - Content covers the screen, previous content is detached.

### Options supported on Android
* `overCurrentContext` - Content is displayed over the previous screen. Useful for **transparent modals**
* `none` - Previous content is detached when the Modal's show animation ends

```js
{
  modalPresentationStyle: 'overCurrentContext'
}
```

## navBarButtonFontFamily
Button font family

```js
topBar: {
  rightButtons: [
    {
      fontFamily: 'Helvetica'
    }
  ]
}
```

# iOS only

## navBarHideOnScroll
Hide TopBar when list is scrolled

```js
topBar: {
  hideOnScroll: true
}
```

## navBarTranslucent
Translucent TopBar, Setting `drawBehind: true` is required for this property to work as expected.

```js
topBar: {
  translucent: true
}
```

## navBarNoBorder
Remove TopBar border (hair line)

```js
topBar: {
  noBorder: true
}  
```

## navBarBlur
Blue the area behind the TopBar, Setting `drawBehind: true` is required for this property to work as expected.

```js
topBar: {
  blur: true
}  
```

## rootBackgroundImageName

* iOS: name of image in Images.xcassets
* Android: name of drawable

```js
{
  rootBackgroundImage: require('rootBackground.png')
}
```

## screenBackgroundImageName
name of image in Images.xcassets

```js
{
  backgroundImage: require('background.png')
}
```

## statusBarHideWithNavBar
Hide the StatusBar if the TopBar is also hidden

```js
statusBar: {
  hideWithTopBar: true
}
```

## statusBarBlur
Blur the area behind the StatusBar

```js
statusBar: {
  blur: true
}  
```

## disabledBackGesture
Disable the back (swipe) gesture used to pop screens 

```js
{
  popGesture: false
} 
```

## largeTitle
Use iOS 11 large title

```js
  topBar: {
    largeTitle: {
      visible: true,
      fontSize: 30,
      color: 'red',
      fontFamily: 'Helvetica'
    }
  }
```

# Android Options

## topBarElevationShadowEnabled
TopBar elevation in dp. Set this value to `0` to disable the TopBa's shadow.

```js
topBar: {
  elevation: 0
}
```

## navBarTitleTextCentered
Title alignment

```js
topBar: {
  alignment: 'center'|'fill'
}
```

## statusBarColor
StatusBar color

```js
statusBar: {
  backgroundColor: 'red'
}
```

## drawUnderStatusBar
Draw content behind the StatusBar

```js
statusBar: {
  drawBehind: true
}
```

## navBarHeight
TopBar height in dp

```js
topBar: {
  height: 70
}
```

## navBarTopPadding
Content top margin

```js
layout: {
    topMargin: 26
  }
```

## topTabsHeight
TopTabs height

```js
topTabs: {
  height: 70
}
```

## topBarBorderColor
TopBar border color

```js
topBar: {
  borderColor: 'red'
}
```

## topBarBorderWidth
TopBar border height

```js
topBar: {
  borderHeight: 1.3
} 
```

# Unsupported options
* disabledSimultaneousGesture
* statusBarTextColorSchemeSingleScreen
* navBarButtonFontWeight
* topBarShadowColor
* topBarShadowOpacity
* topBarShadowOffset
* topBarShadowRadius
* preferredContentSize
* navigationBarColor
* navBarSubTitleTextCentered
* collapsingToolBarImage
* collapsingToolBarCollapsedColor
* navBarTextFontBold
