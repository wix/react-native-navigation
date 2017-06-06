import {createStore, applyMiddleware, combineReducers} from "redux";
import {Provider} from "react-redux";
import {Navigation} from "react-native-navigation";
import thunk from "redux-thunk";
import * as reducers from "./reducers";
import * as appActions from "./reducers/app/actions";
import {registerScreens} from "./screens";
import {Platform} from "react-native";

// redux related book keeping
const createStoreWithMiddleware = applyMiddleware(thunk)(createStore);
const reducer = combineReducers(reducers);
const store = createStoreWithMiddleware(reducer);

// screen related book keeping
registerScreens(store, Provider);

// notice that this is just a simple class, it's not a React component
export default class App {
  constructor() {
    // since react-redux only works on components, we need to subscribe this class manually
    store.subscribe(this.onStoreUpdate.bind(this));
    store.dispatch(appActions.appInitialized());
  }

  onStoreUpdate() {
    const {root} = store.getState().app;
    // handle a root change
    // if your app doesn't change roots in runtime, you can remove onStoreUpdate() altogether
    if (this.currentRoot != root) {
      this.currentRoot = root;
      this.startApp(root);
    }
  }

  startApp(root) {
    switch (root) {
      case 'login':
        if (Platform.OS === 'ios') {
          Navigation.startSingleScreenApp({
            screen: {
              screen: 'example.LoginScreen',
              title: 'Login',
              navigatorStyle: {}
            },
            passProps: {
              str: 'This is a prop passed in \'startSingleScreenApp()\'!',
              obj: {
                str: 'This is a prop passed in an object!',
                arr: [
                  {
                    str: 'This is a prop in an object in an array in an object!'
                  }
                ],
                arr2: [
                  [
                    'array of strings',
                    'with two strings'
                  ],
                  [
                    1, 2, 3
                  ]
                ]
              },
              num: 1234,
              fn: function() {
                return 'Hello from a function!';
              }
            }
          });
        } else {
          Navigation.startSingleScreenApp({
           screen: {
             screen: 'example.LoginScreen',
             title: 'Login',
             navigatorStyle: {}
           },
           passProps: {
             str: 'This is a prop passed in \'startSingleScreenApp()\'!',
             obj: {
               str: 'This is a prop passed in an object!',
               arr: [
                 {
                   str: 'This is a prop in an object in an array in an object!'
                 }
               ],
               arr2: [
                 [
                   'array of strings',
                   'with two strings'
                 ],
                 [
                   1, 2, 3
                 ]
               ]
             },
             num: 1234,
             fn: function() {
               return 'Hello from a function!';
             }
           }
          });
          // Navigation.startSingleScreenApp({
          //   screen: {
          //     title: 'Example',
          //     screen: 'example.TopTabsScreen',
          //     topTabs: [
          //       {
          //         screenId: 'example.FirstTabScreen',
          //         title: 'Tab1',
          //         passProps: {
          //           str: 'This is a prop passed to Tab1',
          //           fn: () => 'Hello from a function passed as passProps!'
          //         }
          //       },
          //       {
          //         screenId: 'example.PushedScreen',
          //         title: 'Tab2',
          //         passProps: {
          //           str: 'This is a prop passed to Tab2'
          //         }
          //       },
          //       {
          //         screenId: 'example.ListScreen',
          //         title: 'Tab3',
          //         passProps: {
          //           str: 'This is a prop passed to Tab3'
          //         }
          //       }
          //     ],
          //     navigatorStyle: {}
          //   },
          //   drawer: { // optional, add this if you want a side menu drawer in your app
          //     left: { // optional, define if you want a drawer from the left
          //       screen: 'example.SideMenu' // unique ID registered with Navigation.registerScreen
          //     },
          //     disableOpenGesture: false // optional, can the drawer be opened with a swipe instead of button
          //   }
          // });
        }
        return;
      case 'after-login':
        Navigation.startTabBasedApp({
          tabs: [
            {
              label: 'One',
              screen: 'example.FirstTabScreen',
              icon: require('../img/one.png'),
              selectedIcon: require('../img/one_selected.png'),
              title: 'Screen One',
              overrideBackPress: true,
              navigatorStyle: {}
            },
            {
              label: 'Two',
              screen: 'example.SecondTabScreen',
              icon: require('../img/two.png'),
              selectedIcon: require('../img/two_selected.png'),
              title: 'Screen Two',
              navigatorStyle: {}
            }
          ],
          passProps: {
            str: 'This is a prop passed in \'startTabBasedApp\'!',
            obj: {
              str: 'This is a prop passed in an object!',
              arr: [
                {
                  str: 'This is a prop in an object in an array in an object!'
                }
              ]
            },
            num: 1234
          },
          animationType: 'slide-down',
          title: 'Redux Example',
          drawer: { // optional, add this if you want a side menu drawer in your app
            left: { // optional, define if you want a drawer from the left
              screen: 'example.BottomTabsSideMenu' // unique ID registered with Navigation.registerScreen
            },
            disableOpenGesture: false, // optional, can the drawer be opened with a swipe instead of button
            passProps: {
              title: 'Hello from SideMenu'
            }
          },
          appStyle: {
            bottomTabBadgeTextColor: '#ffffff',
            bottomTabBadgeBackgroundColor: '#ff0000'
          }
        });
        return;
      default:
        console.error('Unknown app root');
    }
  }
}
