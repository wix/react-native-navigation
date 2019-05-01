# Button options

The following options can be used to customise buttons.

```js
{
  id: 'buttonOne',
  icon: require('icon.png'),
  component: {
    name: 'example.CustomButtonComponent'
  },
  text: 'Button one',
  enabled: true,
  disableIconTint: false,
  color: 'red',
  disabledColor: 'black',
  testID: 'buttonOneTestID',
  // Android only
  showAsAction: 'ifRoom', // See below for details.
  // iOS only
  systemItem: 'done', // Sets a system bar button item as the icon. Matches UIBarButtonSystemItem naming. See below for details.
}
```

## iOS System Items
On iOS, UIKit supplies some common bar button glyphs for developers to use. The following values can be supplied as values to to `systemItem` to use them as an icon for your button.

* `done`
* `cancel`
* `edit`
* `save`
* `add`
* `flexibleSpace`
* `fixedSpace`
* `compose`
* `reply`
* `action`
* `organize`
* `bookmarks`
* `search`
* `refresh`
* `stop`
* `camera`
* `trash`
* `play`
* `pause`
* `rewind`
* `fastForward`
* `undo`
* `redo`

More information about these glyphs can be found in [Apple's Human Interface Guidelines](https://developer.apple.com/ios/human-interface-guidelines/icons-and-images/system-icons/).


## Android showAsAction

It is keyword witch configure when and how this item should appear as an action item in the app bar. 
A menu item can appear as an action item only when the activity includes an app bar.

* `always`
* `ifRoom`
* `never`
* `withText`

[More info about `showAsAction` values](https://developer.android.com/guide/topics/resources/menu-resource)

# Declaring Buttons statically

Buttons can be defined in a screen's options:

```js
class MyScreen extends Component {
  static options(passProps) {
    return {
      topBar: {
        leftButtons: [
          {
            id: 'buttonOne',
            icon: require('icon.png')
          }
        ],
        rightButtons: [],
      }
    };
  }
  
}
```

# Declaring buttons dynamically

TopBar buttons can be declared dynamically as well when adding a screen to the layout hierarchy.

```js
Navigation.push(this.props.componentId, {
  component: {
    name: 'navigation.playground.PushedScreen',
    options: {
      topBar: {
        rightButtons: [
          {
            id: 'buttonOne',
            icon: require('icon.png')
          }
        ]
      }
    }
  }
}
```

# Handling button press events

Navigation sends events on button clicks, to which you can subscribe from anywhere using `Navigation.events().registerNavigationButtonPressedListener((event) => {})`.
Additionally the component can listen to the button clicks just for its own buttons (via componentId) by using `events().bindComponent(this)`.
This has to be called if you want the component to handle navigation events, such as navigationButtonPressed.
Example:

```js
class MyScreen extends Component {
  static options(passProps) {
    return {
      topBar: {
        rightButtons: {
          id: 'buttonOne',
          icon: require('icon.png')
        }
      }
    };
  }

  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this); // <== Will be automatically unregistered when unmounted
  }

  navigationButtonPressed({ buttonId }) {
    // will be called when "buttonOne" is clicked
  }
}
```

# Modifying buttons at runtime

As buttons are part of a screen's options, they can be modified like any other styling option using the `mergeOptions` command.

## Setting buttons

The following command will set the screen's right buttons. If the screen already has Right Buttons declared - they will be overridden.

```js
Navigation.mergeOptions(this.props.componentId, {
  topBar: {
    rightButtons: [
      {
        id: 'myDynamicButton',
        text: 'My Button'
      }
    ]
  }
});
```

## Removing buttons

Buttons can be removed by setting zero buttons, as shown in the snippet below.

```js
Navigation.mergeOptions(this.props.componentId, {
  topBar: {
    rightButtons: []
  }
});
```
