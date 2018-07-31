# Layouts

The possibilities of the RNN layout API are wide open in terms of what you can construct with it: stacks, tabs and drawers in many combinations.

You can compose arbitrary native layout hierarchies (although some weird edge cases may not be possible or produce errors). In such cases, open an issue so that we either fix it or warn in dev time.

## component

Component layout holds a single react component.

```js
const component = {
  id: 'component1', // Optional, Auto generated if empty
  name: 'Your registered component name',
  options: {},
  passProps: {
    text: 'This text will be available in your component.props'
  }
}
```

## stack

Support children layouts of any kind.
A stack can be initialised with more than one screen, in which case the last screen will be presented at the top of the stack.

```js
const stack = {
  children: [
    {
      component: {}
    },
    {
      component: {}
    }
  ],
  options: {}
}
```

## bottomTabs

```js
const bottomTabs = {
  children: [
    {
      stack: {
        children: [],
        options: {
          bottomTab: {
            text: 'Tab 1',
            icon: require('../images/one.png')
          }
        }
      }
    },
    {
      component: {
        name: 'secondTabScreen',
        options: {
          bottomTab: {
            text: 'Tab 2',
            icon: require('../images/two.png')
          }
        }
      }
    }
  ],
  options: {}
}
```

### Selecting tabs programmatically

The selected index is a style property which can be updated using the `mergeOptions` command. In order to update the BottomTabs options, Pass the BottomTabs `componentId` or the `componentId` of one of its children.

?>We'll use the following BottomTabs layout to demonstrate programmatic tab selection.

```js
const bottomTabs = {
  id: 'BottomTabsId',
  children: [
    {
      component: {
        name: 'FirstScreen',
        options: { ... }
      }
    },
    {
      component: {
        id: 'SecondScreenId',
        name: 'SecondScreen',
        options: { ... }
      }
    }
  ]
}
```

#### selecting a tab by index

The following `mergeOptions` command will select the second tab. We're passing the id of our BottomTabs, but we could also use the id of any of the child components, for example `SecondScreenId`.

```js
Navigation.mergeOptions('BottomTabsId', {
  bottomTabs: {
    currentTabIndex: 1
  }
});
```

#### selecting a tab by componentId

Tabs can also be selected by componentId. This is particularly useful in cases where you don't know in which tab a screen is contained.

For example, if invoked from one of the child components;`SecondScreen` or `FirstScreen`, the following merge command will select the tab containing the child.

```js
Navigation.mergeOptions(this.props.componentId, {
  bottomTabs: {
    currentTabId: this.props.componentId
  }
});
```

### Updating options for a specific tab
Updating (merging) tab specific options is done using the `mergeOptions` command. `mergeOptions` expects a `componentId` as first argument, therefore in order to update a specific tab we'll need to pass a `componentId` of a child of that specific tab.
For example, Using the layout specified above, To update the `badge` property of the second tab we'll call `mergeOptions` with `SecondScreenId`.

```js
Navigation.mergeOptions('SecondScreenId', {
  bottomTab: {
    badge: 'New'
  }
});
```

## sideMenu

Expect center, left and right layouts

```js
const sideMenu = {
  left: {
    component: {}
  },
  center: {
    stack: {}
  },
  right: {
    component: {}
  }
}
```

## splitView (iOS only)

Master and Detail based layout.

You can change the it's options with `Navigation.mergeOptions('splitView1', { maxWidth: 400 })`.

```js
const splitView = {
  id: 'splitView1', // Required to update options
  master: {
    // All layout types accepted supported by device, eg. `stack`
  },
  detail: {
    // All layout types accepted supported by device, eg. `stack`
  },
  options: {
    displayMode: 'auto', // Master view display mode: `auto`, `visible`, `hidden` and `overlay`
    primaryEdge: 'leading', // Master view side: `leading` or `trailing`
    minWidth: 150, // Minimum width of master view
    maxWidth: 300, // Maximum width of master view
  },
}
```

## Layout Examples

### Single page app with two side menus:

```js
Navigation.setRoot({
  root: {
    sideMenu: {
      left: {
        component: {
          name: 'navigation.playground.TextScreen',
          passProps: {
            text: 'This is a left side menu screen'
          }
        }
      },
      center: {
        component: {
          name: 'navigation.playground.WelcomeScreen'
        },
      },
      right: {
        component: {
          name: 'navigation.playground.TextScreen',
          passProps: {
            text: 'This is a right side menu screen'
          }
        }
      }
    }
  }
});
```

### Tab based app (with passProps example):

```js
Navigation.setRoot({
  root: {
    bottomTabs: {
      children: [
        {
          component: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is tab 1',
              myFunction: () => 'Hello from a function!',
            },
          },
        },
        {
          component: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is tab 2',
            },
          },
        },
      ],
    },
  }
});
```

### Stack based app (with options example, initialised with 2 screens):

```js
Navigation.setRoot({
  root: {
    stack: {
      options: {
        topBar: {
          visible: false
        }
      },
      children: [
        {
          component: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is tab 1',
              myFunction: () => 'Hello from a function!',
            }
          }
        },
        {
          component: {
            name: 'navigation.playground.TextScreen',
            passProps: {
              text: 'This is tab 2',
            }
          }
        }
      ]
    }
  }
});
```
