# Animations (Preview API)


## Shared element
In order to animate shared element between two screens you need to wrap your element with `Navigation.Element` in both screens with different `elementId`.
For example, to animate `Image` element wrap it in your first screen like this:
```jsx
<Navigation.Element elementId='image1'>
  <Image source={require('img/icon.png')} />
</Navigation.Element>
```

And in your second screen:
```jsx
<Navigation.Element elementId='image2'>
  <Image source={require('img/icon.png')} />
</Navigation.Element>
```

Then call `push` or `showModal` with `customTransition.animations` options:
```js
Navigation.push(this.props.componentId, {
  component: {
    name: 'second.screen',
    options: {
      customTransition: {
        animations: [
          { type: 'sharedElement', fromId: 'image1', toId: 'image2', startDelay: 0, springVelocity: 0.2, duration: 0.5 }
        ],
        duration: 0.8
      }
    }
  }
});
```

## Peek and Pop (iOS 11.4+)

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