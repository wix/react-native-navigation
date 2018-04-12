# Layout Types

## stack

Expect children layouts of any kind.\
When initializing with more than one screen last screen will be presented at the top of the stack.

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
  options: {

  }
}
```

## component

Component layout holds your react component.

```js
const component = {
  id: 'comopnent1', // Optional, Auto generated if empty
  name: 'Your registered component name',
  options: {},
  passProps: {
    text: 'This text will be available in your component.props'
  }
}
```

## bottomTabs

Expect children layouts

```js
const bottomTabs = {
  children: [
    {
      stack: {}
    },
    {
      component: {
        name: 'tab1',
        options: {
          bottomTab: {
            icon: require('icon')
          }
        }
      }
    }
  ],
  options: {

  }
}
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