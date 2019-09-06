# Third Party Libraries Support

## Redux

Create a HOC which provides the redux store.
```js
import { Provider } from 'react-redux';

let store;
function ReduxProvider(Component) {
    store = store || createStore({});

    return (props) => (
        <Provider store={store}>
            <Component {...props} />
        </Provider>
    );
}
```
Wrap all Screens, which need the redux store, with the HOC.
```js
import { Navigation } from 'react-native-navigation';

export default () => {
    Navigation.registerComponent('TestScreen', () => ReduxProvider(TestScreen), () => TestScreen);
}
```

For more information about how to set up redux read the [react-redux docs](https://react-redux.js.org/)

## react-native-vector-icons

This library can be used to set icons as the following example does.
For available icons read the [react-native-vector-icons docs](https://github.com/oblador/react-native-vector-icons).

```js
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import { Navigation } from 'react-native-navigation';

export default function startApp() {
  Promise.all([
    MaterialIcons.getImageSource('home', 25),
    MaterialIcons.getImageSource('menu', 25),
    MaterialIcons.getImageSource('search', 25),
    MaterialIcons.getImageSource('add', 25),
  ]).then(([homeIcon, menuIcon, searchIcon, addIcon]) => {
    Navigation.setRoot({
      root: {
        sideMenu: {
          id: 'main',
          left: {
            component: {
              name: 'App.FirstBottomTab.HomeScreen',
            },
          },
          center: {
            bottomTabs: {
              id: 'BottomTabs',
              children: [{
                stack: {
                  id: 'FirstBottomTab',
                  children: [{
                    component: {
                      name: 'App.FirstBottomTab.HomeScreen',
                    },
                  }],
                  options: {
                    topBar: {
                      title: {
                        text: 'Home',
                      },
                      leftButtons: [{
                        id: 'sideMenu',
                        icon: menuIcon,
                      }],
                      rightButtons: [{
                        id: 'search',
                        icon: searchIcon,
                      }],
                    },
                    bottomTab: {
                      text: 'FirstBottomTab',
                      icon: homeIcon,
                    },
                    fab: {
                      id: 'addRecipe',
                      icon: addIcon,
                    },
                  },
                },
              }],
            },
          },
        },
      },
    });
  });
}
```

Its also possible to to define custom icons without react-native-vector-icons within the iOS and Android project as described [here](https://wix.github.io/react-native-navigation/#/docs/styling?id=custom-tab-icons).