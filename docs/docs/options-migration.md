# Common Options

## navBarTextColor
```js
topBar: {
  title: {
    color: 'red'
  }
}
```

## navBarTextFontSize
```js
topBar: {
  title: {
    fontSize: 'red'
  }
}
```

## navBarTextFontFamily
```js
topBar: {
  title: {
    fontFamily: 'Helvetica'
  }
}
```

## navBarBackgroundColor
```js
topBar: {
  background: {
    color: 'red'
  }
}  
```

## navBarCustomView
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
```js
topBar: {
  title: {
    component: {
      name: 'example.CustomTopBarTitle',
      alignment: 'center'
    }
  }
}  
```

## navBarCustomViewInitialProps
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
```js
topBar: {
  rightButtons: [
    {
      color: 'red'
    }
  ]
}
```

## topBarElevationShadowEnabled
```js
topBar: {
  elevation: 0
}
```

## navBarHidden
```js
topBar: {
  visible: false
} 
```

## navBarTransparent
```js
topBar: {
  transparent: true
}  
```

## drawUnderNavBar
```js
topBar: {
  drawBehind: true
}  
```

## drawUnderTabBar
```js
bottomTabs: {
  drawBehind: true
}  
```

## tabBarHidden
```js
bottomTabs: {
  visible: false
}  
```

## statusBarHidden
```js
statusBar: {
  visible: false
}  
```

## statusBarTextColorScheme
```js
statusBar: {
  style: 'light' | 'dark'
}
```

## navBarSubtitleColor
```js
topBar: {
  subtitle: {
    color: 'red'
  }
}
```

## navBarSubtitleFontFamily
```js
topBar: {
  subtitle: {
    fontFamily: 'Helvetica'
  }
}
```

## navBarSubtitleFontSize
```js
topBar: {
  subtitle: {
    fontSize: 14
  }
}
```

## screenBackgroundColor
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
```js
topBar: {
  rightButtons: [
    {
      fontSize: 13
    }
  ]
}  
```

## navBarLeftButtonFontSize
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
```js
{
modalPresentationStyle: 'overCurrentContext' // Supported styles are: 'formSheet', 'pageSheet', 'overFullScreen', 'overCurrentContext', 'currentContext', 'popOver', 'fullScreen' and 'none'. On Android, only overCurrentContext and none are supported.
}
```

## navBarButtonFontFamily
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
```js
topBar: {
  hideOnScroll: true
}
```

## navBarTranslucent
```js
topBar: {
  translucent: true
}
```

## navBarNoBorder
```js
topBar: {
  noBorder: true
}  
```

## navBarBlur
```js
topBar: {
  blur: true
}  
```

## rootBackgroundImageName
```js
{
  rootBackgroundImage: require('rootBackground.png')
}
```

## statusBarHideWithNavBar
```js
statusBar: {
  hideWithTopBar: true
}
```

## statusBarBlur
```js
statusBar: {
  blur: true
}  
```

## disabledBackGesture
```js
{
  popGesture: false
} 
```

## screenBackgroundImageName
```js
{
  backgroundImage: require('background.png')
}
```

## largeTitle
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

## navBarTitleTextCentered
```js
topBar: {
  alignment: 'center'
}
```

## statusBarColor
```js
statusBar: {
  backgroundColor: 'red'
}
```

## drawUnderStatusBar
```js
statusBar: {
  drawBehind: true
}
```

## navBarHeight
```js
topBar: {
  height: 70, // TopBar height in dp
}
```

## navBarTopPadding
```js
layout: {
    topMargin: 26 // Set the layout's top margin in dp
  }
```

## topTabsHeight
```js
topTabs: {
  height: 70
}
```

## topBarBorderColor
```js
topBar: {
  borderColor: 'red'
}
```

## topBarBorderWidth
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