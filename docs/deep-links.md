# Deep Links

Deep links are strings which represent internal app paths/routes. They commonly appear on push notification payloads to control which section of the app should be displayed when the notification is clicked. For example, in a chat app, clicking on the notification should open the relevant conversation on the "chats" tab.

Another use-case for deep links is when one screen wants to control what happens in another sibling screen. Normally, a screen can only push/pop from its own stack, it cannot access the navigation stack of a sibling tab for example. Returning to our chat app example, assume that by clicking on a contact in the "contacts" tab we want to open the relevant conversation in the "chats" tab. Since the tabs are siblings, you can achieve this behavior by triggering a deep link:

```js
onContactSelected(contactID) {
  this.props.navigator.handleDeepLink({
    link: 'chats/' + contactID,
    payload: '' // (optional) Extra payload with deep link
  });
}
```

> Tip: Deep links are also the recommended way to handle side drawer actions. Since the side drawer screen is a sibling to the rest of the app, it can control the other screens by triggering deep links.

#### Handling deep links

Every deep link event is broadcasted to all screens. A screen can listen to these events by defining a handler using `setOnNavigatorEvent` (much like listening for button events). Using this handler, the screen can filter links directed to it by parsing the link string and act upon any relevant links found.

```js
export default class SecondTabScreen extends Component {
  constructor(props) {
    super(props);
    // if you want to listen on navigator events, set this up
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }
  onNavigatorEvent(event) {
    // handle a deep link
    if (event.type == 'DeepLink') {
      const parts = event.link.split('/'); // Link parts
      const payload = event.payload; // (optional) The payload

      if (parts[0] == 'tab2') {
        // handle the link somehow, usually run a this.props.navigator command
      }
    }
  }
```

#### Deep link string format

There is no specification for the format of deep links. Since you're implementing the parsing logic in your handlers, you can use any format you wish.
