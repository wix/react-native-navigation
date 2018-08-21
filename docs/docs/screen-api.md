# Screen API

This API is relevant when in a screen component context - it allows a screen to push other screens, pop screens, change its navigator style, etc. Access to this API is available through the `Navigation` module and expect to receive the current presented component id from screen `props.componentId`.
Component must initialize in stack in order to push another component.

## push(componentId, layout)

Push a new screen into this screen's navigation stack.

```js
Navigation.push(this.props.componentId, {
  component: {
    name: 'example.PushedScreen',
    passProps: {
      text: 'Pushed screen'
    },
    options: {
      topBar: {
        title: {
          text: 'Pushed screen title'
        }
      }
    }
  }
});
```

## pop(componentId)

Pop the top screen from this screen's navigation stack.

```js
Navigation.pop(this.props.componentId);
```

## popToRoot(componentId)

Pop all the screens until the root from this screen's navigation stack.

```js
Navigation.popToRoot(this.props.componentId);
```
## popTo(componentId)

Pop the stack to a given component.

```js
Navigation.popTo(componentId);
```

## setStackRoot(componentId, params)

Reset the current navigation stack to a new screen component (the stack root is changed).

```js
Navigation.setStackRoot(this.props.componentId, {
  component: {
        name: 'example.NewRootScreen',
        passProps: {
          text: 'Root screen'
        },
        options: {
          animated: true // Will animate root change same as push
        }
      }
});
```

## showModal(layout = {})

Show a screen as a modal.

```js
Navigation.showModal({
  stack: {
    children: [{
      component: {
        name: 'example.ModalScreen',
        passProps: {
          text: 'stack with one child'
        },
        options: {
          topBar: {
            title: {
              text: 'Modal'
            }
          }
        }
      }
    }]
  }
});
```

## dismissModal(componentId)

Dismiss the current modal.

```js
Navigation.dismissModal(this.props.componentId);
```

## dismissAllModals()

Dismiss all the current modals at the same time.

```js
Navigation.dismissAllModals();
```

<!-- ## handleDeepLink(params = {})

Trigger a deep link within the app. See [deep links](https://wix.github.io/react-native-navigation/#/deep-links) for more details about how screens can listen for deep link events.

```js
this.props.navigator.handleDeepLink({
  link: "chats/2349823023" // the link string (required)
});
```

> `handleDeepLink` can also be called statically:
```js
  import {Navigation} from 'react-native-navigation';
  Navigation.handleDeepLink(...);
``` -->

## mergeOptions(componentId, options = {})

Set options dynamically for component.

WARNING! this is called after the component has been rendered at least once.
If you want the options to apply as soon as the screen is created, use `static options(passProps){...}` or pass the options as part of the push/modal etc command.

```js
Navigation.mergeOptions(this.props.componentId, {
  topBar: {
    visible: true,
    title: {
      text: 'Title'
    }
  },
  bottomTabs: {

  },
  bottomTab: {

  },
  sideMenu: {

  },
  overlay: {

  }
});
```

<!-- ## toggleDrawer(params = {})

Toggle the side menu drawer assuming you have one in your app.

```js
this.props.navigator.toggleDrawer({
  side: 'left', // the side of the drawer since you can have two, 'left' / 'right'
  animated: true, // does the toggle have transition animation or does it happen immediately (optional)
  to: 'open' // optional, 'open' = open the drawer, 'closed' = close it, missing = the opposite of current state
});
``` -->


<!-- ## setOnNavigatorEvent(callback)

Set a handler for navigator events (like nav button press). This would normally go in your component constructor.
Can not be used in conjuction with `addOnNavigatorEvent`.

```js
// this.onNavigatorEvent will be our handler
this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
```

## addOnNavigatorEvent(callback)

Add a handler for navigator events (like nav button press). This would normally go in your component constructor.
If you choose to use `addOnNavigatorEvent` instead of `setOnNavigatorEvent` you will be able to add multiple handlers.
Bear in mind that you can't use both `addOnNavigatorEvent` and `setOnNavigatorEvent`.
`addOnNavigatorEvent` returns a function, that once called will remove the registered handler. -->

<!-- # Screen Visibility

`const isVisible = await this.props.navigator.screenIsCurrentlyVisible()`

## Listen visibility events in onNavigatorEvent handler

```js
export default class ExampleScreen extends Component {
  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }
  onNavigatorEvent(event) {
    switch(event.id) {
      case 'willAppear':
       break;
      case 'didAppear':
        break;
      case 'willDisappear':
        break;
      case 'didDisappear':
        break;
      case 'willCommitPreview':
        break;
    }
  }
}
```

## Listen to visibility events globally

```js
import {ScreenVisibilityListener as RNNScreenVisibilityListener} from 'react-native-navigation';

export class ScreenVisibilityListener {

  constructor() {
    this.listener = new RNNScreenVisibilityListener({
      didAppear: ({screen, startTime, endTime, commandType}) => {
        console.log('screenVisibility', `Screen ${screen} displayed in ${endTime - startTime} millis after [${commandType}]`);
      }
    });
  }

  register() {
    this.listener.register();
  }

  unregister() {
    if (this.listener) {
      this.listener.unregister();
      this.listener = null;
    }
  }
}
```

# Listening to tab selected events
In order to listen to `bottomTabSelected` event, set an `onNavigatorEventListener` on screens that are pushed to BottomTab. The event is dispatched to the top most screen pushed to the selected tab's stack.

```js
export default class ExampleScreen extends Component {
  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  onNavigatorEvent(event) {
	if (event.id === 'bottomTabSelected') {
	  console.log('Tab selected!');
	}
	if (event.id === 'bottomTabReselected') {
	  console.log('Tab reselected!');
	}
  }
}
```

# Peek and pop (3D touch)

react-native-navigation supports the [Peek and pop](
https://developer.apple.com/library/content/documentation/UserExperience/Conceptual/Adopting3DTouchOniPhone/#//apple_ref/doc/uid/TP40016543-CH1-SW3) feature by setting a react view reference as a `previewView` parameter when doing a push, more options are available in the `push` section.

You can define actions and listen for interactions on the pushed screen with the `PreviewActionPress` event.

Previewed screens will have the prop `isPreview` that can be used to render different things when the screen is in the "Peek" state and will then recieve a navigator event of `willCommitPreview` when in the "Pop" state. -->