# Animations

## Shared element transition

Shared element transitions allows us to provide visual continuity when navigating between destinations. It also
focuses the user's attention on a significant element which gives the user better context when transitioning to
another destination.

!> At the moment, the transition is available on iOS for push and pop while on Android it's available only for push commands.
We will soon add parity and expand the supported commands to show/dismiss modal and changing BottomTabs.

### Example
In the sections below we will use the following example from the playground app:
* [Source screen](https://github.com/wix/react-native-navigation/blob/master/playground/src/screens/sharedElementTransition/CocktailsList.js)
* [Destination screen](https://github.com/wix/react-native-navigation/blob/master/playground/src/screens/sharedElementTransition/CocktailDetailsScreen.js)

<img src="https://github.com/wix/react-native-navigation/blob/master/docs/_images/sharedElement.gif?raw=true"/>




#### Transition breakdown
Four elements are animated in this example.

* **image** - the item's image is animated to the next screen.
  * Image scale (resizeMode)
  * position on screen

* **image background** - each item has a "shadow" view which transitions to the next screen and turns into a colorful header.
  * scale
  * position on screen

* **title** - the item's title is animated to the next screen.
  * font size
  * font color
  * position on screen

* **Description** - the item's description in the destination screen.
  * fade-in
  * translation y


### API
#### Step 1 - set a nativeID prop to elements in the source screen
In order for RNN to be able to detect the corresponding native views of the elements,
each element must include a unique `nativeID` prop.

```jsx
<Image
  source={item.image}
  nativeID={`image${item.id}`}
  style={styles.image}
  resizeMode={'contain'} />
```

#### Step 2 - set a nativeID prop to elements in the destination screen

```jsx
<Image
  source={this.props.image}
  nativeID={`image${this.props.id}Dest`}
  style={styles.image} />
```

#### Step 3 - Declare the shared element animation when pushing the screen

```jsx
Navigation.push(this.props.componentId, {
  component: {
    name: Screens.CocktailDetailsScreen,
    passProps: { ...item },
    options: {
      animations: {
        push: {
          sharedElementTransitions: [
            {
              fromId: `image${item.id}`,
              toId: `image${item.id}Dest`
            }
          ]
        }
      }
    }
  }
});
```

## Element Transitions
Element transitions allow you to animate elements during shared element transitions.

### Example
In the sections below we will use the following example from the playground app:
* [Source screen](https://github.com/wix/react-native-navigation/blob/master/playground/src/screens/sharedElementTransition/CocktailsList.js)
* [Destination screen](https://github.com/wix/react-native-navigation/blob/master/playground/src/screens/sharedElementTransition/CocktailDetailsScreen.js)

### API
#### Step 1 - set a nativeID prop to elements either source or destination screens

```jsx
<Text
  nativeID='description'
  style={styles.description}>
  {this.props.description}
</Text>
```

#### Step 2 - Declare the element animation when pushing the screen

```jsx
Navigation.push(this.props.componentId, {
  component: {
    name: Screens.CocktailDetailsScreen,
    passProps: { ...item },
    options: {
      animations: {
        push: {
          elementTransitions: [
            {
              id: 'description',
              alpha: {
                from: 0, // We don't declare 'to' value as that is the element's current alpha value, here we're essentially animating from 0 to 1
                duration: SHORT_DURATION
              },
              translationY: {
                from: 16, // Animate translationY from 16dp to 0dp
                duration: SHORT_DURATION
              }
            }
          ]
        }
      }
    }
  }
});
```

## Peek and Pop (iOS 11.4+) (Preview API)

react-native-navigation supports the [Peek and pop](
https://developer.apple.com/library/content/documentation/UserExperience/Conceptual/Adopting3DTouchOniPhone/#//apple_ref/doc/uid/TP40016543-CH1-SW3) feature in iOS 11.4 and newer.

This works by passing a ref a componentent you would want to transform into a peek view. We have included a handly component to handle all the touches and ref for you.

```jsx
const handlePress ({ reactTag }) => {
  Navigation.push(this.props.componentId, {
    component {
      name: 'previewed.screen',
      options: {
        preview: {
          reactTag,
          height: 300,
          width: 300,
          commit: true,
          actions: [{
            title: "Displayed Name",
            id: "actionId",
            style: 'default', /* or 'selected', 'destructive'*/
            actions: [/*define a submenu of actions with the same options*/]
          }]
        },
      },
    },
  });
};

const Button = (
  <Navigation.TouchablePreview
    touchableComponent={TouchableHighlight}
    onPress={handlePress}
    onPressIn={handlePress}
  >
    <Text>My button</Text>
  </Navigation.TouchablePreview>
);
```

All options except for reactTag are optional. Actions trigger the same event as [navigation button presses](https://wix.github.io/react-native-navigation/#/docs/topBar-buttons?id=handling-button-press-events). To react when a preview is committed, listen to the [previewCompleted](https://wix.github.io/react-native-navigation/#/docs/events?id=previewcompleted-ios-114-only) event.
