---
id: sideMenu-disable
title: Disable Open and Close gesture
sidebar_label: Open and Close Gesture
---

To open the side menu programmatically, you'll need to update the visible property of the side menu you'd like to open.

The following snippet shows how to open the left side menu.
```jsx
Navigation.mergeOptions(componentId, {
  sideMenu: {
    left: {
      visible: true,
    },
  },
});
```

## Open by pressing on the hamburger menu
The most common use case is to open the side menu by pressing the hamburger button in the TopBar. To achieve this, listen to the press event of the burger button, and open the side menu as shown above.

```jsx
const React = require('react');
const Navigation = require('react-native-navigation');
const { View, Text } = require('react-native');

class SideMenuCenterScreen extends React.Component {
  static options() {
    return {
      topBar: {
        leftButtons: {
          id: 'sideMenu',
          icon: require('./menuIcon.png')
        }
      }
    };
  }

  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  render() {
    return (
      <View>
        <Text>Click the hamburger icon to open the side menu</Text>
      </View>
    );
  }

  navigationButtonPressed({ buttonId }) {
    if (buttonId === 'sideMenu') {
      Navigation.mergeOptions(this, {
        sideMenu: {
          left: {
            visible: true
          }
        }
      });
    }
  }
}
```
