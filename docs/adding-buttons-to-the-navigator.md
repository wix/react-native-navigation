# Adding buttons to the navigator

Nav bar buttons can be defined per-screen by adding `static navigatorButtons = {...};` on the screen component definition. This object can also be passed when the screen is originally created; and can be overridden when a screen is pushed. Handle onPress events for the buttons by setting your handler with `navigator.setOnNavigatorEvent(callback)`.

```js
class FirstTabScreen extends Component {
  static navigatorButtons = {
    rightButtons: [
      {
        title: 'Edit', // for a textual button, provide the button title (label)
        id: 'edit', // id for this button, given in onNavigatorEvent(event) to help understand which button was clicked
        testID: 'e2e_rules', // optional, used to locate this view in end-to-end tests
        disabled: true, // optional, used to disable the button (appears faded and doesn't interact)
        disableIconTint: true, // optional, by default the image colors are overridden and tinted to navBarButtonColor, set to true to keep the original image colors
        showAsAction: 'ifRoom', // optional, Android only. Control how the button is displayed in the Toolbar. Accepted valued: 'ifRoom' (default) - Show this item as a button in an Action Bar if the system decides there is room for it. 'always' - Always show this item as a button in an Action Bar. 'withText' - When this item is in the action bar, always show it with a text label even if it also has an icon specified. 'never' - Never show this item as a button in an Action Bar.
        buttonColor: 'blue', // Optional, iOS only. Set color for the button (can also be used in setButtons function to set different button style programatically)
        buttonFontSize: 14, // Set font size for the button (can also be used in setButtons function to set different button style programatically)
        buttonFontWeight: '600', // Set font weight for the button (can also be used in setButtons function to set different button style programatically)
      },
      {
        icon: require('../../img/navicon_add.png'), // for icon button, provide the local image asset name
        id: 'add' // id for this button, given in onNavigatorEvent(event) to help understand which button was clicked
      }
    ]
  };

  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  onNavigatorEvent(event) { // this is the onPress handler for the two buttons together
    if (event.type == 'NavBarButtonPress') { // this is the event type for button presses
      if (event.id == 'edit') { // this is the same id field from the static navigatorButtons definition
        AlertIOS.alert('NavBar', 'Edit button pressed');
      }
      if (event.id == 'add') {
        AlertIOS.alert('NavBar', 'Add button pressed');
      }
    }
  }

  render() {
    return (
      <View style={{flex: 1}}>...</View>
     );
  }
```

#### Buttons object format

```js
{
  rightButtons: [{ // buttons for the right side of the nav bar (optional)
    title: 'Edit', // if you want a textual button
    icon: require('../../img/navicon_edit.png'), // if you want an image button
    id: 'compose', // id of the button which will pass to your press event handler. See the section bellow for Android specific button ids
    testID: 'e2e_is_awesome', // if you have e2e tests, use this to find your button
    disabled: true, // optional, used to disable the button (appears faded and doesn't interact)
    disableIconTint: true, // optional, by default the image colors are overridden and tinted to navBarButtonColor, set to true to keep the original image colors
    buttonColor: 'blue', // Set color for the button (can also be used in setButtons function to set different button style programatically)
    buttonFontSize: 14, // Set font size for the button (can also be used in setButtons function to set different button style programatically)
    buttonFontWeight: '600', // Set font weight for the button (can also be used in setButtons function to set different button style programatically)
  }],
  leftButtons: [] // buttons for the left side of the nav bar (optional)
}
```

##### Android left button
On Android, four button types are supported by default without the need to provide an icon. You can use them by specifying one of the following ids in your left button definition:

* back
* cancel
* accept
* sideMenu

#### Floating Action Button (FAB) - Android only
Each screen can contain a single Fab which is displayed at the bottom right corner of the screen.

* Simple Fab:
```js
  static navigatorButtons = {
    fab: {
      collapsedId: 'share',
      collapsedIcon: require('../../img/ic_share.png'),
      backgroundColor: '#607D8B'
    }
  };
```

* Fab with expanded state
[Example](https://material-design.storage.googleapis.com/publish/material_v_9/0B8v7jImPsDi-ZmQ0UnFPZmtiSU0/components-buttons-fab-transition_speeddial_02.mp4)
```js
    fab: {
      collapsedId: 'share',
      collapsedIcon: require('../../img/ic_share.png'),
      expendedId: 'clear',
      expendedIcon: require('../../img/ic_clear.png'),
      backgroundColor: '#3F51B5',
      actions: [
        {
          id: 'mail',
          icon: require('../../img/ic_mail.png'),
          backgroundColor: '#03A9F4'
        },
        {
          id: 'twitter',
          icon: require('../../img/ic_twitter.png'),
          backgroundColor: '#4CAF50'
        }
      ]
    }
```

#### Contextual TopBar Menu - Android only
A contextual menu offers actions that affect a specific item or context frame in the UI. You can provide a context menu for any view, but they are most often used for items in a ListView, GridView, or other view collections in which the user can perform direct actions on each item. (Taken from the [Android documentation](https://developer.android.com/guide/topics/ui/menus.html#context-menu))

#### showing the contextual menu

```js
this.props.navigator.showContextualMenu(
  {
    rightButtons: [
      {
        title: 'Add',
        icon: require('../img/add.png')
      },
      {
        title: 'Delete',
        icon: require('../img/delete.png')
      }
    ],
    onButtonPressed: (index) => console.log(`Button ${index} tapped`)
  }
);
```

##### Hiding the contextual menu
```js
this.props.navigator.dismissContextualMenu();
```


To style the `ContextualMenu`, use the following properties in the screen's `navigatorStyle`:
```js
static navigatorStyle = {
  contextualMenuStatusBarColor: '#0092d1',
  contextualMenuBackgroundColor: '#00adf5',
  contextualMenuButtonsColor: '#ffffff'
};
```
